# 表情基础维修与解耦（2026-09-08）

记录 2026-09-08 的表情轨道拆分与基础维修。动作与表情的搭配由资源配置决定。

## 资源分工

- 身体动作保留原名、时长和身体关键帧，不再直接包含眼睛轨道。
- `animations/mare_animation.json` 中新增 `face.*` 表情片段；普通眨眼仍单独使用原有 `blink_parallel`。
- `assets/magicaland/expressions.json` 集中维护稳定 ID、中文名称、动作搭配和眼型骨骼映射，支持资源重载。配置结构无效时保留上一份有效配置；修改时仍须保证引用的动画片段与骨骼实际存在。

| ID | 内容 |
| --- | --- |
| `neutral` | 普通 |
| `happy` | 笑眼 |
| `angry` | 生气 |
| `closed` | 闭眼 |
| `scrunched` | 挤眼 |
| `look_back` / `look_down` | 向后看／向下看 |
| `fall_transition` | 保留原轨道的下落过渡 |
| `wave` / `ballet` | 保留原关键帧节奏的挥手／芭蕾表情演出 |

ID 不依赖列表顺序，中文名称可以修改。`happy` 当前只代表已有笑眼，不会自动创建新的嘴部动作。已有模型中的 `Smeile` 拼写保留，避免改骨骼名波及其他资源。

例如让跳跃使用笑眼，可将 `actions` 中的 `"jump1": "neutral"` 改为 `"jump1": "happy"`。资源中的 `jump1` 与 `run` 默认使用普通眼。睡眠和潜行的闭眼规则移至配置；旧 `sneak` 的向下看轨道另存为 `look_down`。

下落的组合动作使用 `{"expression": "fall_transition", "then_loop": "neutral"}`，先播放过渡表情，再循环普通表情。原下落过渡缺少普通眼与闭眼显隐值，本次保留原有轨道，并补齐 `CommonFace=1`、`close=0`，避免依赖上一动作或回到模型默认值后叠眼。挥手和芭蕾保留作者制作的动态表情及原有变化节奏。

Java 可通过 `PonyExpressions.forExpression("happy")` 取得 `RawAnimation`；`forAction` 查询动作搭配，`expressions()` 提供 ID 与中文名称。未知 ID 回退普通表情。玩家表情系统可复用这些入口，手动覆盖的优先级、持续时间与多人同步仍需另行实现。

## 新增眼型

保留现有 `PonyStyleRegistry` 样式注册流程，在 `eye_styles` 增加对应骨骼即可，例如：

```json
"04": {
  "normal": "Style04CommonFace",
  "left_pupil": "Style04LeftEye",
  "right_pupil": "Style04RightEye",
  "happy": "Style04Smile"
}
```

这些是示例名，必须先有实际模型骨骼，才可填入正式配置。省略的表情通道复用公共骨骼；不需要逐个身体动画增加 04 轨道。01 的标准骨骼作为逻辑参考通道保留。不同通道不能指向同一骨骼。差别较大的眼型仍需人工校准形状与缩放轴心，不能保证任意新拓扑自动合适。

## 基础维修

- 移除渲染器里睡眠／潜行隐藏 `emot` 的逻辑，避免其子节点 `close` 一同消失。由独立表情控制器选择闭眼。
- 01 的普通眼、笑眼与眼仁姿态通过统一适配层传给 02／03，修复 `Style03Smile` 未被正确过滤和控制的问题。
- 02／03 眼仁原 pivot 在原点；本次渲染临时使用同侧标准眼仁 pivot，避免缩放漂移。
- 动作已指定笑眼、持续闭眼或挤眼时，普通眨眼不替换其眼型。作者的眼神位移优先于普通眨眼的小幅位移，眼仁缩放仍由眨眼控制。
- 临时骨骼修改在 `Emotions` 子树渲染结束后恢复，包括姿态、pivot 和变更标记，避免跨帧或跨玩家残留。

眨眼沿用约 3 秒的循环，眼仁缩放为 `0.90 → 1.07 → 1.00`。

本地与远端玩家从现有主动作推导对应表情，不新增表情数据包；捏脸和标题界面的预览也注册了表情控制器。控制器顺序为身体、眨眼、表情、耳朵、尾巴。

## 同步范围与 Blockbench 编辑

运行时 mare 动画、`Resources/Animations` 下的 mare／changeling 动画，以及 `Mare.bbmodel`／`Changeling.bbmodel` 均拆出同名表情片段。身体关键帧、模型 geometry、UV、贴图与骨骼层级未变；两种模型各自原有的身体动画差异保留。

Blockbench 中需要组合预览身体动作、对应 `face.*` 与眨眼片段。保持表情轨道独立，避免身体与表情控制器同时修改眼睛。原项目快照保存在本地维护资料的 `work/face-basic-repair-20260908/original` 中。

## 验证与限制

当时的独立 main 回归测试为 `tests/face/PonyFacePoseTest.java`，使用真实 `mare_geo.json` 和 GeckoLib 4.8.3 的 `GeoBone` 检查三种眼型、闭眼父子关系、眼仁映射、pivot、重复切换和异常恢复。还检查配置改绑动作、新增模拟 04 眼型和错误配置回退。此路径记录当时布局，测试源码现存于本地维护资料。

当时使用 Java 17+、GeckoLib 4.8.3、fastutil、JOML、Gson JAR，单独编译 `PonyExpressions.java`、`PonyFacePose.java`、`PonyGazeMath.java` 和测试类，将 `src/main/resources` 加入运行 classpath，以 `top.csituka.magicaland.client.render.PonyFacePoseTest <mare_geo.json 路径> <expressions.json 路径>` 运行。检查范围为独立类与资源，不包含完整模组编译或 Minecraft 渲染。

结构核对覆盖三个动画 JSON、两个 Blockbench 项目的身体轨道、原始眨眼、演出时长及非动画数据，通过 379 项检查；独立回归测试通过 5029 项断言。

随后在隔离开发环境完成编译、启动和表情验收，运行时通过 905 项断言，详见[游戏验收记录](face-runtime-check.md)。未覆盖双客户端同步、挥手／芭蕾的完整动作组合，也未打包发布 JAR。

后续加入的[自动眼仁注视](automatic-gaze.md)有独立检查记录；上述游戏结果早于注视功能加入。
