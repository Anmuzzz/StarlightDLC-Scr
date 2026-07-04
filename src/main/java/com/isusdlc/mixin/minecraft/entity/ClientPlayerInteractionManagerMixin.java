package com.isusdlc.mixin.minecraft.entity;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.impl.game.BlockBreakEvent;
import com.isusdlc.systems.event.impl.game.InternalAttackEvent;
import com.isusdlc.systems.event.impl.game.StartBreakBlockEvent;
import com.isusdlc.systems.modules.modules.player.Freelook;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin {
   @Shadow
   @Final
   private MinecraftClient client;

   @Inject(
      method = {"attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void elegant$critPre(PlayerEntity player, Entity target, CallbackInfo ci) {
      if (Freelook.isActive) {
         ci.cancel();
      } else {
         InternalAttackEvent event = new InternalAttackEvent(target);
         elegant.getInstance().getEventManager().triggerEvent(event);
         if (event.isCancelled()) {
            ci.cancel();
         }
      }
   }

   @Inject(
      method = {"breakBlock(Lnet/minecraft/util/math/BlockPos;)Z"},
      at = {@At("RETURN")},
      cancellable = true
   )
   public void breakBlockHook(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
      BlockBreakEvent event = new BlockBreakEvent(pos);
      elegant.getInstance().getEventManager().triggerEvent(event);
      if (event.isCancelled()) {
         cir.setReturnValue(false);
      }
   }

   @Inject(
      method = {"attackBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAttackBlock(BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> info) {
      if (Freelook.isActive) {
         info.setReturnValue(false);
         info.cancel();
      } else {
         StartBreakBlockEvent event = new StartBreakBlockEvent(blockPos);
         elegant.getInstance().getEventManager().triggerEvent(event);
         if (event.isCancelled()) {
            info.cancel();
         }
      }
   }

   @Inject(
      method = {"interactBlock(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void preventInteraction(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
      if (Freelook.isActive) {
         cir.setReturnValue(ActionResult.FAIL);
         cir.cancel();
      } else {
         if (this.client.world != null) {
         }
      }
   }

   @Inject(
      method = {"interactEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onInteractEntity(PlayerEntity player, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      if (Freelook.isActive) {
         cir.setReturnValue(ActionResult.FAIL);
         cir.cancel();
      }
   }

   @Inject(
      method = {"interactEntityAtLocation(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/hit/EntityHitResult;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onInteractEntityAtLocation(PlayerEntity player, Entity entity, EntityHitResult hitResult, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      if (Freelook.isActive) {
         cir.setReturnValue(ActionResult.FAIL);
         cir.cancel();
      }
   }

   private boolean isLookingAtBlock(BlockPos blockPos) {
      if (this.client.player == null) {
         return false;
      } else {
         Vec3d playerPos = this.client.player.getEyePos();
         Vec3d blockCenter = Vec3d.ofCenter(blockPos);
         Vec3d direction = blockCenter.subtract(playerPos).normalize();
         Vec3d lookVec = Freelook.getCurrentLookVector();
         double dot = lookVec.dotProduct(direction);
         double angle = Math.acos(MathHelper.clamp(dot, -1.0, 1.0)) * (180.0 / Math.PI);
         return angle < 90.0;
      }
   }

   private boolean isLookingAtEntity(Entity entity) {
      if (this.client.player == null) {
         return false;
      } else {
         Vec3d playerPos = this.client.player.getEyePos();
         Vec3d entityPos = entity.getPos().add(0.0, entity.getHeight() / 2.0F, 0.0);
         Vec3d direction = entityPos.subtract(playerPos).normalize();
         Vec3d lookVec = Freelook.getCurrentLookVector();
         double dot = lookVec.dotProduct(direction);
         double angle = Math.acos(MathHelper.clamp(dot, -1.0, 1.0)) * (180.0 / Math.PI);
         return angle < 90.0;
      }
   }
}
