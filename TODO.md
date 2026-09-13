# 项目待办

## 功能与维护

- [ ] 为[嘴咬与蹄托持物](docs/guides/nonmagical-held-items.md)补充弓弩动作及异形物品适配。
- [ ] 评估[独立外观同步服务](docs/appearance-sync-future.md)方案。
- [ ] 分开处理首次外观同步与“广播自身外观”开关，减少重复发送完整外观配置。
- [ ] 补齐[魔法音效](docs/magic-sounds.md)的原始来源链接、作者、授权与署名记录。
- [ ] 恢复款式缩略图的实时调色，完善按部件缓存失效、请求合并、旧图回退和帧预算，以及窗口缩放、缓存淘汰、页面关闭和资源重载时的渲染状态恢复。详见[缩略图维修说明](docs/style-thumbnail-repair.md)。

## 美术

- [ ] 调整 AJ 前发 `05`（`Style05FrontMane`），以现行的四处尖端轻微收拢版本为基础，保持与其他后发混搭的衔接，减少厚板感和过度分束。
- [ ] 重新设计后发 `07`（搁置）：采用贴合现有风格的发束轮廓与细碎层次。保留 `07` 编号，丸子头继续使用后发 `08`。
- [ ] 重新设计尾巴 `08`（搁置）：保留短小、团起的方向，增加盘团层次、发束走向和收束质感，与 `08` 背头、丸子头协调。

模型编辑入口为 [Mare.bbmodel](Resources/BlockbenchProjects/Mare.bbmodel)，资源修改同步到[源几何](Resources/Models/Mare.geo.json)和[运行几何](src/main/resources/assets/magicaland/geo/mare_geo.json)。

## 宣传片

- [ ] 完善并定稿 [v1 宣传片草案](docs/v1-trailer-draft.md)。
