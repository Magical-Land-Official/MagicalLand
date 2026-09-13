# 手持物柔边魔法光

## 实现

- 原物品只画一次，同时记录实际提交的四边形、法线和纹理 UV。模型覆写、方块、扁平道具及特殊 renderer 的有纹理四边形都沿用原形状；附魔层不重复画。
- 沿物品表面生成六层逐渐变淡的光晕。2026-09-09 将离表面的扩张距离乘以 1.65，物品本体、层数、透明度与 UV 不变。shader 读取原纹理 alpha，保留花、工具等模型的透明背景与内部空洞。
- 和角部复用 `horn_aura` shader：`UV1.x` 是循环时间，`UV1.y=2` 表示物品剪影，`UV2.y` 保存归一化物品高度，因此流动不需要滚动图集 UV。角部与星星原分支保持不变。
- 每 3–4 秒出现 2–4 颗小星星，由 `MagicSparkles` 控制数量与寿命，尺寸随道具和预览比例缩放。

## 绘制顺序

世界物品与角部光效一起排队：普通模式在云层与天气之后的 `LAST` 阶段绘制，使用主目标且只写颜色；Fabulous 在 `AFTER_TRANSLUCENT` 阶段使用 item/entity 目标，并写入透明合成所需深度。两者均保留前景深度测试。

第一人称必须在两只手都画完后再提交光晕：外层 `HeldItemRenderer.renderItem(float, MatrixStack, Immediate, ClientPlayerEntity, int)` 开头调用 `GlowingItem.beginFirstPersonPass()`，结束调用 `GlowingItem.endFirstPersonPass(buffers)`。这次队列独立于世界渲染阶段，保留第一人称投影，不把光晕带入下一帧。

捏脸道具预览调用：

```java
GlowingItem.renderPreviewWithGlow(renderer, stack, mode, matrices,
        buffers, world, light, seed, glowColor);
```

接口自行画原物品、提交对应缓冲，然后画光晕。调用前可设置预览光照颜色；魔法光仍保持自身发光颜色。

## 边界与检查

每个物品最多记录 8192 个光晕顶点，原物品完整绘制。纹理无法解析、第三方图层格式不受支持，或地图等绕过普通模型的绘制路径，均保留原样。

几何与 GPU 测试覆盖左右手、GUI 缩放、UV 保持、透明角与内部孔洞、局部流动和前景遮挡，并保留角部及 Fabulous 排序测试。
