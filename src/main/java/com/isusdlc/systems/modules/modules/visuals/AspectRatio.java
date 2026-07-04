package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;

@ModuleInfo(
   name = "Aspect Ratio",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.aspect_ratio"
)
public class AspectRatio extends BaseModule {
   private final SliderSetting ratio = new SliderSetting(this, "modules.settings.aspect_ratio.ratio").step(0.001F).min(0.5F).max(5.0F).currentValue(1.7777F);
   private static final ThreadLocal<Boolean> renderingHands = ThreadLocal.withInitial(() -> false);

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
   }

   public float getRatio() {
      return this.ratio.getCurrentValue();
   }

   public static void setRenderingHands(boolean rendering) {
      renderingHands.set(rendering);
   }

   public static boolean isRenderingHands() {
      return renderingHands.get();
   }
}
