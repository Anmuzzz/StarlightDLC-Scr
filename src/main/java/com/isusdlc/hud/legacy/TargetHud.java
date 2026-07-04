package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.awt.*;

public class TargetHud extends AbstractHudElement {

    private final StopWatch stopWatch = new StopWatch();
    private LivingEntity lastTarget;

    private float healthAnimation = 0;
    private float displayedHealth = 0;
    private float displayedAbsorption = 0;
    private float animatedHeight = HEIGHT;
    private long lastUpdateTime = System.currentTimeMillis();

    private static final float WIDTH = 118;
    private static final float HEIGHT = 36;
    private static final float COMPACT_HEIGHT = 26;

    public TargetHud() {
        super("TargetHud", 10, 80, (int) WIDTH, (int) HEIGHT, true);
    }

    @Override
    public boolean visible() {
        return !scaleAnimation.isFinished(com.isusdlc.utility.animation.base.Direction.BACKWARDS);
    }

    @Override
    public void tick() {
        if (mc.player == null || mc.world == null) {
            lastTarget = null;
            stopAnimation();
            return;
        }

        LivingEntity auraTarget = Aura.target;
        if (auraTarget != null) {
            lastTarget = auraTarget;
            startAnimation();
            stopWatch.reset();
        } else if (isChat(mc.currentScreen)) {
            lastTarget = mc.player;
            startAnimation();
            stopWatch.reset();
        } else if (stopWatch.finished(1000)) {
            stopAnimation();
        }
    }

    private float lerp(float current, float target, float deltaTime, float speed) {
        float factor = (float) (1.0 - Math.pow(0.001, deltaTime * speed));
        return current + (target - current) * factor;
    }

    private float getHealth(LivingEntity entity) {
        if (entity.isInvisible() && Network.isCopyTime()) {
            return entity.getMaxHealth();
        }
        return entity.getHealth();
    }

    @Override
    public void drawDraggable(DrawContext context, int alpha) {
        if (alpha <= 0) return;
        if (mc.player == null || mc.world == null) return;
        if (lastTarget == null || (lastTarget != mc.player && lastTarget.isRemoved())) return;

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;
        deltaTime = Math.min(deltaTime, 0.1f);

        float x = getX();
        float y = getY();

        boolean fullLayout = hasItems(lastTarget) || lastTarget.getAbsorptionAmount() > 0.1f;
        float targetHeight = fullLayout ? HEIGHT : COMPACT_HEIGHT;
        animatedHeight = lerp(animatedHeight, targetHeight, deltaTime, 10f);

        setWidth((int) WIDTH);
        setHeight((int) Math.ceil(animatedHeight));

        float alphaFactor = alpha / 255.0f;
        float scale = scaleAnimation.getOutput().floatValue();

        float centerX = x + WIDTH / 2f;
        float centerY = y + animatedHeight / 2f;

        context.getMatrices().push();
        context.getMatrices().translate(centerX, centerY, 0);
        context.getMatrices().scale(scale, scale, 1);
        context.getMatrices().translate(-centerX, -centerY, 0);

        drawBackground(x, y, alphaFactor);
        drawFace(x, y, alphaFactor, fullLayout);
        drawContent(context, x, y, alphaFactor, deltaTime, fullLayout);

        context.getMatrices().pop();

        if (fullLayout) {
            drawArmor(context, x, y, alphaFactor);
        }
    }

