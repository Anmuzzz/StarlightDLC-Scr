package com.isusdlc.hud.legacy;

import com.isusdlc.framework.msdf.MsdfFont;
import com.isusdlc.framework.msdf.MsdfRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public final class Fonts {

    private static MsdfFont getBoldFont() {
        return com.isusdlc.framework.msdf.Fonts.BOLD;
    }

    public static class BOLD {
        public static void draw(String text, float x, float y, float size, int color) {
            if (!com.isusdlc.framework.msdf.Fonts.isInitialized() || getBoldFont() == null) return;
            Matrix4f matrix = getMatrix();
            MsdfRenderer.renderText(getBoldFont(), text, size, color, matrix, x, y, 0);
        }

        public static float getWidth(String text, float size) {
            if (!com.isusdlc.framework.msdf.Fonts.isInitialized() || getBoldFont() == null)
                return text.length() * size * 0.5f;
            return getBoldFont().getWidth(text, size);
        }
    }

    public static class HUDNEW {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class CATEGORY_ICONS {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class safinBpekax {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class BEBRA {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class ICONS {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class HUD_ICONS {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    public static class ICONSTYPETHO {
        public static void draw(String text, float x, float y, float size, int color) {
        }

        public static float getWidth(String text, float size) {
            return text.length() * size * 0.5f;
        }
    }

    private static Matrix4f getMatrix() {
        DrawContext ctx = Render2D.getContext();
        if (ctx != null) {
            return ctx.getMatrices().peek().getPositionMatrix();
        }
        return new MatrixStack().peek().getPositionMatrix();
    }

    private Fonts() {
        throw new UnsupportedOperationException("Utility class");
    }
}
