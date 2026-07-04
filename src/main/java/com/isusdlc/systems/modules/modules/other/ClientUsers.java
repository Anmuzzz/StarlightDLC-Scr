package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.systems.setting.settings.StringSetting;
import com.isusdlc.ui.mainmenu.IRCScreen;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.text.Text;

@ModuleInfo(
    name = "ClientUsers",
    category = ModuleCategory.OTHER,
    desc = "IRC чат между пользователями клиента"
)
public class ClientUsers extends BaseModule {

    private final StringSetting serverIP = new StringSetting(this, "Server IP").text("localhost");
    private final SliderSetting serverPort = new SliderSetting(this, "Server Port").min(1).max(65535).step(1).currentValue(20036);
    private final StringSetting nickName = new StringSetting(this, "Nickname").text("User");

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.setScreen(new IRCScreen(
                serverIP.getText(),
                (int) serverPort.getCurrentValue(),
                nickName.getText()
            ));
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof IRCScreen screen) {
            screen.disconnect();
            mc.setScreen(null);
        }
        super.onDisable();
    }
}
