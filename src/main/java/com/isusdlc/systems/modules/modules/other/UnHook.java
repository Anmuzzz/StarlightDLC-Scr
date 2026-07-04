package com.isusdlc.systems.modules.modules.other;
import com.isusdlc.systems.setting.settings.BindSetting;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BindSetting;

@ModuleInfo(
   name = "UnHook",
   category = ModuleCategory.OTHER,
   desc = "Отключение чита для прохождения проверки"
)
public class UnHook extends BaseModule {
   private final BindSetting unHookKey = new BindSetting(this, "Кнопка возврата");

    @Override
    public void onEnable() {
      for (Module module : elegant.getInstance().getModuleManager().getModules()) {
         if (module.isEnabled() && module != this) {
            module.setEnabled(false, true);
         }
      }
      toggle();
   }
}
