package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;

public final class CosmeticHandItemsRenderer {
    private static final MinecraftClient MC = MinecraftClient.getInstance();
    private static final float S = 1.8f / 24f;

    private CosmeticHandItemsRenderer() {
    }

    public static void render(PlayerEntity player, CosmeticModel model, MatrixStack matrices,
                              VertexConsumerProvider consumers, int light, float partialTicks) {
        if (player == null || model == null || model.getType() != CosmeticType.MODEL) return;

        ItemRenderer itemRenderer = MC.getItemRenderer();
        if (itemRenderer == null) return;

        renderHand(player, model, matrices, consumers, light, itemRenderer,
                player.getMainHandStack(), Hand.MAIN_HAND, true, partialTicks);
        renderHand(player, model, matrices, consumers, light, itemRenderer,
                player.getOffHandStack(), Hand.OFF_HAND, false, partialTicks);
    }

    private static void renderHand(PlayerEntity player, CosmeticModel model, MatrixStack matrices,
                                   VertexConsumerProvider consumers, int light, ItemRenderer itemRenderer,
                                   ItemStack stack, Hand hand, boolean right, float partialTicks) {
        if (stack.isEmpty()) return;

        CosmeticBone pivot = findItemPivot(model, right);
        if (pivot == null) return;

        matrices.push();
        matrices.scale(S, S, S);

        float bodyYaw = net.minecraft.util.math.MathHelper.lerp(
                partialTicks, player.prevBodyYaw, player.bodyYaw);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f - bodyYaw));

        applyBoneChain(matrices, model, pivot);

        matrices.scale(0.55f, 0.55f, 0.55f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180f));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(right ? -90f : 90f));

        int seed = hand == Hand.MAIN_HAND ? 0 : 1;
        itemRenderer.renderItem(
                player,
                stack,
                ModelTransformationMode.THIRD_PERSON_RIGHT_HAND,
                right,
                matrices,
                consumers,
                player.getWorld(),
                light,
                OverlayTexture.DEFAULT_UV,
                player.getId() + seed
        );

        matrices.pop();
    }

    private static void applyBoneChain(MatrixStack matrices, CosmeticModel model, CosmeticBone target) {
        java.util.List<CosmeticBone> chain = new java.util.ArrayList<>();
        if (!findChain(model.getRenderRoots(), target, chain)) {
            findChain(model.getRootBones(), target, chain);
        }
        for (CosmeticBone bone : chain) {
            if (model.shouldSkipBone(bone)) continue;
            float px = bone.getPivot().x;
            float py = bone.getPivot().y;
            float pz = bone.getPivot().z;

            float apx = bone.getAnimPosition().x;
            float apy = bone.getAnimPosition().y;
            float apz = bone.getAnimPosition().z;
            if (apx != 0f || apy != 0f || apz != 0f) {
                matrices.translate(apx, apy, apz);
            }

            float rx = bone.getDefaultRotation().x + bone.getAnimRotation().x;
            float ry = bone.getDefaultRotation().y + bone.getAnimRotation().y;
            float rz = bone.getDefaultRotation().z + bone.getAnimRotation().z;
            if (rx != 0f || ry != 0f || rz != 0f) {
                matrices.translate(px, py, pz);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rx));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(ry));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rz));
                matrices.translate(-px, -py, -pz);
            }
        }
    }

    private static boolean findChain(java.util.List<CosmeticBone> roots, CosmeticBone target,
                                     java.util.List<CosmeticBone> chain) {
        for (CosmeticBone root : roots) {
            chain.clear();
            if (findChain(root, target, chain)) return true;
        }
        return false;
    }

    private static boolean findChain(CosmeticBone bone, CosmeticBone target, java.util.List<CosmeticBone> chain) {
        chain.add(bone);
        if (bone == target) return true;
        for (CosmeticBone child : bone.getChildren()) {
            if (findChain(child, target, chain)) return true;
        }
        chain.remove(chain.size() - 1);
        return false;
    }

    private static CosmeticBone findItemPivot(CosmeticModel model, boolean right) {
        String[] names = right
                ? new String[]{"rightitempivot", "right_item_pivot", "righthanditem"}
                : new String[]{"leftitempivot", "left_item_pivot", "lefthanditem"};
        for (String name : names) {
            CosmeticBone bone = model.getBone(name);
            if (bone != null) return bone;
        }
        for (CosmeticBone bone : model.getBoneMap().values()) {
            String lower = bone.getName().toLowerCase();
            if (!lower.contains("itempivot") && !lower.contains("item_pivot")) continue;
            if (right && (lower.contains("right") || lower.contains("_r"))) return bone;
            if (!right && (lower.contains("left") || lower.contains("_l"))) return bone;
        }
        return null;
    }
}
