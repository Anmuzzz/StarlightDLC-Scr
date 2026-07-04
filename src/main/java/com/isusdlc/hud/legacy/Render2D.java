package com.isusdlc.hud.legacy;

import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.render.DrawUtility;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public final class Render2D {
    private static final ThreadLocal<DrawContext> CURRENT_CONTEXT = new ThreadLocal<>();

    public static void setContext(DrawContext ctx) {
        CURRENT_CONTEXT.set(ctx);
    }

    public static DrawContext getContext() {
        return CURRENT_CONTEXT.get();
    }

    public static void clearContext() {
        CURRENT_CONTEXT.remove();
    }

    private static MatrixStack getMatrices() {
        DrawContext ctx = CURRENT_CONTEXT.get();
        if (ctx != null) return ctx.getMatrices();
        return new MatrixStack();
    }

    public static void gradientRect(float x, float y, float width, float height, int[] colors, float radius) {
        if (colors.length < 2) return;
        MatrixStack matrices = getMatrices();
        DrawUtility.drawSetup();
        BorderRadius br = BorderRadius.all(radius);
        ColorRGBA tl = ColorRGBA.fromInt(colors[0]);
        ColorRGBA bl = ColorRGBA.fromInt(colors.length > 1 ? colors[1] : colors[0]);
        ColorRGBA brc = ColorRGBA.fromInt(colors.length > 2 ? colors[2] : colors[0]);
        ColorRGBA tr = ColorRGBA.fromInt(colors.length > 3 ? colors[3] : colors[0]);
        DrawUtility.drawRoundedRect(matrices, x, y, width, height, br, tl, bl, brc, tr);
        DrawUtility.drawEnd();
    }

    public static void outline(float x, float y, float width, float height, float thickness, int color, float radius) {
        MatrixStack matrices = getMatrices();
        DrawUtility.drawSetup();
        DrawUtility.drawRoundedBorder(matrices, x, y, width, height, thickness, BorderRadius.all(radius), ColorRGBA.fromInt(color));
        DrawUtility.drawEnd();
    }

    public static void rect(float x, float y, float width, float height, int color, float radius) {
        MatrixStack matrices = getMatrices();
        DrawUtility.drawSetup();
        DrawUtility.drawRoundedRect(matrices, x, y, width, height, BorderRadius.all(radius), ColorRGBA.fromInt(color));
        DrawUtility.drawEnd();
    }

    public static void texture(Identifier tex, float x, float y, float width, float height,
                               float u0, float v0, float u1, float v1, int color, float borderWidth, float radius) {
        MatrixStack matrices = getMatrices();
        ColorRGBA clr = ColorRGBA.fromInt(color);
        if (radius > 0) {
            DrawUtility.drawRoundedTextureWithUV(matrices, tex, x, y, width, height, BorderRadius.all(radius), clr, u0, v0, u1, v1);
        } else {
            DrawUtility.drawTexture(matrices, tex, x, y, width, height, u0, u1, v0, v1, clr);
        }
    }

    public static void arc(float x, float y, float size, float thickness, float degree, float rotation, int color) {
        if (degree <= 0) return;
        MatrixStack matrices = getMatrices();
        matrices.push();
        org.joml.Matrix4f matrix = matrices.peek().getPositionMatrix();
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
        com.mojang.blaze3d.systems.RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
        float rad = size / 2.0f;
        float centerX = x + rad;
        float centerY = y + rad;
        int segments = Math.max(4, (int) (degree / 360.0f * 64));
        float startAngle = (float) Math.toRadians(rotation - 90);
        float arcRad = (float) Math.toRadians(degree);
        for (int i = 0; i <= segments; i++) {
            float angle = startAngle + arcRad * (float) i / segments;
            float px = centerX + rad * (float) Math.cos(angle);
            float py = centerY + rad * (float) Math.sin(angle);
            builder.vertex(matrix, px, py, 0).color(color);
        }
        BufferRenderer.drawWithGlobalProgram(builder.end());
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
        matrices.pop();
    }

    public static void blur(float x, float y, float width, float height, float blurRadius, float squirt, int blurTint) {
        if (blurRadius <= 0 || width <= 0 || height <= 0) return;
        MatrixStack matrices = getMatrices();
        DrawUtility.drawBlur(matrices, x, y, width, height, blurRadius, squirt, BorderRadius.all(squirt), ColorRGBA.fromInt(blurTint));
    }

    private Render2D() {
        throw new UnsupportedOperationException("Utility class");
    }
}
