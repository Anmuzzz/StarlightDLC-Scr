package com.isusdlc.systems.modules.modules.movement;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.systems.setting.settings.BooleanSetting;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.game.EntityUtility;

@ModuleInfo(
   name = "Timer",
   category = ModuleCategory.MOVEMENT,
   desc = "Ускорение игры"
)
public class Timer extends BaseModule {
   private final SliderSetting timerAmount = new SliderSetting(this, "Speed")
      .min(0.0F).max(10.0F).step(0.01F).currentValue(2.0F);

   private final BooleanSetting smart = new BooleanSetting(this, "Smart").enable();

   private final SliderSetting ticks = new SliderSetting(this, "Reduction speed", () -> this.smart.isEnabled())
      .min(0.15F).max(5.0F).step(0.1F).currentValue(3.8F);

   private static final float MAX_VIOLATION = 100.0F;
   private float violation = 0.0F;
   private boolean isCooldown = false;

   @Override
   public void onEnable() {
      violation = 0.0F;
      isCooldown = false;
   }

   @Override
   public void onDisable() {
      EntityUtility.resetTimer();
      violation = 0.0F;
      isCooldown = false;
   }

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (!this.smart.isEnabled()) {
         EntityUtility.setTimer(this.timerAmount.getCurrentValue());
         return;
      }

      if (isCooldown) {
         violation -= this.ticks.getCurrentValue();
         EntityUtility.setTimer(1.0F);
         if (violation <= 0) {
            violation = 0;
            isCooldown = false;
         }
      } else {
         EntityUtility.setTimer(this.timerAmount.getCurrentValue());
         violation += this.ticks.getCurrentValue();
         if (violation >= MAX_VIOLATION) {
            violation = MAX_VIOLATION;
            isCooldown = true;
         }
      }
   };
}
