package com.isusdlc.hud.legacy;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import com.isusdlc.utility.animation.base.Direction;

import java.awt.*;
import java.util.Locale;

public class Info extends AbstractHudElement {

    private double lastX = 0;
    private double lastZ = 0;
    private double currentBps = 0;
    private double displayBps = 0;
    private double targetBps = 0;
    private long lastUpdateTime = 0;

    private static final double BPS_SMOOTHING = 0.05;
    private static final double DISPLAY_SMOOTHING = 0.03;

    private final Animation chatAnimation = new Decelerate().setMs(250).setValue(14);

    public Info() {
        super("Info", 10, 0, 100, 60, false);
        startAnimation();
    }

    @Override
    public void tick() {
    }

    private double roundToStep(double value, double step) {
        return Math.round(value / step) * step;
    }

    @Override
    public void drawDraggable(DrawContext context, int alpha) {
        if (alpha <= 0) return;
        if (mc.player == null) return;

        boolean showBps = HudModule.getInstance() != null && HudModule.getInstance().showBps.isValue();

        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0;

        if (lastUpdateTime > 0 && deltaTime > 0) {
            double dx = mc.player.getX() - lastX;
            double dz = mc.player.getZ() - lastZ;
            double distance = Math.sqrt(dx * dx + dz * dz);
            double instantBps = distance / deltaTime;

            currentBps = currentBps + (instantBps - currentBps) * BPS_SMOOTHING;
            targetBps = roundToStep(currentBps, 0.1);
        }

        displayBps = displayBps + (targetBps - displayBps) * DISPLAY_SMOOTHING;

        lastX = mc.player.getX();
        lastZ = mc.player.getZ();
        lastUpdateTime = currentTime;

        chatAnimation.setDirection(mc.currentScreen instanceof ChatScreen ? Direction.FORWARDS : Direction.BACKWARDS);

        int totalHeight = 16;
        if (showBps) totalHeight += 18;
        totalHeight += 18;

        int screenHeight = mc.getWindow().getScaledHeight();
        float x = 5;
        float y = screenHeight - totalHeight - 5 - (chatAnimation.getOutput().floatValue());

        setX((int) x);
        setY((int) y);

        float currentY = y;

        float tps = TPSCalculate.getInstance().getTPS();
        String tpsValue = String.format(Locale.US, "%.1f", tps).replace('.', ',');
        drawInfoBox(context, x, currentY, "A", "TPS ", tpsValue, new Color(130, 140, 255), 2.5f);
        currentY += 18;

        if (showBps) {
            String bpsValue = String.format(Locale.US, "%.1f", displayBps).replace('.', ',');
            drawInfoBox(context, x, currentY, "E", "BPS ", bpsValue, new Color(130, 140, 255), 3f);
            currentY += 18;
        }

        int playerX = (int) mc.player.getX();
        int playerY = (int) mc.player.getY();
        int playerZ = (int) mc.player.getZ();

        drawCoordsBox(context, x, currentY, "n", playerX, playerY, playerZ, new Color(130, 140, 255));

        setHeight(totalHeight);
    }

    private void drawInfoBox(DrawContext context, float x, float y, String icon, String label, String value, Color iconColor, float iconYOffset) {
        float labelWidth = Fonts.BOLD.getWidth(label, 7);
        float valueWidth = Fonts.BOLD.getWidth(value, 7);
        float boxWidth = 5 + 10 + 5 + labelWidth + valueWidth + 8;
        float boxHeight = 16;

        int bgAlpha = 200;
        Render2D.rect(x, y, boxWidth, boxHeight, new Color(15, 15, 15, bgAlpha).getRGB(), 4);
        Render2D.outline(x, y, boxWidth, boxHeight, 0.5f, new Color(45, 45, 45, bgAlpha).getRGB(), 4);

        float iconBoxSize = 12;
        float iconBoxX = x + 4;
        float iconBoxY = y + 2;
        Render2D.gradientRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize,
                new int[]{
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB(),
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB()
                }, 3);

        Fonts.HUDNEW.draw(icon, x + 5.5f, y + iconYOffset, 9, iconColor.getRGB());
        Fonts.BOLD.draw(label, x + 19, y + 5.5f, 7, new Color(160, 160, 160).getRGB());
        Fonts.BOLD.draw(value, x + 19 + labelWidth, y + 5.5f, 7, Color.WHITE.getRGB());

        if (boxWidth > getWidth()) setWidth((int) boxWidth);
    }

    private void drawCoordsBox(DrawContext context, float x, float y, String icon, int px, int py, int pz, Color iconColor) {
        String xLab = "X ";
        String yLab = " Y ";
        String zLab = " Z ";
        String xVal = String.valueOf(px);
        String yVal = String.valueOf(py);
        String zVal = String.valueOf(pz);

        float xLabW = Fonts.BOLD.getWidth(xLab, 7);
        float yLabW = Fonts.BOLD.getWidth(yLab, 7);
        float zLabW = Fonts.BOLD.getWidth(zLab, 7);
        float xValW = Fonts.BOLD.getWidth(xVal, 7);
        float yValW = Fonts.BOLD.getWidth(yVal, 7);
        float zValW = Fonts.BOLD.getWidth(zVal, 7);

        float boxWidth = 5 + 10 + 5 + xLabW + xValW + yLabW + yValW + zLabW + zValW + 8;
        float boxHeight = 16;

        int bgAlpha = 200;
        Render2D.rect(x, y, boxWidth, boxHeight, new Color(15, 15, 15, bgAlpha).getRGB(), 4);
        Render2D.outline(x, y, boxWidth, boxHeight, 0.5f, new Color(45, 45, 45, bgAlpha).getRGB(), 4);

        float iconBoxSize = 12;
        float iconBoxX = x + 4;
        float iconBoxY = y + 2;
        Render2D.gradientRect(iconBoxX, iconBoxY, iconBoxSize, iconBoxSize,
                new int[]{
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB(),
                        new Color(30, 30, 45, bgAlpha).getRGB(),
                        new Color(25, 25, 40, bgAlpha).getRGB()
                }, 3);

        Fonts.ICONSTYPETHO.draw(icon, x + 5.5f, y + 4f, 9, iconColor.getRGB());

        float curX = x + 19;
        Fonts.BOLD.draw(xLab, curX, y + 5.5f, 7, new Color(160, 160, 160).getRGB());
        curX += xLabW;
        Fonts.BOLD.draw(xVal, curX, y + 5.5f, 7, Color.WHITE.getRGB());
        curX += xValW;

        Fonts.BOLD.draw(yLab, curX, y + 5.5f, 7, new Color(160, 160, 160).getRGB());
        curX += yLabW;
        Fonts.BOLD.draw(yVal, curX, y + 5.5f, 7, Color.WHITE.getRGB());
        curX += yValW;

        Fonts.BOLD.draw(zLab, curX, y + 5.5f, 7, new Color(160, 160, 160).getRGB());
        curX += zLabW;
        Fonts.BOLD.draw(zVal, curX, y + 5.5f, 7, Color.WHITE.getRGB());

        if (boxWidth > getWidth()) setWidth((int) boxWidth);
    }
}
