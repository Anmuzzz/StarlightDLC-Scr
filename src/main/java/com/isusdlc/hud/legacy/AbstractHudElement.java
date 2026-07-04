package com.isusdlc.hud.legacy;

import com.isusdlc.utility.animation.base.Direction;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;

public abstract class AbstractHudElement implements IMinecraft {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float lastTickDelta;
    protected Animation scaleAnimation = new Animation();
    private final String name;
    private final boolean visibleByDefault;

    public AbstractHudElement(String name, int x, int y, int width, int height, boolean visible) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.visibleByDefault = visible;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public abstract void drawDraggable(DrawContext context, int alpha);

    public void tick() {
    }

    public boolean visible() {
        return true;
    }

    public void startAnimation() {
        scaleAnimation.setDirection(Direction.FORWARDS);
    }

    public void stopAnimation() {
        scaleAnimation.setDirection(Direction.BACKWARDS);
    }

    public boolean isChat(Screen screen) {
        return screen instanceof ChatScreen;
    }

    public void render(DrawContext context, int alpha) {
        lastTickDelta = mc.getRenderTickCounter().getTickDelta(false);
        Render2D.setContext(context);
        if (visible()) {
            drawDraggable(context, alpha);
        }
        Render2D.clearContext();
    }
}
