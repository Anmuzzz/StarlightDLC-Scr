package com.isusdlc.mixin.minecraft.client.gui.screen;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.AspectRatio;
import com.isusdlc.systems.modules.modules.visuals.Removals;
import com.isusdlc.systems.modules.modules.visuals.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public abstract class GameRendererMixin {
   @Shadow
   private float zoom;
   @Shadow
   private float zoomX;
   @Shadow
   private float zoomY;
   @Shadow
   private float viewDistance;
   @Shadow
   @Final
   private MinecraftClient client;
   private final Matrix4f cachedPerspectiveMatrix = new Matrix4f();

   @Inject(
      method = {"tiltViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tiltViewWhenHurtHook(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
      Removals removals = elegant.getInstance().getModuleManager().getModule(Removals.class);
      if (removals.isEnabled() && removals.getHurtCam().isSelected()) {
         ci.cancel();
      }
   }

   @Inject(
      method = {"getBasicProjectionMatrix(F)Lorg/joml/Matrix4f;"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void onGetBasicProjectionMatrix(float fovDegrees, CallbackInfoReturnable<Matrix4f> cir) {
      Zoom zoomModule = elegant.getInstance().getModuleManager().getModule(Zoom.class);
      AspectRatio aspectRatio = elegant.getInstance().getModuleManager().getModule(AspectRatio.class);
      if (zoomModule != null && zoomModule.isEnabled()) {
         zoomModule.updateZoom();
         fovDegrees /= zoomModule.getZoomMultiplier();
      }

      if (aspectRatio != null && aspectRatio.isEnabled() && !AspectRatio.isRenderingHands()) {
         MatrixStack stack = new MatrixStack();
         stack.peek().getPositionMatrix().identity();
         if (this.zoom != 1.0F) {
            stack.translate(this.zoomX, -this.zoomY, 0.0F);
            stack.scale(this.zoom, this.zoom, 1.0F);
         }

         float ratio = aspectRatio.getRatio();
         this.cachedPerspectiveMatrix.setPerspective((float)(fovDegrees * (float) (Math.PI / 180.0)), ratio, 0.05F, this.viewDistance * 4.0F);
         stack.peek().getPositionMatrix().mul(this.cachedPerspectiveMatrix);
         cir.setReturnValue(stack.peek().getPositionMatrix());
      } else if (zoomModule != null && zoomModule.isEnabled()) {
         MatrixStack stack = new MatrixStack();
         stack.peek().getPositionMatrix().identity();
         if (this.zoom != 1.0F) {
            stack.translate(this.zoomX, -this.zoomY, 0.0F);
            stack.scale(this.zoom, this.zoom, 1.0F);
         }

         this.cachedPerspectiveMatrix
            .setPerspective(
               (float)(fovDegrees * (float) (Math.PI / 180.0)),
               (float)this.client.getWindow().getFramebufferWidth() / this.client.getWindow().getFramebufferHeight(),
               0.05F,
               this.viewDistance * 4.0F
            );
         stack.peek().getPositionMatrix().mul(this.cachedPerspectiveMatrix);
         cir.setReturnValue(stack.peek().getPositionMatrix());
      }
   }
}
