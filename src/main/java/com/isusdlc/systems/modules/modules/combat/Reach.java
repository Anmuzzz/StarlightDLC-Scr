package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@ModuleInfo(
   name = "Reach",
   category = ModuleCategory.COMBAT,
   desc = "modules.descriptions.reach"
)
@Environment(EnvType.CLIENT)
public class Reach extends BaseModule {
   public final SliderSetting blocksRange = new SliderSetting(this, "modules.settings.reach.blocks_range")
      .min(0.1F)
      .max(6.0F)
      .step(0.1F)
      .currentValue(3.0F);
   public final SliderSetting entityRange = new SliderSetting(this, "modules.settings.reach.entity_range")
      .min(0.1F)
      .max(6.0F)
      .step(0.1F)
      .currentValue(3.0F);
}
