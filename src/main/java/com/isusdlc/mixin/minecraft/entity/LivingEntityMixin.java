package com.isusdlc.mixin.minecraft.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.isusdlc.elegant;
import com.isusdlc.systems.event.impl.game.EntityDeathEvent;
import com.isusdlc.systems.event.impl.game.EntityJumpEvent;
import com.isusdlc.systems.modules.modules.visuals.SwingAnimation;
import com.isusdlc.utility.rotations.MoveCorrection;
import com.isusdlc.utility.rotations.RotationHandler;
import com.isusdlc.utility.rotations.RotationTask;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity.RemovalReason;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class})
public abstract class LivingEntityMixin {
   @Shadow
   private int jumpingCooldown;

   @Shadow
   public abstract void remove(RemovalReason var1);

   @Shadow
   public abstract ItemStack getMainHandStack();

   @Shadow
   public abstract ItemStack getOffHandStack();

   @Shadow
   public abstract boolean isUsingItem();

   @ModifyReturnValue(
      method = {"getHandSwingDuration()I"},
      at = {@At("RETURN")}
   )
   public int replaceSwingSpeed(int original) {
      SwingAnimation swingAnimationModule = elegant.getInstance().getModuleManager().getModuleSafe(SwingAnimation.class);
      return swingAnimationModule != null && swingAnimationModule.isEnabled() && swingAnimationModule.shouldApplyAnimation(this.getMainHandStack())
         ? (int)(original * elegant.getInstance().getSwingManager().getSpeed().getCurrentValue())
         : original;
   }

   @Inject(
      method = {"jump()V"},
      at = {@At("HEAD")},
       cancellable = true
    )
    public void triggerJumpEvent(CallbackInfo ci) {
      LivingEntity livingEntity = (LivingEntity)(Object)this;
      EntityJumpEvent event = new EntityJumpEvent(livingEntity);
      elegant.getInstance().getEventManager().triggerEvent(event);
      if (event.isCancelled()) {
         ci.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"jump()V"},
      at = {@At(
         value = "NEW",
         target = "(DDD)Lnet/minecraft/util/math/Vec3d;"
      )}
   )
   public Vec3d movementCorrection(Vec3d original) {
      RotationHandler rotationHandler = elegant.INSTANCE.getRotationHandler();
      RotationTask currentTask = rotationHandler.getCurrentTask();
      if ((Object)this != MinecraftClient.getInstance().player) {
         return original;
      } else if (currentTask != null && currentTask.getMoveCorrection() != MoveCorrection.NONE) {
         float yaw = rotationHandler.getCurrentRotation().getYaw() * (float) (Math.PI / 180.0);
         return new Vec3d(-MathHelper.sin(yaw) * 0.2F, 0.0, MathHelper.cos(yaw) * 0.2F);
      } else {
         return original;
      }
   }

   @Inject(
      method = {"onDeath(Lnet/minecraft/entity/damage/DamageSource;)V"},
       at = {@At("TAIL")}
   )
   public void triggerEntityDeathEvent(DamageSource damageSource, CallbackInfo ci) {
      LivingEntity entity = (LivingEntity)(Object)this;
      elegant.getInstance().getEventManager().triggerEvent(new EntityDeathEvent(entity, damageSource));
   }

   @Inject(
      method = {"tick()V"},
      at = {@At("HEAD")}
   )
   private void preventItemInterruptionInTick(CallbackInfo ci) {
   }

   @Redirect(
      method = {"calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getPitch()F"
      )
   )
   private float redirectGetPitch(LivingEntity instance) {
      RotationHandler rotationHandler = elegant.getInstance().getRotationHandler();
      return rotationHandler.isIdling() ? instance.getPitch() : rotationHandler.getCurrentRotation().getPitch();
   }

   @Redirect(
      method = {"calcGlidingVelocity(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/LivingEntity;getRotationVector()Lnet/minecraft/util/math/Vec3d;"
      )
   )
   private Vec3d redirectGetRotationVector(LivingEntity instance) {
      RotationHandler rotationHandler = elegant.getInstance().getRotationHandler();
      return rotationHandler.isIdling() ? instance.getRotationVector() : rotationHandler.getCurrentRotation().getRotationVector();
   }
}
