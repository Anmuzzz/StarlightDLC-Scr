package com.isusdlc.hud.legacy;

import com.isusdlc.utility.interfaces.IWindow;
import org.lwjgl.opengl.GL11;

public final class Scissor implements IWindow {

    public static void enable(float x, float y, float width, float height) {
        int fbHeight = mw.getFramebufferHeight();
        double scale = mw.getScaleFactor();
        int sx = (int) (x * scale);
        int sy = (int) (fbHeight - (y + height) * scale);
        int sw = (int) Math.ceil(width * scale);
        int sh = (int) Math.ceil(height * scale);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(sx, sy, sw, sh);
    }

    public static void enable(float x, float y, float width, float height, float radius) {
        enable(x, y, width, height);
    }

    public static void disable() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private Scissor() {
        throw new UnsupportedOperationException("Utility class");
    }
}
