package top.csituka.magicaland.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import top.csituka.magicaland.client.config.ModelConfig;
import top.csituka.magicaland.client.config.ModelManager;
import top.csituka.magicaland.client.gui.widget.CustomButton;
import top.csituka.magicaland.client.network.MglSkinClient;

import java.util.List;

public final class MglSkinScreen extends Screen {
    private final Screen parent;
    private List<MglSkinClient.RemoteSkin> skins = List.of();
    private String status = "";
    private boolean loading;

    public MglSkinScreen(Screen parent) {
        super(text("title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int width = Math.min(360, this.width - 32);
        int left = (this.width - width) / 2;
        addDrawableChild(new CustomButton(left, 10, 84, 20,
                text("refresh"), false, button -> refresh()));
        addDrawableChild(new CustomButton(left + width - 84, 10, 84, 20,
                text("back"), false, button -> close()));
        int actionY = height - 34;
        addDrawableChild(new CustomButton(left, actionY, 112, 20,
                MglSkinClient.isLoggedIn() ? text("logout") : text("login"), false,
                button -> { if (MglSkinClient.isLoggedIn()) logout(); else login(); }));
        CustomButton upload = new CustomButton(left + width - 112, actionY, 112, 20,
                text("upload"), false, button -> upload());
        upload.active = MglSkinClient.isLoggedIn() && ModelManager.getActiveModel() != null;
        addDrawableChild(upload);
        int y = 70;
        for (int i = 0; i < skins.size() && i < 10; i++) {
            MglSkinClient.RemoteSkin skin = skins.get(i);
            String label = skin.name() + (skin.username().isBlank() ? "" : " · " + skin.username());
            addDrawableChild(new CustomButton(left, y, width, 20, Text.literal(label), false,
                    button -> importSkin(skin)));
            y += 23;
        }
        if (skins.isEmpty() && !loading) refresh();
    }

    private void refresh() {
        if (loading) return;
        loading = true;
        status = "";
        MglSkinClient.fetchSkins(result -> {
            skins = List.copyOf(result);
            loading = false;
            clearChildren();
            init();
        }, error -> {
            loading = false;
            status = error;
        });
    }

    private void login() {
        status = "正在打开浏览器...";
        MglSkinClient.beginLogin(username -> {
            status = "已登录：" + username;
            clearChildren();
            init();
        }, error -> status = error);
    }

    private void logout() {
        var config = top.csituka.magicaland.client.config.Config.getInstance();
        config.mglSkinToken = "";
        config.mglSkinUsername = "";
        top.csituka.magicaland.client.config.Config.save();
        clearChildren();
        init();
    }

    private void upload() {
        ModelConfig model = ModelManager.getAppliedModel();
        if (model == null) return;
        status = "正在上传...";
        MglSkinClient.upload(model, name -> status = "上传成功：" + name, error -> status = error);
    }

    private void importSkin(MglSkinClient.RemoteSkin skin) {
        String base = skin.name().isBlank() ? "mglskin-" + skin.id() : skin.name();
        base = base.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
        if (base.isBlank()) base = "mglskin-" + skin.id();
        if (base.length() > 24) base = base.substring(0, 24).trim();
        String name = base;
        int suffix = 2;
        while (isModelNameTaken(name))
            name = base + " " + suffix++;
        if (ModelManager.importModel(name, skin.data())) status = "已导入：" + name;
        else status = "导入失败，请检查预设数据";
    }

    private static boolean isModelNameTaken(String name) {
        for (String existing : ModelManager.getAvailableModels())
            if (existing.equalsIgnoreCase(name)) return true;
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 42, 0xFFFFFFFF);
        if (loading) {
            context.drawCenteredTextWithShadow(textRenderer, text("loading"), width / 2, 64, 0xFFAAAAAA);
        } else if (skins.isEmpty()) {
            context.drawCenteredTextWithShadow(textRenderer, text("empty"), width / 2, 64, 0xFFAAAAAA);
        }
        int contentWidth = Math.min(360, width - 32);
        if (!status.isBlank()) {
            var lines = textRenderer.wrapLines(Text.literal(status), contentWidth);
            int statusY = height - 58 - lines.size() * textRenderer.fontHeight;
            for (int i = 0; i < lines.size(); i++)
                context.drawCenteredTextWithShadow(textRenderer, lines.get(i), width / 2,
                        statusY + i * textRenderer.fontHeight, 0xFFCCCCCC);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public void close() {
        client.setScreen(parent);
    }
    private static Text text(String key) { return Text.translatable("text.magicaland.mglskin." + key); }
}
