package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.notifications.NotificationType;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.systems.setting.settings.StringSetting;
import com.isusdlc.utility.sounds.ClientSounds;

@ModuleInfo(
   name = "Coord Invite",
   category = ModuleCategory.OTHER,
   desc = "Отправляет координаты игроку по бинду"
)
public class CoordInvite extends BaseModule {
   private final BindSetting inviteKey = new BindSetting(this, "Бинд приглашения").key(-1);
   private final StringSetting playerName = new StringSetting(this, "Ник игрока").text("");
   private long lastInviteMs = 0L;
   private final EventListener<KeyPressEvent> onKeyPress = event -> {
      if (this.isEnabled() && mc.player != null && event.getAction() == 1) {
         if (mc.currentScreen == null) {
            if (this.inviteKey.isKey(event.getKey())) {
               long now = System.currentTimeMillis();
               if (now - this.lastInviteMs < 400L) {
                  return;
               }

               this.lastInviteMs = now;
               this.handleInvite();
            }
         }
      }
   };

   private void handleInvite() {
      if (mc.player != null) {
         String nick = this.playerName.getText();
         if (nick != null && !nick.trim().isEmpty()) {
            if (nick.trim().length() < 3) {
               elegant.getInstance().getNotificationManager().addNotification(NotificationType.ERROR, "Ник слишком короткий");
               ClientSounds.CRITICAL.play(1.0F, 1.0F);
            } else {
               int x = (int)Math.floor(mc.player.getX());
               int y = (int)Math.floor(mc.player.getY());
               int z = (int)Math.floor(mc.player.getZ());
               String command = String.format("/m %s %d %d %d", nick.trim(), x, y, z);
               if (mc.player.networkHandler != null) {
                  mc.player.networkHandler.sendChatMessage(command);
                  elegant.getInstance().getNotificationManager().addNotification(NotificationType.SUCCESS, "Координаты отправлены");
                  ClientSounds.MODULE.play(1.0F, 1.1F);
               } else {
                  ClientSounds.CRITICAL.play(1.0F, 1.0F);
               }
            }
         } else {
            elegant.getInstance().getNotificationManager().addNotification(NotificationType.ERROR, "Укажите ник игрока");
            ClientSounds.CRITICAL.play(1.0F, 1.0F);
         }
      }
   }

   @Override
   public void onEnable() {
      this.lastInviteMs = 0L;
   }
}
