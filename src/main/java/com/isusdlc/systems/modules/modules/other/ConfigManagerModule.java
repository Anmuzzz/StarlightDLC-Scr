package com.isusdlc.systems.modules.modules.other;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.constructions.configgui.ConfigScreen;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ButtonSetting;

@ModuleInfo(
   name = "Config Manager",
   category = ModuleCategory.OTHER,
   desc = "Управление конфигами через графический интерфейс"
)
public class ConfigManagerModule extends BaseModule {

   private final ButtonSetting openGUI = new ButtonSetting(this, "Open GUI")
      .action(() -> mc.setScreen(new ConfigScreen()));
}