    private boolean hasItems(LivingEntity entity) {
        if (!(entity instanceof PlayerEntity player)) return false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR || slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
                if (!player.getEquippedStack(slot).isEmpty()) return true;
            }
        }
        return false;
    }

    private void drawBackground(float x, float y, float alpha) {
        int bgAlpha = (int) (230 * alpha);
        Render2D.gradientRect(x, y, WIDTH, animatedHeight,
                new int[]{
                        new Color(20, 20, 20, bgAlpha).getRGB(),
                        new Color(15, 15, 15, bgAlpha).getRGB(),
                        new Color(20, 20, 20, bgAlpha).getRGB(),
                        new Color(15, 15, 15, bgAlpha).getRGB()
                },
                6);
        Render2D.outline(x, y, WIDTH, animatedHeight, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 6);
    }

    private void drawFace(float x, float y, float alpha, boolean fullLayout) {
        EntityRenderer<? super LivingEntity, ?> baseRenderer = mc.getEntityRenderDispatcher().getRenderer(lastTarget);
        if (!(baseRenderer instanceof LivingEntityRenderer<?, ?, ?>)) return;

        @SuppressWarnings("unchecked")
        LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?> renderer =
                (LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>) baseRenderer;

        LivingEntityRenderState state = renderer.getAndUpdateRenderState(lastTarget, lastTickDelta);
        Identifier textureLocation = renderer.getTexture(state);

        float faceSize = fullLayout ? 26 : 18;
        float faceX = x + 5;
        float faceY = y + (animatedHeight - faceSize) / 2f;

        float hurtPercent = lastTarget.hurtTime > 0 ? lastTarget.hurtTime / 10.0f : 0.0f;
        int r = 255;
        int g = (int) (255 * (1.0f - hurtPercent));
        int b = (int) (255 * (1.0f - hurtPercent));
        int color = new Color(r, g, b, (int) (255 * alpha)).getRGB();

        float u0 = 8f / 64f;
        float v0 = 8f / 64f;
        float u1 = 16f / 64f;
        float v1 = 16f / 64f;

        Render2D.texture(textureLocation, faceX, faceY, faceSize, faceSize, u0, v0, u1, v1, color, 0, 4f);

        float hatU0 = 40f / 64f;
        float hatV0 = 8f / 64f;
        float hatU1 = 48f / 64f;
        float hatV1 = 16f / 64f;
        Render2D.texture(textureLocation, faceX, faceY, faceSize, faceSize, hatU0, hatV0, hatU1, hatV1, color, 0f, 4f);
    }

    private void drawArmor(DrawContext context, float x, float y, float alpha) {
        if (!(lastTarget instanceof PlayerEntity player)) return;

        float armorX = (int)x + 36;
        float armorY = (int)y + 6;
        float iconSize = 7;
        float spacing = 2;

        int iconAlpha = (int) (255 * alpha);

        EquipmentSlot[] armorSlots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        for (int i = 0; i < 4; i++) {
            ItemStack stack = player.getEquippedStack(armorSlots[i]);
            if (!stack.isEmpty()) {
                ItemRender.drawItemCenteredWithContext(context, stack, armorX + iconSize / 2f, armorY + iconSize / 2f, 0.5f, alpha);
                armorX += iconSize + spacing;
            }
        }

        ItemStack mainHand = player.getMainHandStack();
        if (!mainHand.isEmpty()) {
            ItemRender.drawItemCenteredWithContext(context, mainHand, armorX + iconSize / 2f, armorY + iconSize / 2f, 0.5f, alpha);
            armorX += iconSize + spacing;
        }

        ItemStack offHand = player.getOffHandStack();
        if (!offHand.isEmpty()) {
            ItemRender.drawItemCenteredWithContext(context, offHand, armorX + iconSize / 2f, armorY + iconSize / 2f, 0.5f, alpha);
        }
    }

    private void drawContent(DrawContext context, float x, float y, float alpha, float deltaTime, boolean fullLayout) {
        if (lastTarget == null) return;

        float contentX = x + (fullLayout ? 36 : 28);
        float nameY = y + (fullLayout ? 17 : 8);

        float hp = getHealth(lastTarget);
        float maxHp = lastTarget.getMaxHealth();
        float absorp = lastTarget.getAbsorptionAmount();

        boolean isInvisible = lastTarget.isInvisible() && Network.isCopyTime();

        displayedHealth = lerp(displayedHealth, hp, deltaTime, 5f);
        displayedAbsorption = absorp;

        String name = lastTarget.getName().getString();
        if (name == null || name.trim().isEmpty()) {
            name = lastTarget.getDisplayName().getString();
        }
        if (name == null || name.trim().isEmpty()) {
            name = "Target";
        }

        String cleanName = name.replaceAll("(?i)§[0-9A-FK-OR]", "");

        int accentColor = new Color(130, 140, 255, (int) (255 * alpha)).getRGB();
        int absorptionColor = new Color(255, 200, 50, (int) (255 * alpha)).getRGB();
        int whiteColor = new Color(255, 255, 255, (int) (255 * alpha)).getRGB();

        float hpX_base = x + WIDTH - 6;

        if (absorp > 0.1f || displayedAbsorption > 0.1f) {
            String hpStr = isInvisible ? "Unknown" : String.format("%.1f", displayedHealth);
            float hpWidth = Fonts.BOLD.getWidth(hpStr, 6f);
            float hpX = hpX_base - hpWidth;
            float hpY = y + 6;

            Fonts.HUDNEW.draw("D", hpX - 9, hpY - 0.5f, 7f, accentColor);
            Fonts.BOLD.draw(hpStr, hpX, hpY + 0.5f, 6f, accentColor);

            String absStr = String.format("%.1f", displayedAbsorption);
            float absWidth = Fonts.BOLD.getWidth(absStr, 6f);
            float absX = hpX_base - absWidth;
            float absY = nameY + (fullLayout ? 0.5f : 0f);

            ItemStack goldenApple = new ItemStack(net.minecraft.item.Items.GOLDEN_APPLE);
            float iconSize = 7f;
            ItemRender.drawItemCenteredWithContext(context, goldenApple, absX - 5.5f, absY + 3.5f, iconSize / 16f, alpha);

            Fonts.BOLD.draw(absStr, absX, absY + 0.5f, 6f, absorptionColor);
        } else {
            String hpStr = isInvisible ? "Unknown" : String.format("%.1f", displayedHealth);
            float hpWidth = Fonts.BOLD.getWidth(hpStr, 6f);
            float hpX = hpX_base - hpWidth;
            float hpY = nameY + (fullLayout ? 0.5f : 0f);

            Fonts.HUDNEW.draw("D", hpX - 9, hpY - 0.5f, 7f, accentColor);
            Fonts.BOLD.draw(hpStr, hpX, nameY + 0.5f, 6f, accentColor);
        }

        float hpWidthSample = Fonts.BOLD.getWidth("20.0", 6f);
        float hpX_Sample = hpX_base - hpWidthSample;
        float maxNameWidth = hpX_Sample - contentX - 10;
        float nameWidth = Fonts.BOLD.getWidth(cleanName, 7f);

        if (nameWidth > maxNameWidth) {
            Scissor.enable(contentX, nameY - 2, maxNameWidth, 12);
            Fonts.BOLD.draw(cleanName, contentX, nameY, 7f, whiteColor);
            Scissor.disable();

            int bgAlpha = (int) (230 * alpha);
            int fadeColor = new Color(15, 15, 15, bgAlpha).getRGB();
            int transparentFadeColor = new Color(15, 15, 15, 0).getRGB();

            Render2D.gradientRect(contentX + maxNameWidth - 15, nameY - 2, 15, 12,
                    new int[]{transparentFadeColor, fadeColor, transparentFadeColor, fadeColor}, 0);
        } else {
            Fonts.BOLD.draw(cleanName, contentX, nameY, 7f, whiteColor);
        }

        float barX = contentX;
        float barY = y + (fullLayout ? 29 : 18);
        float barWidth = WIDTH - (fullLayout ? 36 : 28) - 6;
        float barHeight = 1.5f;

        Render2D.rect(barX, barY, barWidth, barHeight, new Color(30, 30, 35, (int) (180 * alpha)).getRGB(), 1f);

        float targetHealth = isInvisible ? 1.0f : Math.min(1.0f, (hp + absorp) / (maxHp + absorp));
        healthAnimation = lerp(healthAnimation, targetHealth, deltaTime, 4f);

        if (healthAnimation > 0.01f) {
            int color1 = new Color(130, 140, 255, (int) (255 * alpha)).getRGB();
            int color2 = new Color(100, 110, 230, (int) (255 * alpha)).getRGB();

            Render2D.gradientRect(barX, barY, barWidth * Math.min(1.0f, healthAnimation), barHeight,
                    new int[]{color1, color2, color1, color2}, 1f);
        }
    }
}
