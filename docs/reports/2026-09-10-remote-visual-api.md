# 出窍视觉 API 扩展检查

本文记录 Magical Land 0.3.1 / API 1.1 的独立检查。后续 Magical Land 0.3.2 / API 1.2 与 Gameplay 0.2.1 已完成构建和体验验收，含亮角、渲染坐标、物品朝向与拾取修复，见 [Gameplay 后续验收记录](https://github.com/Magical-Land-Official/Magical-Land-Gameplay/blob/1.20.1-Fabric/docs/reports/2026-09-10-remote-pickup-pose.md)。当时未记录多人联机及其他渲染模组兼容性的实测结果。

日期：2026-09-10。对应工作区：Magical Land 0.3.1 / API 1.1，起点 `0b3cf2d993a4a1f7527fb8b427bde16b6dbc3c6a`；联调 Gameplay 0.2.0，起点 `70061236c84b9f765a738daa8e531ebccf42c134`。两仓均为 `1.20.1-Fabric` 上本轮未提交的源码改动。

## 内容

新增 `ItemVisualContext`，由扩展提供投影实体、物品、收放与挥动状态。第一人称与世界悬浮入口按投影隔离惯性缓存，兼容旧调用。Gameplay 仅通过公共 API 接入。

第一人称独立视图保留原版换物升降，跳过本体的侧边飞入路径，避免双重位移。物品颜色继续取所有者外观，运动源使用投影。接口说明见[公共 API](../appearance-api.md)。

本轮没有修改光效素材、着色器、飞行动画或身体几何，未涉及新的魔法美术、无翼角色站立悬浮及独角兽全身包裹。

## 验证

- 135 个生产 Java 源码在真实依赖下以 `--release 17` 独立编译通过。
- 公共 API 65 项、公开签名与仅 API 消费者编译通过；Gameplay 43 个源码仅使用 8 个公开 API 类编译通过。
- 外观架构 477 项、惯性运动 349,360 项、收放运动 23,207 项、独立视觉缓存隔离 7 项通过。
- 捏脸与预览的 13 套逻辑回归共 1,615,146 项通过。额外 OpenGL 状态测试仅编译，未执行。
- 使用本轮两包元数据的 Fabric 无窗口夹具中，23 个 Mixin 目标类转换通过。

环境为 Windows / JDK 25.0.4，Java 输出目标 17；Minecraft 1.20.1、Fabric Loader 0.19.1、Fabric API 0.92.7、GeckoLib 4.8.3。没有完整 Gradle 构建、发布 JAR、启动游戏或进行本轮多人验收。

此前的[双仓构建验收](2026-09-10-split-build.md)对应 Magical Land 0.3.0 / Gameplay 0.1.0。新增出窍库存、遮挡及客户端测试见随本轮改动发布的 [Gameplay 重构检查](https://github.com/Magical-Land-Official/Magical-Land-Gameplay/blob/1.20.1-Fabric/docs/reports/2026-09-10-remote-rebuild.md)。

## 测试范围与证据

本轮独立检查未覆盖普通本体收放、出窍持物与光晕的实机表现，也未覆盖其他渲染模组组合。

本地证据：`work/remote-rebuild-20260910/verification/appearance-verification.json`、同目录最终 API/Knot 检查，以及专项 `visual-api-20260910/run-548a8dbe`。依赖副本、临时测试 JAR 与日志保留在本地；本轮代码当时尚未推送。
