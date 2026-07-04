package com.isusdlc.mixin.minecraft.client.render;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.render.Utils;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.ObjectAllocator;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WorldRenderer.class})
public class WorldRendererMixin implements IMinecraft {
   @Inject(
      method = {"render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"},
      at = {@At("RETURN")}
   )
   private void render(
      ObjectAllocator allocator,
      RenderTickCounter tickCounter,
      boolean renderBlockOutline,
      Camera camera,
      GameRenderer gameRenderer,
      Matrix4f positionMatrix,
      Matrix4f projectionMatrix,
      CallbackInfo ci
   ) {
      Utils.onRender(positionMatrix, projectionMatrix);
      Profilers.get().swap(elegant.MOD_ID + "_renderWorld");
      MatrixStack matrices = new MatrixStack();
      matrices.multiplyPositionMatrix(positionMatrix);
      elegant.getInstance()
         .getEventManager()
         .triggerEvent(new Render3DEvent(matrices, positionMatrix, projectionMatrix, camera, tickCounter.getTickDelta(false)));
   }
}
