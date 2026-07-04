package com.isusdlc.mixin.minecraft.client.option;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.Ambience;
import net.minecraft.text.Text;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SimpleOption.class})
public class SimpleOptionMixin<T> {
   @Shadow
   @Final
   Text text;
   @Shadow
   T value;

   @Inject(
      method = {"getValue()Ljava/lang/Object;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getGammaValue(CallbackInfoReturnable<Double> cir) {
      if (elegant.getInstance().getModuleManager() != null) {
         Ambience ambienceModule = elegant.getInstance().getModuleManager().getModule(Ambience.class);
         if (ambienceModule.isEnabled() && ambienceModule.getBright().isEnabled() && this.text.equals(Text.translatable("options.gamma"))) {
            cir.setReturnValue(1337.0);
         }
      }
   }

   @Inject(
      method = {"setValue(Ljava/lang/Object;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setGammaValue(T value, CallbackInfo ci) {
      if (this.text.equals(Text.translatable("options.gamma"))) {
         this.value = value;
         ci.cancel();
      }
   }
}
