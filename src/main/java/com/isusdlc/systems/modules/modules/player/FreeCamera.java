package com.isusdlc.systems.modules.modules.player;
import com.isusdlc.systems.event.EventListener;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;

@ModuleInfo(name = "FreeCamera", category = ModuleCategory.PLAYER, desc = "Свободная камера")
public class FreeCamera extends BaseModule {

    private final SliderSetting speed = new SliderSetting(this, "Скорость").min(0.1f).max(3f).step(0.1f).currentValue(1f);

    private final EventListener<ClientPlayerTickEvent> listener = event -> {
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        mc.player.setVelocity(0, 0, 0);
        mc.player.noClip = true;
        mc.player.setNoGravity(true);
    };

    @Override
    public void onEnable() {
        super.onEnable();
        if (mc.player != null) {
            mc.player.setNoGravity(true);
            mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.setNoGravity(false);
            mc.player.noClip = false;
        }
    }
}
