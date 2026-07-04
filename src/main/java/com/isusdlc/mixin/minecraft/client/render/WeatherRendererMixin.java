package com.isusdlc.mixin.minecraft.client.render;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.Removals;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.FrameGraphBuilder;
import net.minecraft.client.render.Fog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldRenderer.class})
public abstract class WeatherRendererMixin {
   @Inject(
      method = {"renderWeather(Lnet/minecraft/client/render/FrameGraphBuilder;Lnet/minecraft/util/math/Vec3d;FLnet/minecraft/client/render/Fog;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderWeather(FrameGraphBuilder frameGraphBuilder, Vec3d pos, float tickDelta, Fog fog, CallbackInfo ci) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getWeather().isSelected()) {
         ci.cancel();
      }
   }
}
