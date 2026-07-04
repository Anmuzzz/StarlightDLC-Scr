package com.isusdlc.mixin.minecraft.client.render.entity;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.ItemPhysics;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.render.entity.state.ItemStackEntityRenderState;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ItemEntityRenderer.class})
public abstract class ItemEntityRendererMixin {
   @Shadow
   @Final
   private Random random;
   @Unique
   private ItemEntity currentEntity;

   @Unique
   private void renderWithPhysics(ItemEntityRenderState itemEntityRenderState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
      if (!itemEntityRenderState.itemRenderState.isEmpty()) {
         matrices.push();
         float h = itemEntityRenderState.itemRenderState.getTransformation().scale.y();
         ItemPhysics module = elegant.getInstance().getModuleManager().getModuleSafe(ItemPhysics.class);
         boolean moduleEnabled = module != null && module.isEnabled();
         if (!moduleEnabled) {
            float g = MathHelper.sin(itemEntityRenderState.age / 10.0F + itemEntityRenderState.uniqueOffset) * 0.1F + 0.1F;
            matrices.translate(0.0F, g + 0.25F * h, 0.0F);
            float rotation = ItemEntity.getRotation(itemEntityRenderState.age, itemEntityRenderState.uniqueOffset);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rotation));
         } else {
            float rotation = ItemEntity.getRotation(itemEntityRenderState.age, itemEntityRenderState.uniqueOffset);
            boolean isOnGround = this.currentEntity != null && this.currentEntity.isOnGround();
            if (isOnGround) {
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
            } else {
               matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotation * 300.0F));
            }
         }

         this.renderItemStack(matrices, vertexConsumers, light, itemEntityRenderState);
         matrices.pop();
      }
   }

   @Unique
   private void renderItemStack(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStackEntityRenderState state) {
      this.random.setSeed(state.seed);
      int renderedAmount = state.renderedAmount;
      ItemRenderState itemRenderState = state.itemRenderState;
      boolean hasDepth = itemRenderState.hasDepth();
      float scaleX = itemRenderState.getTransformation().scale.x();
      float scaleY = itemRenderState.getTransformation().scale.y();
      float scaleZ = itemRenderState.getTransformation().scale.z();
      if (!hasDepth) {
         float offsetX = -0.0F * (renderedAmount - 1) * 0.5F * scaleX;
         float offsetY = -0.0F * (renderedAmount - 1) * 0.5F * scaleY;
         float offsetZ = -0.09375F * (renderedAmount - 1) * 0.5F * scaleZ;
         matrices.translate(offsetX, offsetY, offsetZ);
      }

      for (int i = 0; i < renderedAmount; i++) {
         matrices.push();
         if (i > 0) {
            if (hasDepth) {
               float offsetX = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float offsetY = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               float offsetZ = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F;
               matrices.translate(offsetX, offsetY, offsetZ);
            } else {
               float offsetX = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               float offsetY = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
               matrices.translate(offsetX, offsetY, 0.0F);
            }
         }

         itemRenderState.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV);
         matrices.pop();
         if (!hasDepth) {
            matrices.translate(0.0F * scaleX, 0.0F * scaleY, 0.09375F * scaleZ);
         }
      }
   }

   @Inject(
      method = {"updateRenderState(Lnet/minecraft/entity/ItemEntity;Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;F)V"},
      at = {@At("HEAD")}
   )
   private void captureEntity(ItemEntity itemEntity, ItemEntityRenderState state, float tickDelta, CallbackInfo ci) {
      this.currentEntity = itemEntity;
   }

   @Inject(
      method = {"render*(Lnet/minecraft/client/render/entity/state/ItemEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void render(ItemEntityRenderState itemEntityRenderState, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
      this.renderWithPhysics(itemEntityRenderState, matrices, vertexConsumers, light, ci);
      ci.cancel();
   }
}
