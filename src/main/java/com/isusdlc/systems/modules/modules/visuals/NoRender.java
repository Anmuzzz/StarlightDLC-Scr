package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SelectSetting;

@ModuleInfo(
   name = "No Render",
   category = ModuleCategory.VISUALS,
   desc = "Убирает разные типы на экране"
)
public class NoRender extends BaseModule {
   private final SelectSetting removals = new SelectSetting(this, "Убрать");
   private final SelectSetting.Value hurtCam = new SelectSetting.Value(removals, "Тряска камеры").select();
   private final SelectSetting.Value fireOverlay = new SelectSetting.Value(removals, "Огонь на экране").select();
   private final SelectSetting.Value waterOverlay = new SelectSetting.Value(removals, "Вода на экране").select();
   private final SelectSetting.Value suffocation = new SelectSetting.Value(removals, "Удушье").select();
   private final SelectSetting.Value scoreboard = new SelectSetting.Value(removals, "Скорборд");
   private final SelectSetting.Value badEffects = new SelectSetting.Value(removals, "Плохие эффекты").select();
}
