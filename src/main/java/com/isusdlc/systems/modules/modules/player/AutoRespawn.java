package com.isusdlc.systems.modules.modules.player;
import com.isusdlc.systems.event.EventListener;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import net.minecraft.client.gui.screen.DeathScreen;

@ModuleInfo(name = "AutoRespawn", category = ModuleCategory.PLAYER, desc = "Автоматически респавнит вас при смерти")
public class AutoRespawn extends BaseModule {

    private final BooleanSetting autohome = new BooleanSetting(this, "Автоматически телепортироваться домой").enabled(true);

    private final EventListener<ClientPlayerTickEvent> listener = event -> {
        if (mc.player == null) return;

        if (mc.currentScreen instanceof DeathScreen) {
            mc.player.requestRespawn();
            mc.setScreen(null);
            if (autohome.isEnabled()) {
                mc.player.networkHandler.sendCommand("home home");
            }
        }
    };
}
