# 双仓构建与联合开发客户端启动记录

日期：2026-09-10。

范围：Magical Land 与 Gameplay 独立构建、公共 API 依赖和联合开发客户端启动；不含完整游戏验收。

## 对应代码

| 仓库 | 分支 | 提交 | 构建版本 |
| --- | --- | --- | --- |
| Magical Land | `1.20.1-Fabric` | `0b3cf2d993a4a1f7527fb8b427bde16b6dbc3c6a` | `0.3.0` |
| Gameplay | `1.20.1-Fabric` | `70061236c84b9f765a738daa8e531ebccf42c134` | `0.1.0` |

本轮接续[迁移记录](../repository-migration.md)。

## 本轮结果

- Magical Land 的 `build` 和 `publishMavenJavaPublicationToLocalDevelopmentRepository` 成功，生成普通模组、公共 API、源码及 API 源码产物。
- Gameplay 使用主仓本地发布的版本化产物完成 `build`，未共享同级源码目录。
- 两仓标准 Gradle `test` 均为 `NO-SOURCE`；自定义测试另行执行。
- 本轮另行执行的外观架构检查通过 474 项，Gameplay 架构检查通过 169 项，远程能力结构检查通过 77 项。更早的独立编译和逻辑测试，仍以各自记录为准。
- Magical Land 普通 JAR 未包含 Gameplay 包、远程工具和金胡萝卜彩蛋的目标条目；包含 7 个公共 API 类，并保留原同步实现。
- 启动联合开发客户端，日志确认载入 `magicaland 0.3.0` 与 `magicaland_gameplay 0.1.0`，完成资源图集与声音引擎初始化。只检查进程和日志，未操作游戏窗口。
- 测试使用旧测试世界与设置的独立副本，未删除备份或覆盖原世界；构建后两个仓库的跟踪文件均无新增改动。

构建环境为 Windows、JDK 25.0.4、Gradle 9.4.1、Loom 1.16.3，Java 编译输出目标为 17。运行组合为 Minecraft 1.20.1、Fabric Loader 0.19.1、Fabric API 0.92.7+1.20.1、GeckoLib 4.8.3、Mod Menu 7.2.2。

联合开发客户端由 Gameplay 仓库启动：Magical Land 使用发布 JAR 经 Loom 重映射后的依赖，Gameplay 使用开发类与资源。当时未验证普通启动器安装两个发行 JAR。

## 已知日志提示

构建包含弃用 API 和 Gradle 未来版本兼容提示；启动日志包含 JDK / LWJGL、GeckoLib 模型格式版本及离线账号 Realms 授权提示。本轮未处理。

## 测试范围

2026-09-10，联合开发客户端体验验收通过，未提供逐项操作记录。当时未覆盖独立服务器与双客户端、仅主模组安装和普通启动器发行 JAR 安装。

本记录对应 Magical Land 0.3.0 与 Gameplay 0.1.0；不覆盖后续出窍重构和公共 API 扩展。
