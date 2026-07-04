package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.notifications.NotificationType;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.utility.sounds.ClientSounds;

@ModuleInfo(
    name = "Spawn Bind",
    category = ModuleCategory.OTHER,
    desc = "Отправляет команду /spawn по нажатию бинда"
)
public class SpawnBind extends BaseModule {
    private final BindSetting spawnKey = new BindSetting(this, "Бинд спавна").key(-1);
    private long lastSpawnMs = 0L;
    private final EventListener<KeyPressEvent> onKeyPress = event -> {
        if (this.isEnabled() && mc.player != null && event.getAction() == 1) {
            if (mc.currentScreen == null && this.spawnKey.isKey(event.getKey())) {
                long now = System.currentTimeMillis();
                if (now - this.lastSpawnMs < 400L) return;
                this.lastSpawnMs = now;
                handleSpawn();
            }
        }
    };

    private void handleSpawn() {
        if (mc.player != null) {
            String command = "/spawn";
            if (mc.player.networkHandler != null) {
                mc.player.networkHandler.sendChatMessage(command);
                elegant.getInstance().getNotificationManager().addNotification(NotificationType.SUCCESS, "Команда отправлена");
                ClientSounds.MODULE.play(1.0F, 1.1F);
            } else {
                ClientSounds.CRITICAL.play(1.0F, 1.0F);
            }
        }
    }

    @Override
    public void onEnable() {
        this.lastSpawnMs = 0L;
    }
}
