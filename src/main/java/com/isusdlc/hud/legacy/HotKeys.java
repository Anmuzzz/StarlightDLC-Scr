package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import com.isusdlc.utility.animation.base.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HotKeys extends AbstractHudElement {

    private List<ModuleStructure> keysList = new ArrayList<>();
    private long lastKeyChange = 0;
    private String currentRandomKey = "NONE";

    private float animatedWidth = 85;
    private float animatedHeight = 22;
    private long lastUpdateTime = System.currentTimeMillis();

    private static final float ANIMATION_SPEED = 8.0f;
    private static final float HEADER_HEIGHT = 22f;
    private static final float ROW_HEIGHT = 16f;

    public HotKeys() {
        super("HotKeys", 300, 40, 85, 22, true);
        stopAnimation();
    }

    @Override
    public boolean visible() {
        return !scaleAnimation.isFinished(Direction.BACKWARDS);
    }

    @Override
    public void tick() {
        if (Initialization.getInstance() == null ||
                Initialization.getInstance().getManager() == null ||
                Initialization.getInstance().getManager().getModuleProvider() == null) {
            return;
        }

        keysList = Initialization.getInstance().getManager().getModuleProvider().getModuleStructures().stream()
                .filter(module -> module.isState()
                        && module.getKey() != GLFW.GLFW_KEY_UNKNOWN)
                .toList();

        boolean hasActiveKeys = !keysList.isEmpty();
        boolean inChat = isChat(mc.currentScreen);

        if (hasActiveKeys || inChat) {
            startAnimation();
        } else {
            stopAnimation();
        }

        if (!hasActiveKeys && inChat) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastKeyChange >= 1000) {
                List<String> availableKeys = List.of("A", "B", "C", "D", "E");
                currentRandomKey = availableKeys.get(new Random().nextInt(availableKeys.size()));
                lastKeyChange = currentTime;
            }
        }
    }

    private float lerp(float current, float target, float deltaTime) {
        float factor = (float) (1.0 - Math.pow(0.001, deltaTime * ANIMATION_SPEED));
        return current + (target - current) * factor;
    }

    @Override
    public void drawDraggable(DrawContext context, int alpha) {
        if (alpha <= 0) return;

        float alphaFactor = alpha / 255.0f;

        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastUpdateTime) / 1000.0f;
        lastUpdateTime = currentTime;
        deltaTime = Math.min(deltaTime, 0.1f);

        float x = getX();
        float y = getY();

        boolean hasActiveKeys = !keysList.isEmpty();
        boolean showExample = !hasActiveKeys && isChat(mc.currentScreen);

        float targetWidth = 85;
        float contentRows = 0;

        if (showExample) {
            contentRows = 1;
            String name = "Example Module";
            String bind = currentRandomKey;
            float nameWidth = Fonts.BOLD.getWidth(name, 7);
            float bindWidth = Fonts.BOLD.getWidth(bind, 7);
            targetWidth = Math.max(nameWidth + bindWidth + 35, targetWidth);
        } else {
            for (ModuleStructure module : keysList) {
                contentRows += 1;
                String bind = KeyHelper.getKeyName(module.getKey());
                float nameWidth = Fonts.BOLD.getWidth(module.getName(), 7);
                float bindWidth = Fonts.BOLD.getWidth(bind, 7);
                targetWidth = Math.max(nameWidth + bindWidth + 35, targetWidth);
            }
        }

        float targetHeight = HEADER_HEIGHT + contentRows * ROW_HEIGHT + 4;

        animatedWidth = lerp(animatedWidth, targetWidth, deltaTime);
        animatedHeight = lerp(animatedHeight, targetHeight, deltaTime);

        if (Math.abs(animatedWidth - targetWidth) < 0.3f) animatedWidth = targetWidth;
        if (Math.abs(animatedHeight - targetHeight) < 0.3f) animatedHeight = targetHeight;

        setWidth((int) Math.ceil(animatedWidth));
        setHeight((int) Math.ceil(animatedHeight));

        float w = getWidth();
        float h = animatedHeight;
        int bgAlpha = (int) (230 * alphaFactor);

        if (h > 0) {
            Render2D.gradientRect(x, y, w, h,
                    new int[]{
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB(),
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB()
                    },
                    5);
            Render2D.outline(x, y, w, h, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 5);
        }

        Scissor.enable(x, y, w, h, 2);

        Fonts.BOLD.draw("Hotkeys", x + 8, y + 7f, 8, new Color(255, 255, 255, (int)(255 * alphaFactor)).getRGB());

        float iconBoxSize = 12;
        float iconBoxX = x + w - iconBoxSize - 5;
        float iconBoxY = y + 5;
        Render2D.gradientRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize,
                new int[]{
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB(),
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB()
                }, 3);
        Fonts.HUD_ICONS.draw("e", iconBoxX + 1.5f, iconBoxY + 2f, 9, new Color(130, 140, 255, (int)(255 * alphaFactor)).getRGB());

        float rowY = y + HEADER_HEIGHT;

        if (showExample) {
            drawKeyRow(x, rowY, w, "Example Module", currentRandomKey, (int)(255 * alphaFactor));
        } else {
            for (ModuleStructure module : keysList) {
                drawKeyRow(x, rowY, w, module.getName(), KeyHelper.getKeyName(module.getKey()), (int)(255 * alphaFactor));
                rowY += ROW_HEIGHT;
            }
        }

        Scissor.disable();
    }

    private void drawKeyRow(float x, float rowY, float w, String name, String bind, int alpha) {
        float alphaFactor = alpha / 255.0f;
        int textColor = new Color(220, 220, 220, alpha).getRGB();
        int accentColor = new Color(130, 130, 140, alpha).getRGB();

        Fonts.BOLD.draw(name, x + 8, rowY + (ROW_HEIGHT - 7) / 2f + 0.5f, 7, textColor);

        float bindTextSize = 7f;
        float bindWidth = Fonts.BOLD.getWidth(bind, bindTextSize);
        float boxSize = 12;
        float boxWidth = Math.max(boxSize, bindWidth + 6);
        float boxHeight = boxSize;
        float boxX = x + w - boxWidth - 5;
        float boxY = rowY + (ROW_HEIGHT - boxHeight) / 2f;

        Render2D.gradientRect(boxX, boxY, boxWidth, boxHeight,
                new int[]{
                        new Color(25, 25, 25, (int)(alpha * 0.6f)).getRGB(),
                        new Color(20, 20, 20, (int)(alpha * 0.6f)).getRGB(),
                        new Color(25, 25, 25, (int)(alpha * 0.6f)).getRGB(),
                        new Color(20, 20, 20, (int)(alpha * 0.6f)).getRGB()
                },
                3);
        Render2D.outline(boxX, boxY, boxWidth, boxHeight, 0.2f, new Color(45, 45, 45, (int)(alpha * 0.6f)).getRGB(), 3);

        Fonts.BOLD.draw(bind, boxX + (boxWidth - bindWidth) / 2f, boxY + (boxHeight - 6f) / 2f - 0.5f, bindTextSize, accentColor);
    }
}
