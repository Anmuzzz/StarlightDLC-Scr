package com.isusdlc.mixin.minecraft.client.render;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.Removals;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.block.BlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleManager.class})
public abstract class ParticleManagerMixin {
   @Inject(
      method = {"addBlockBreakParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddBlockBreakParticles(BlockPos blockPos, BlockState state, CallbackInfo info) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getBreakParticles().isSelected()) {
         info.cancel();
      }
   }

   @Inject(
      method = {"addBlockBreakingParticles(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddBlockBreakingParticles(BlockPos blockPos, Direction direction, CallbackInfo info) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getBreakParticles().isSelected()) {
         info.cancel();
      }
   }

   @Inject(
      method = {"addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAddParticle(
      ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir
   ) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getWeather().isSelected() && parameters.getType() == ParticleTypes.RAIN) {
         cir.cancel();
      }
   }
}
