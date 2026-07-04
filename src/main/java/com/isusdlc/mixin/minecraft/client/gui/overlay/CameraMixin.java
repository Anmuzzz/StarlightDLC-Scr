package com.isusdlc.mixin.minecraft.client.gui.overlay;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.player.Freelook;
import com.isusdlc.systems.modules.modules.visuals.Removals;
import net.minecraft.client.render.Camera;
import net.minecraft.block.enums.CameraSubmersionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class CameraMixin {
   @Inject(
      method = {"getSubmersionType()Lnet/minecraft/block/enums/CameraSubmersionType;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getSubmergedFluidState(CallbackInfoReturnable<CameraSubmersionType> ci) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getWater().isSelected()) {
         ci.setReturnValue(CameraSubmersionType.NONE);
      }
   }

   @ModifyArgs(
      method = {"update(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;ZZF)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"
      )
   )
   private void modifyRotation(Args args) {
      if (Freelook.isActive) {
         args.set(0, Freelook.x);
         args.set(1, Freelook.y);
      }
   }
}
