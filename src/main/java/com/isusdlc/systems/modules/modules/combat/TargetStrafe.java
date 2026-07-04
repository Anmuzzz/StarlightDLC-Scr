package com.isusdlc.systems.modules.modules.combat;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.systems.setting.settings.BooleanSetting;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.modules.modules.movement.Speed;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;

@ModuleInfo(
   name = "TargetStrafe",
   category = ModuleCategory.COMBAT,
   desc = "Стрейф вокруг цели"
)
public class TargetStrafe extends BaseModule {

   public final SliderSetting speed = new SliderSetting(this, "Speed")
      .min(0.01F).max(1.2F).step(0.01F).currentValue(0.095F);

   public final ModeSetting mode = new ModeSetting(this, "Mode");
   public final ModeSetting.Value vector = new ModeSetting.Value(this.mode, "Vector").select();
   public final ModeSetting.Value motion = new ModeSetting.Value(this.mode, "Motion/Velocity");

   public final SliderSetting distance = new SliderSetting(this, "Distance")
      .min(0.01F).max(12.0F).step(0.01F).currentValue(7.0F);

   public final SliderSetting hitbox = new SliderSetting(this, "HitBox")
      .min(0.01F).max(50.0F).step(0.01F).currentValue(0.095F);

   public final BooleanSetting predictCheck = new BooleanSetting(this, "Predict").enabled(true);

   public final SliderSetting predict = new SliderSetting(this, "Predict value", () -> !this.predictCheck.isEnabled())
      .min(0.1F).max(4.0F).step(0.1F).currentValue(2.5F);

   public final BooleanSetting predictView = new BooleanSetting(this, "Predict view").enabled(false);

    @Override
    public void onEnable() {
      Speed spd = elegant.getInstance().getModuleManager().getModule(Speed.class);
      if (spd.isEnabled()) {
         spd.setEnabled(false, true);
      }
      super.onEnable();
   }

    @Override
    public void onDisable() {
      if (mc.options.forwardKey.isPressed()) {
         mc.options.forwardKey.setPressed(false);
      }
      super.onDisable();
   }
}
