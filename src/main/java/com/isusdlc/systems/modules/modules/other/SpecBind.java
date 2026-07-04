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
   name = "Spec Bind",
   category = ModuleCategory.OTHER,
   desc = "Отправляет команду !Спек с вашим ником в чат по нажатию бинда"
)
public class SpecBind extends BaseModule {
   private final BindSetting specKey = new BindSetting(this, "Бинд спека").key(-1);
   private long lastSpecMs = 0L;
   private final EventListener<KeyPressEvent> onKeyPress = event -> {
      if (this.isEnabled() && mc.player != null && event.getAction() == 1) {
         if (mc.currentScreen == null) {
            if (this.specKey.isKey(event.getKey())) {
               long now = System.currentTimeMillis();
               if (now - this.lastSpecMs < 400L) {
                  return;
               }

               this.lastSpecMs = now;
               this.handleSpec();
            }
         }
      }
   };

   private void handleSpec() {
      if (mc.player != null) {
         String playerName = mc.player.getName().getString();
         String command = "!Спек " + playerName;
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
      this.lastSpecMs = 0L;
   }
}
