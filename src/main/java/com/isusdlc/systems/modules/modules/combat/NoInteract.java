package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;

@ModuleInfo(
   name = "No Interact",
   category = ModuleCategory.COMBAT,
   desc = "Отключает взаимодействие с интерактивными блоками"
)
public class NoInteract extends BaseModule {
}
