package top.csituka.magicaland.client.gui.tab.settings;

import net.minecraft.text.Text;
import top.csituka.magicaland.client.config.Config;
import top.csituka.magicaland.client.gui.widget.SettingsList;
import top.csituka.magicaland.client.gui.widget.SettingsTextField;
import top.csituka.magicaland.client.gui.widget.Toggle;
import top.csituka.magicaland.client.network.ClientNetworkHandler;

public class NetworkPage implements SettingsPage {
    @Override
    public void build(SettingsList list, int buttonX, int buttonWidth) {
        Config config = Config.getInstance();
        SettingsTextField serviceUrl = new SettingsTextField(buttonX, 0, buttonWidth, 20,
                Text.translatable("text.magicaland.config.mglskin_url.name"));
        String configuredUrl = config.mglSkinUrl;
        if (configuredUrl == null || configuredUrl.isBlank()) configuredUrl = "http://127.0.0.1:4300";
        serviceUrl.setText(configuredUrl);
        serviceUrl.setMaxLength(200);
        serviceUrl.setChangedListener(value -> {
            config.mglSkinUrl = value.trim();
            Config.save();
        });
        list.addWidget(serviceUrl);
        list.addWidget(new Toggle(buttonX, 0, buttonWidth, 20,
                Text.translatable("text.magicaland.config.broadcast_own_model.name"),
                config.broadcastOwnModel,
                toggle -> {
                    config.broadcastOwnModel = toggle.getState();
                    Config.save();
                    ClientNetworkHandler.setBroadcastOwnModel(config.broadcastOwnModel);
                }));
    }
}
