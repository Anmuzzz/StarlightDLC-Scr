package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Notifications extends AbstractHudElement {

    private static final int FORCED_GUI_SCALE = 2;

    private static Notifications instance;

    public static Notifications getInstance() {
        return instance;
    }

    public enum Type {
        SUCCESS(new Color(45, 190, 100), "P"),
        ERROR(new Color(220, 60, 60), "O"),
        INFO(new Color(60, 130, 220), "N"),
        WARNING(new Color(230, 170, 50), "C");

        private final Color color;
        private final String icon;

        Type(Color color, String icon) {
            this.color = color;
            this.icon = icon;
        }

        public Color getColor() {
            return color;
        }

        public String getIcon() {
            return icon;
        }
    }

    private final List<Notification> list = new ArrayList<>();
    private static final float NOTIFICATION_HEIGHT = 16f;
    private static final float NOTIFICATION_GAP = 3f;

    public Notifications() {
        super("Notifications", 10, 100, 110, 16, true);
        instance = this;
    }

    private int getCurrentGuiScale() {
        int scale = mc.options.getGuiScale().getValue();
        if (scale == 0) {
            scale = mc.getWindow().calculateScaleFactor(0, mc.forcesUnicodeFont());
        }
        return scale;
    }

    private float getScaleFactor() {
        return (float) getCurrentGuiScale() / (float) FORCED_GUI_SCALE;
    }

    private float getVirtualWidth() {
        return mc.getWindow().getFramebufferWidth() / (float) FORCED_GUI_SCALE;
    }

    private float getVirtualHeight() {
        return mc.getWindow().getFramebufferHeight() / (float) FORCED_GUI_SCALE;
    }

    @Override
    public boolean visible() {
        return !list.isEmpty() || isChat(mc.currentScreen);
    }

    @Override
    public void tick() {
        if (isChat(mc.currentScreen) || !list.isEmpty()) {
            startAnimation();
        } else {
            stopAnimation();
        }

        list.forEach(notif -> {
            if (System.currentTimeMillis() > notif.removeTime) {
                notif.anim.setDirection(com.isusdlc.utility.animation.base.Direction.BACKWARDS);
            }
        });
        list.removeIf(notif -> notif.anim.isFinished(com.isusdlc.utility.animation.base.Direction.BACKWARDS));
    }

    private void updatePosition() {
        if (mc.getWindow() == null) return;

        float virtualWidth = getVirtualWidth();
        float virtualHeight = getVirtualHeight();

        float crosshairX = virtualWidth / 2f;
        float crosshairY = virtualHeight / 2f;

        this.setX((int) (crosshairX - 60));
        this.setY((int) (crosshairY + 100));
    }

    public void addNotification(String text, Type type, long duration) {
        Animation anim = new OutBack().setMs(700).setValue(1);
        anim.setDirection(com.isusdlc.utility.animation.base.Direction.FORWARDS);

        int targetIndex = list.size();
        float targetY = targetIndex * (NOTIFICATION_HEIGHT + NOTIFICATION_GAP);

        Notification notification = new Notification(text, type, anim, System.currentTimeMillis(), System.currentTimeMillis() + duration);
        notification.currentY = targetY;
        notification.targetY = targetY;
        notification.velocityY = 0;

        list.add(notification);
        if (list.size() > 12) list.removeFirst();
        list.sort(Comparator.comparingDouble(notif -> -notif.removeTime));

        updateTargetPositions();
    }

    public void addNotification(String text, long duration) {
        addNotification(text, Type.INFO, duration);
    }

    private void updateTargetPositions() {
        float offsetY = 0;
        for (int i = 0; i < list.size(); i++) {
            Notification notif = list.get(i);
            float anim = notif.anim.getOutput().floatValue();
            notif.targetY = offsetY;
            offsetY += (NOTIFICATION_HEIGHT + NOTIFICATION_GAP) * anim;
        }
    }

    private int clampAlpha(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private int clampAlpha(float value) {
        return Math.max(0, Math.min(255, (int) value));
    }

    @Override
    public void drawDraggable(DrawContext context, int alpha) {
        alpha = clampAlpha(alpha);
        if (alpha <= 0) return;

        float alphaFactor = alpha / 255.0f;
        updateTargetPositions();

        float springStiffness = 180f;
        float damping = 12f;
        float deltaTime = 0.016f;

        for (Notification notification : list) {
            float diff = notification.targetY - notification.currentY;
            float springForce = diff * springStiffness;
            float dampingForce = notification.velocityY * damping;
            float acceleration = springForce - dampingForce;

            notification.velocityY += acceleration * deltaTime;
            notification.currentY += notification.velocityY * deltaTime;

            if (Math.abs(diff) < 0.01f && Math.abs(notification.velocityY) < 0.01f) {
                notification.currentY = notification.targetY;
                notification.velocityY = 0;
            }
        }

        float offsetX = 5;
        float maxWidth = 0;
        float totalHeight = 0;

        if (list.isEmpty() && isChat(mc.currentScreen)) {
            float textWidth = Fonts.BOLD.getWidth("Notifications (Drag Me)", 6f);
            float iconSize = 15f;
            float width = textWidth + iconSize + 6f;
            float startX = this.getX();
            float startY = this.getY();

            int bgAlpha = 230;
            Render2D.gradientRect(startX, startY, width, NOTIFICATION_HEIGHT,
                    new int[]{
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB(),
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB()
                    }, 4);
            Render2D.outline(startX, startY, width, NOTIFICATION_HEIGHT, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 4);

            Fonts.BEBRA.draw("H", startX + 1f, startY + (NOTIFICATION_HEIGHT - iconSize) / 2f - 1.5f, iconSize, new Color(130, 140, 255, 255).getRGB());
            Fonts.BOLD.draw("Notifications (Drag Me)", startX + iconSize + 1f, startY + 4.5f, 6f, new Color(255, 255, 255, 255).getRGB());

            setWidth((int) Math.ceil(width));
            setHeight((int) Math.ceil(NOTIFICATION_HEIGHT));
            return;
        }

        for (Notification notification : list) {
            float anim = notification.anim.getOutput().floatValue();
            if (anim <= 0.01f) continue;
            float textWidth = Fonts.BOLD.getWidth(notification.text, 6f);
            maxWidth = Math.max(maxWidth, textWidth + 15f + 6f);
        }

        for (Notification notification : list) {
            float anim = notification.anim.getOutput().floatValue();
            if (anim <= 0.01f) continue;

            anim = Math.max(0f, Math.min(1f, anim));

            float textWidth = Fonts.BOLD.getWidth(notification.text, 6f);
            float iconSize = 15f;
            float width = textWidth + iconSize + 6f;

            float startY = this.getY() + notification.currentY;
            float startX = this.getX() + (maxWidth - width) / 2f;

            int bgAlpha = 230;
            Render2D.gradientRect(startX, startY, width, NOTIFICATION_HEIGHT,
                    new int[]{
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB(),
                            new Color(20, 20, 20, bgAlpha).getRGB(),
                            new Color(15, 15, 15, bgAlpha).getRGB()
                    }, 4);
            Render2D.outline(startX, startY, width, NOTIFICATION_HEIGHT, 0.35f, new Color(45, 45, 45, bgAlpha).getRGB(), 4);

            float iconX = startX + 1f;
            float iconY = startY + (NOTIFICATION_HEIGHT - iconSize) / 2f - 1.5f;

            Fonts.BEBRA.draw("H", iconX, iconY, iconSize, new Color(130, 140, 255, 255).getRGB());

            Fonts.BOLD.draw(notification.text, iconX + iconSize + 1f, startY + 4.5f, 6f,
                    new Color(255, 255, 255, 255).getRGB());

            totalHeight = Math.max(totalHeight, notification.currentY + NOTIFICATION_HEIGHT);
        }

        if (maxWidth > 0) {
            setWidth((int) Math.ceil(maxWidth));
        }
        setHeight((int) Math.ceil(Math.max(NOTIFICATION_HEIGHT, totalHeight)));
    }

    public static class Notification {
        String text;
        Type type;
        Animation anim;
        long startTime;
        long removeTime;

        float currentY;
        float targetY;
        float velocityY;

        Notification(String text, Type type, Animation anim, long startTime, long removeTime) {
            this.text = text;
            this.type = type;
            this.anim = anim;
            this.startTime = startTime;
            this.removeTime = removeTime;
            this.currentY = 0;
            this.targetY = 0;
            this.velocityY = 0;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > removeTime;
        }
    }
}
