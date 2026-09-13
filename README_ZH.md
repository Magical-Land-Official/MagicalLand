<div align="center">

<img src="Resources/Icon/full-缩放.png" alt="Magical Land 标志" width="45%">

# 魔法大陆

把自己的小马带进《我的世界》。

[English](README.md) | 简体中文

</div>

**魔法大陆（Magical Land）** 是一款受《小马宝莉：友谊是魔法》启发的粉丝模组。你可以搭配小马造型、调整颜色、绘制可爱标志，并保存自己的角色预设。

[版本下载](https://github.com/Elysian-Herd-Studio/Magical-Land/releases) · [使用与开发文档](docs/README.md) · [问题反馈](https://github.com/Elysian-Herd-Studio/Magical-Land/issues)

## 角色自定义

- **造型搭配**：分别选择前发、后发、尾巴和眼型，鬃毛与尾巴可独立镜像。
- **自由配色**：调整身体、鬃毛和眼睛的颜色。身体与鬃毛的高光、阴影可自动搭配或手动设置。
- **六区挑染**：前发、后发和尾巴各有六个色区，可分别搭配颜色。
- **自绘可爱标志**：在 12×12 像素画布上绘制图案，左右两侧可共用或分别设计。
- **角色预设**：保存喜欢的造型，也可以复制预设尝试新搭配。

小马会眨眼、抖动耳朵并注视周围的生物，部分动作带有对应表情。独角兽持物时，物品会随动作轻晃，伴随魔法光晕、星点和音效。

飞行时，有翼角色拍翼，无翼角色悬浮。生存飞行等能力由可选扩展 [Magical Land Gameplay](https://github.com/Elysian-Herd-Studio/Magical-Land-Gameplay) 提供，包含三族能力与成就系统。

<p align="center">
  <img src="https://github.com/user-attachments/assets/9d5d3c8d-fb6e-4066-bb05-3bfdbf14ad2d" alt="暮光闪闪的小马模型展示" width="45%">
  <img src="https://github.com/user-attachments/assets/be96510c-c7bc-45e5-a75e-e2c09c7155fb" alt="暮光闪闪的另一视角展示" width="45%">
</p>

*早期游戏截图，展示模型与美术风格。*

## 安装与使用

当前开发版本为 **0.3.6**，适用于 **Minecraft Java 1.20.1 / Fabric**。需要以下依赖：

- Fabric Loader **0.19.1 或更高版本**；
- 适配 Minecraft 1.20.1 的 **Fabric API**；
- 适配 Minecraft 1.20.1 的 **GeckoLib 4.7 或更高版本**。

从 [Releases](https://github.com/Elysian-Herd-Studio/Magical-Land/releases) 获取安装文件，将模组和依赖的 JAR 放进游戏的 `mods` 文件夹。由旧一体包升级时，先移除旧包，再安装新版。

点击游戏主菜单右下角的 **“捏马”** 编辑角色，点击 **“保存并应用”** 后生效。默认按 **F9** 打开设置，也可通过 Mod Menu 进入。更多操作见[捏脸指南](docs/customization-ui.md)。

## 多人与 Gameplay

| 安装方式 | 使用范围 |
| --- | --- |
| 客户端安装本模组及依赖 | 本地小马造型与捏马。 |
| 客户端和服务器都安装本模组及依赖 | 同步玩家造型、动画与注视。 |
| 双端再安装兼容的 Gameplay | 使用三族能力与成就等玩法。 |

多人外观同步需要服务器支持，服务器使用同一个 Magical Land JAR。Gameplay 的依赖版本与种族规则见其仓库说明。

## 开发进度与反馈

项目仍在开发中，欢迎试用和反馈。已知问题与后续计划见[项目清单](TODO.md)。

反馈问题时，请附上游戏和模组版本、是否安装 Gameplay、单人或多人环境、复现步骤，以及必要的截图或日志。公开日志前请检查其中的个人信息。

## 一起参与

我们希望继续打磨模型、手绘纹理、动画和捏脸体验。欢迎参与 Java 开发、模型与动画制作、美术、测试和翻译，也欢迎在 [Issues](https://github.com/Elysian-Herd-Studio/Magical-Land/issues) 分享想法。

开发资料：[双仓开发说明](docs/module-split.md) · [公共 API](docs/appearance-api.md)

- QQ：2026010008
- Discord：mayhooves
- 电子邮件：w2026010008@outlook.com

本项目为非官方粉丝创作。仓库许可见 [LICENSE.txt](LICENSE.txt)，素材信息见相应文档；[魔法音效来源记录](docs/magic-sounds.md)仍在补充。
