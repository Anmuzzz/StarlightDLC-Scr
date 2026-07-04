package com.isusdlc.systems.modules.modules.visuals.cosmetic;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public final class CosmeticAttachmentTransforms {
    private CosmeticAttachmentTransforms() {
    }

    public static void apply(MatrixStack matrices, CosmeticType type, CosmeticModel model) {
        if (type == CosmeticType.PET) {
            float bob = CosmeticManager.getInstance().getAnimator().getPetHoverOffset();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
            matrices.translate(0.45f, 2.15f + bob, 0.12f);
            matrices.scale(0.55f, 0.55f, 0.55f);
            return;
        }
        if (type == CosmeticType.ACCESSORY && model != null) {
            String folder = model.getFolderName().toLowerCase();
            if (folder.contains("witch") || folder.contains("hat")) {
                matrices.translate(0f, 1.72f, 0f);
                matrices.scale(0.52f, 0.52f, 0.52f);
            } else if (folder.contains("wing")) {
                matrices.translate(0f, 0.1f, -0.3f);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180f));
                matrices.scale(0.85f, 0.85f, 0.85f);
            } else if (folder.contains("wheel")) {
                matrices.translate(0f, 0.55f, 0.25f);
                matrices.scale(0.55f, 0.55f, 0.55f);
            } else {
                matrices.translate(0f, 1.3f, 0f);
                matrices.scale(0.5f, 0.5f, 0.5f);
            }
        }
    }

    public static void applyPreview(MatrixStack matrices, CosmeticModel model) {
        if (model == null) return;
        String folder = model.getFolderName().toLowerCase();
        if (model.getType() == CosmeticType.PET) {
            matrices.translate(6f, 22f, 1f);
            matrices.scale(1.8f, 1.8f, 1.8f);
        } else if (model.getType() == CosmeticType.ACCESSORY) {
            if (folder.contains("witch") || folder.contains("hat")) {
                matrices.translate(0f, 20f, 0f);
            } else if (folder.contains("wing")) {
                matrices.translate(0f, 2f, -4f);
                matrices.scale(1.3f, 1.3f, 1.3f);
            } else if (folder.contains("wheel")) {
                matrices.translate(0f, 6f, 3f);
            }
        }
    }
}
