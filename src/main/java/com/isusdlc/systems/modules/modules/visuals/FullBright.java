package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

@ModuleInfo(
   name = "Full Bright",
   category = ModuleCategory.VISUALS,
   desc = "Освещает местность"
)
public class FullBright extends BaseModule {
   private final StatusEffectInstance nightVision = new StatusEffectInstance(
      StatusEffects.NIGHT_VISION,
      -1,
      255,
      false,
      false,
      true
   );

   @Override
   public void tick() {
      if (mc.player != null) {
         mc.player.addStatusEffect(nightVision, mc.player);
      }
   }

   @Override
   public void onDisable() {
      if (mc.player != null) {
         mc.player.removeStatusEffect(nightVision.getEffectType());
      }
      super.onDisable();
   }
}
