package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.HandRenderEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.constructions.viewmodel.ViewModelTransformations;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "View Model",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.view_model"
)
public class ViewModel extends BaseModule {
   private final SliderSetting mainTranslateX = new SliderSetting(this, "modules.settings.view_model.main_translate_x")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting mainTranslateY = new SliderSetting(this, "modules.settings.view_model.main_translate_y")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting mainTranslateZ = new SliderSetting(this, "modules.settings.view_model.main_translate_z")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting mainRotateX = new SliderSetting(this, "modules.settings.view_model.main_rotate_x")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final SliderSetting mainRotateY = new SliderSetting(this, "modules.settings.view_model.main_rotate_y")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final SliderSetting mainRotateZ = new SliderSetting(this, "modules.settings.view_model.main_rotate_z")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final SliderSetting offTranslateX = new SliderSetting(this, "modules.settings.view_model.off_translate_x")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting offTranslateY = new SliderSetting(this, "modules.settings.view_model.off_translate_y")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting offTranslateZ = new SliderSetting(this, "modules.settings.view_model.off_translate_z")
      .min(-2.0F)
      .max(2.0F)
      .currentValue(0.0F)
      .step(0.05F);
   private final SliderSetting offRotateX = new SliderSetting(this, "modules.settings.view_model.off_rotate_x")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final SliderSetting offRotateY = new SliderSetting(this, "modules.settings.view_model.off_rotate_y")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final SliderSetting offRotateZ = new SliderSetting(this, "modules.settings.view_model.off_rotate_z")
      .min(-180.0F)
      .max(180.0F)
      .currentValue(0.0F)
      .step(1.0F);
   private final EventListener<HandRenderEvent> onHandRender = event -> {
      if (this.isEnabled()) {
         boolean isMain = event.getHand() == Hand.MAIN_HAND;
         float translateX = isMain ? this.mainTranslateX.getCurrentValue() : this.offTranslateX.getCurrentValue();
         float translateY = isMain ? this.mainTranslateY.getCurrentValue() : this.offTranslateY.getCurrentValue();
         float translateZ = isMain ? this.mainTranslateZ.getCurrentValue() : this.offTranslateZ.getCurrentValue();
         float rotateX = isMain ? this.mainRotateX.getCurrentValue() : this.offRotateX.getCurrentValue();
         float rotateY = isMain ? this.mainRotateY.getCurrentValue() : this.offRotateY.getCurrentValue();
         float rotateZ = isMain ? this.mainRotateZ.getCurrentValue() : this.offRotateZ.getCurrentValue();
         boolean hasTransformations = translateX != 0.0F || translateY != 0.0F || translateZ != 0.0F || rotateX != 0.0F || rotateY != 0.0F || rotateZ != 0.0F;
         if (hasTransformations) {
            ViewModelTransformations trans = new ViewModelTransformations(translateX, translateY, translateZ, rotateX, rotateY, rotateZ);
            event.setViewModelTransformations(trans);
         }
      }
   };
}
