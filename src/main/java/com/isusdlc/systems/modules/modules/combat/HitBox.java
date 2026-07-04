package com.isusdlc.systems.modules.modules.combat;
import com.isusdlc.systems.setting.settings.SliderSetting;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;

@ModuleInfo(
   name = "HitBox",
   category = ModuleCategory.COMBAT,
   desc = "Увеличивает хит-бокс игроков"
)
public class HitBox extends BaseModule {

   public final SliderSetting size = new SliderSetting(this, "Size")
      .min(0.1F).max(5.5F).step(0.1F).currentValue(0.4F);
}
