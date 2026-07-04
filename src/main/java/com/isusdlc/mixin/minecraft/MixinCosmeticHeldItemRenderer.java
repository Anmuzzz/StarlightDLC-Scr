package com.isusdlc.mixin.minecraft;

import com.isusdlc.systems.modules.modules.visuals.cosmetic.CosmeticHeldItemRenderer;
import com.isusdlc.systems.modules.modules.visuals.cosmetic.CosmeticModule;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public class MixinCosmeticHeldItemRenderer {

    @Inject(method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true)
    private void isusdlc$onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch,
                                                  Hand hand, float swingProgress, ItemStack item,
                                                  float equipProgress, MatrixStack matrices,
                                                  VertexConsumerProvider vertexConsumers, int light,
                                                  CallbackInfo ci) {
        if (!CosmeticModule.getInstance().isEnabled()) return;
        if (CosmeticHeldItemRenderer.tryRenderHeldAccessory(player, tickDelta, pitch, hand, swingProgress,
                item, equipProgress, matrices, vertexConsumers, light)) {
            ci.cancel();
        }
    }
}
