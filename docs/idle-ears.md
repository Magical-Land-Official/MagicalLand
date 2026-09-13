# 随机待机耳动

只在健康、站立的 `idle` 中启用。移动、后退、潜行、受伤、睡眠、跳落、飞行、游泳、乘骑／坐姿和手工表演动作均不叠加；`sneak`、`sneaking`、`attacked`、`slow_walk_earsdown`、`pp_walk`、`break` 等原有耳姿不改。

`PonyIdleEars` 根据玩家 UUID 和世界时间安排耳动。每个 5 秒窗口有 50% 的概率发生耳动，时间随机错开；其中左耳、右耳和错峰双耳约占 45%、45% 和 10%。

大多数为单抽，约 20% 为双抽；幅度取原动作的 35%／45%／55%，时长略有变化。两耳均可先动，事件预先排定。以上为美术调节参数，并非马匹生理频率的实测值。

`PonyIdleEarAnimations` 从 `ear_parallel` 的旋转轨道截取短抽动，再减幅、镜像并错开左右，保留原曲线。36 个变体缓存在内存中，资源重载后重建；来源缺失或动态旋转不受支持时停用。

联网仍只使用原 `ear_controller = ear_parallel / 空` 许可，不新增消息或向服务端发送内部变体名。同 UUID、相同世界时间有相同机会表，实际播放需各客户端已持续看见健康 idle 满 1 秒；新加入、刚切回视野或网络许可延迟时不补播错过的抽动，因此不承诺逐帧绝对同步。时间回退、世界／实体更换、超过 5 tick 未观察或离开 idle 会取消旧事件。

设计依据：Virginia Cooperative Extension 说明马耳可朝不同方向转动，而紧贴颈部的压耳具有明确的情绪含义；因此这里选择低幅、左右不机械同步的待机动作，不随机叠加大幅压耳。[Virginia Tech：Do You Have Horse Sense!](https://www.pubs.ext.vt.edu/content/pubs_ext_vt_edu/en/380/380-107/380-107.html) 马耳向后也需要结合正在做的活动判断，不把每次耳动直接解释为生气。[University of Kentucky / Extension Horses：Horse Body Language](https://horses.extension.org/horse-body-language/)

`PonyIdleEarsTest` 的 33704 项离线检查通过，覆盖概率分布、两耳覆盖、错峰双耳、不同帧率机会一致、重复绘制不重抽、世界／实体隔离、作者曲线不变、镜像方向、幅度、缓存刷新与原动作优先。原头部视角与后退锁定回归同时通过。
