package com.isusdlc.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.isusdlc.elegant;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.rotations.MoveCorrection;
import com.isusdlc.utility.rotations.RotationHandler;
import com.isusdlc.utility.rotations.RotationTask;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({Entity.class})
public class EntityMixin implements IMinecraft {
   @Shadow
   private Box boundingBox;

   @ModifyExpressionValue(
      method = {"move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;isControlledByPlayer()Z"
      )}
   )
   public boolean fixFalldistanceValue(boolean original) {
      return (Object)this == mc.player ? false : original;
   }

   @Redirect(
      method = {"updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/Entity;getYaw()F"
      )
   )
   public float movementCorrection(Entity instance) {
      RotationHandler rotationHandler = elegant.INSTANCE.getRotationHandler();
      RotationTask currentTask = rotationHandler.getCurrentTask();
      return currentTask != null && currentTask.getMoveCorrection() != MoveCorrection.NONE && instance instanceof ClientPlayerEntity
         ? rotationHandler.getCurrentRotation().getYaw()
         : instance.getYaw();
   }
}
