package com.isusdlc.utility.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.elegant;
import com.isusdlc.utility.colors.ColorRGBA;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormat.DrawMode;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public final class Chinahat {
   private static final MinecraftClient mc = MinecraftClient.getInstance();

   public static void render(
      LivingEntity entity,
      MatrixStack matrices,
      Camera camera,
      float tickDelta,
      float width,
      float height,
      ColorRGBA hatColor,
      float alphaVal,
      boolean followHead,
      float heightOffset
   ) {
      if (entity != null && mc.world != null) {
         if (entity != mc.player || !mc.options.getPerspective().isFirstPerson()) {
            ColorRGBA themeColor = elegant.getInstance().getThemeManager().getCurrentTheme().getAdditionalColor();
            ColorRGBA finalColor = hatColor != null ? hatColor : themeColor;
            Vec3d interpolatedPos = Utils.getInterpolatedPos(entity, tickDelta);
            Vec3d cameraPos = camera.getPos();
            double hatX = interpolatedPos.x - cameraPos.getX();
            double hatY = interpolatedPos.y + entity.getHeight() + heightOffset - cameraPos.getY();
            double hatZ = interpolatedPos.z - cameraPos.getZ();
            matrices.push();
            matrices.translate(hatX, hatY, hatZ);
            if (followHead) {
               float headYaw = entity.prevHeadYaw + (entity.headYaw - entity.prevHeadYaw) * tickDelta;
               float headPitch = entity.prevPitch + (entity.getPitch() - entity.prevPitch) * tickDelta;
               matrices.multiply(new Quaternionf().rotationY((float)Math.toRadians(-headYaw)));
               matrices.multiply(new Quaternionf().rotationX((float)Math.toRadians(headPitch * 0.2F)));
            }

            drawConeHat(matrices, finalColor, width, height, alphaVal);
            matrices.pop();
         }
      }
   }

   public static void render(
      LivingEntity entity,
      MatrixStack matrices,
      Camera camera,
      float tickDelta,
      float width,
      float height,
      ColorRGBA hatColor,
      float alphaVal,
      boolean followHead
   ) {
      render(entity, matrices, camera, tickDelta, width, height, hatColor, alphaVal, followHead, 0.1F);
   }

   public static void render(
      LivingEntity entity, MatrixStack matrices, Camera camera, float tickDelta, float width, float height, ColorRGBA hatColor, float alphaVal
   ) {
      render(entity, matrices, camera, tickDelta, width, height, hatColor, alphaVal, true);
   }

   private static void drawConeHat(MatrixStack matrices, ColorRGBA color, float radius, float coneHeight, float alpha) {
      float r = color.getRed() / 255.0F;
      float g = color.getGreen() / 255.0F;
      float b = color.getBlue() / 255.0F;
      float a = alpha;
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      RenderSystem.depthFunc(515);
      RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
      RenderSystem.lineWidth(40.0F);
      int segments = 32;
      BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(DrawMode.TRIANGLES, VertexFormats.POSITION_COLOR);
      Matrix4f matrix = matrices.peek().getPositionMatrix();

      for (int i = 0; i < segments; i++) {
         float angle1 = (float)((Math.PI * 2) * i / segments);
         float angle2 = (float)((Math.PI * 2) * (i + 1) / segments);
         float x1 = radius * (float)Math.cos(angle1);
         float z1 = radius * (float)Math.sin(angle1);
         float x2 = radius * (float)Math.cos(angle2);
         float z2 = radius * (float)Math.sin(angle2);
         buffer.vertex(matrix, x1, 0.0F, z1).color(r, g, b, a);
         buffer.vertex(matrix, x2, 0.0F, z2).color(r, g, b, a);
         buffer.vertex(matrix, 0.0F, coneHeight, 0.0F).color(r, g, b, a);
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      buffer = RenderSystem.renderThreadTesselator().begin(DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
      matrix = matrices.peek().getPositionMatrix();
      buffer.vertex(matrix, 0.0F, 0.0F, 0.0F).color(r, g, b, a);

      for (int i = 0; i <= segments; i++) {
         float angle = (float)((Math.PI * 2) * i / segments);
         float x = radius * (float)Math.cos(angle);
         float z = radius * (float)Math.sin(angle);
         buffer.vertex(matrix, x, 0.0F, z).color(r, g, b, a);
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      buffer = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINE_STRIP, VertexFormats.POSITION_COLOR);
      matrix = matrices.peek().getPositionMatrix();

      for (int i = 0; i <= segments; i++) {
         float angle = (float)((Math.PI * 2) * i / segments);
         float x = radius * (float)Math.cos(angle);
         float z = radius * (float)Math.sin(angle);
         buffer.vertex(matrix, x, 0.0F, z).color(r, g, b, 1.0F);
      }

      BufferRenderer.drawWithGlobalProgram(buffer.end());
      RenderSystem.depthFunc(513);
      RenderSystem.lineWidth(1.0F);
      RenderSystem.disableBlend();
   }
}
