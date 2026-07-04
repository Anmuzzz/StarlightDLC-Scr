package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.InternalAttackEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.game.EntityUtility;
import com.isusdlc.utility.time.Timer;
import net.minecraft.util.Hand;
import net.minecraft.client.gui.screen.DeathScreen;

@ModuleInfo(
   name = "Player Utils",
   category = ModuleCategory.PLAYER,
   desc = "Утилиты для игрока"
)
public class PlayerUtils extends BaseModule {
   private final BooleanSetting antiAfk = new BooleanSetting(this, "Anti AFK", "Не позволяет серверу кикнуть вас, пока вы AFK");
   private final BooleanSetting autoRespawn = new BooleanSetting(this, "Auto Respawn", "Автоматически возрождает при смерти");
   private final ModeSetting antiAFKMode = new ModeSetting(this, "Режимы", () -> !this.antiAfk.isEnabled());
   private final ModeSetting.Value jump = new ModeSetting.Value(this.antiAFKMode, "Прыгать");
   private final ModeSetting.Value swing = new ModeSetting.Value(this.antiAFKMode, "Взмах рукой");
   private final SliderSetting delay = new SliderSetting(this, "Задержка", "Задержка для действий", () -> !this.antiAfk.isEnabled())
      .min(5.0F)
      .max(60.0F)
      .step(5.0F)
      .currentValue(50.0F);
   private final Timer timerAFK = new Timer();
   private boolean activeAFK;
   private final EventListener<InternalAttackEvent> onAttackEvent = event -> {
      if (this.antiAfk.isEnabled()) {
         this.activeAFK = false;
         this.timerAFK.reset();
      }
   };

   @Override
   public void tick() {
      if (this.antiAfk.isEnabled()) {
         if (this.timerAFK.finished((long)(this.delay.getCurrentValue() * 1000.0F))) {
            this.activeAFK = true;
         }

         if (EntityUtility.isPlayerMoving()) {
            this.activeAFK = false;
            this.timerAFK.reset();
         }

         if (this.activeAFK && this.timerAFK.finished((long)(this.delay.getCurrentValue() * 1000.0F))) {
            if (this.jump.isSelected() && mc.player != null) {
               mc.player.jump();
            } else if (this.swing.isSelected() && mc.player != null) {
               mc.player.swingHand(Hand.MAIN_HAND);
            }

            this.timerAFK.reset();
            this.activeAFK = false;
         }
      }

      if (this.autoRespawn.isEnabled() && mc.currentScreen instanceof DeathScreen && mc.player != null) {
         mc.player.requestRespawn();
         mc.setScreen(null);
      }

      super.tick();
   }
}
