# 魔法大陆文档

[返回中文首页](../README_ZH.md) · [English README](../README.md)

Magical Land 的玩家指南、开发资料与历史记录。安装与版本信息见[首页](../README_ZH.md#安装与使用)。

## 玩家指南与功能说明

初次使用可从[捏脸界面](customization-ui.md)了解款式、预设与“保存并应用”。

| 想了解什么 | 阅读入口 |
| --- | --- |
| 捏脸、预设、预览镜头与保存 | [捏脸界面](customization-ui.md)、[预览待机](preview-idle.md) |
| 鬃毛和尾巴配色、联动与挑染 | [基础调色](mane-palette.md)、[颜色二级菜单](color-submenus.md)、[六区遮罩分色](mane-mask-dye.md)、[左右镜像](mane-mirroring.md) |
| 眼色与可爱标志 | [眼睛颜色](eye-colors.md)、[可爱标志](cutie-marks.md) |
| 眼神、转头与耳动 | [自动注视](automatic-gaze.md)、[头部朝向](head-look.md)、[待机耳动](idle-ears.md) |
| 拍翼、无翼悬浮与独角兽包身光 | [飞行姿态](flight-visuals.md) |
| 落蹄声音与联机去重 | [马蹄声](guides/hoof-sounds.md) |
| 陆马与天马拿东西 | [嘴咬与蹄托持物](guides/nonmagical-held-items.md) |
| 魔法外观和声音 | [魔法光效](magic-glow.md)、[手持物光晕](held-item-aura.md)、[收放物品](魔法收放物品.md)、[惯性与拖尾](魔法手持物惯性与拖尾.md)、[手持物音效](magic-sounds.md) |
| 种族能力与玩法扩展 | [Gameplay 文档](https://github.com/Elysian-Herd-Studio/Magical-Land-Gameplay/blob/1.20.1-Fabric/docs/README.md) |

## 开发与公共 API

- [双仓开发说明](module-split.md)：安装组合与联合开发。
- [公共 API](appearance-api.md)：扩展接口。
- [双仓迁移记录](repository-migration.md)
- [协作测试](../tests/)
- [音频资产清单](magic-sound-assets.json)：素材来源与授权记录。
- [项目待办](../TODO.md)

## 历史报告与维修记录

历史报告以当时的版本与测试范围为准。

| 日期 | 记录 |
| --- | --- |
| 2026-09-11 | [马蹄声接入](reports/2026-09-11-hoof-sounds.md) |
| 2026-09-10 | [飞行活动 API](reports/2026-09-10-flight-activity-api.md) |
| 2026-09-10 | [蹲行动画随步速调整](reports/sneak-motion-20260910.md) |
| 2026-09-10 | [独角兽飞行屏幕边缘光罩](reports/unicorn-flight-rim-20260910.md) |
| 2026-09-10 | [飞行、光效与玩法设置验收](reports/unified-magic-20260910.md) |
| 2026-09-10 | [起飞黑屏修复](reports/flight-blackout-20260910.md) |
| 2026-09-10 | [悬浮与配声调整](reports/flight-polish-20260910.md)、[角光纹路调整](reports/horn-pattern-20260910.md)、[飞行初次检查](reports/flight-visuals-20260910.md) |
| 2026-09-10 | [持物视觉 API 检查](reports/2026-09-10-remote-visual-api.md) |
| 2026-09-10 | [双仓构建与启动](reports/2026-09-10-split-build.md) |
| 2026-09-10 | [双仓迁移检查](repository-migration.md) |
| 2026-09-09 | [共享鬃毛纹理维修](mane-shared-texture-repair.md) |
| 2026-09-08—09 | [自动注视、双客户端检查与眼位调整](reports/2026-09-09-gaze-history.md) |
| 2026-09-08 | [表情系统游戏验收](face-runtime-check.md) |
| 2026-09-08 | [颜色二级菜单游戏验收](color-submenus-runtime-check.md) |
| 2026-09-08 | [款式缩略图维修](style-thumbnail-repair.md) |
| 2026-09-08 | [表情基础维修与解耦](face-basic-repair.md) |
| 2026-09-07 | [鬃毛与尾巴调色游戏验收](mane-palette-runtime-check.md) |
| 2026-08-15；9 月 7 日更新 | [款式数字编号迁移](pony-style-numeric-rename.md) |

## 规划与历史归档

- [独立外观同步服务备选](appearance-sync-future.md)：尚未实施。
- [v1 宣传片分镜与配乐草案](v1-trailer-draft.md)
- [文档维护与归档规则](development/documentation.md)
