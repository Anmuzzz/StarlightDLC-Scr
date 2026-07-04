package com.isusdlc.mixin.minecraft.entity;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.impl.game.FireworkEvent;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.rotations.Rotation;
import com.isusdlc.utility.rotations.RotationHandler;
import com.isusdlc.utility.rotations.RotationState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({FireworkRocketEntity.class})
public abstract class FireworkRocketEntityMixin implements IMinecraft {
   @Redirect(
      method = {"tick()V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"
      )
   )
   private void redirectSetVelocity(LivingEntity shooter, Vec3d velocity) {
      FireworkRocketEntity rocketEntity = (FireworkRocketEntity)(Object)this;
      FireworkEvent event = new FireworkEvent(shooter, velocity, rocketEntity);
      elegant.getInstance().getEventManager().triggerEvent(event);
      shooter.setVelocity(event.getVelocity());
   }

   @Redirect(
      method = {"tick()V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )
   )
   private Vec3d redirectGetRotationVector(LivingEntity instance) {
      if (instance == mc.player) {
         RotationHandler rotationHandler = elegant.getInstance().getRotationHandler();
         if (rotationHandler != null && rotationHandler.getState() != RotationState.IDLE) {
            Rotation currentRotation = rotationHandler.getCurrentRotation();
            return Vec3d.fromPolar(currentRotation.getPitch(), currentRotation.getYaw());
         }
      }

      return instance.getRotationVector();
   }
}
