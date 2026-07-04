package com.isusdlc.systems.modules.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ColorSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SelectSetting;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.EntityUtility;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormat.DrawMode;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Custom HitBox",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.custom_hitbox"
)
public class CustomHitBox extends BaseModule {
   private final ModeSetting colorMode = new ModeSetting(this, "modules.settings.custom_hitbox.color_mode");
   private final ModeSetting.Value clientColor = new ModeSetting.Value(this.colorMode, "modules.settings.custom_hitbox.color_mode.client").select();
   private final ModeSetting.Value customColor = new ModeSetting.Value(this.colorMode, "modules.settings.custom_hitbox.color_mode.custom");
   private final ColorSetting hitboxColor = new ColorSetting(this, "modules.settings.custom_hitbox.color", () -> !this.customColor.isSelected())
      .color(Colors.getAccentColor());
   private final SelectSetting entityTypes = new SelectSetting(this, "modules.settings.custom_hitbox.entity_types");
   private final SelectSetting.Value showPlayers = new SelectSetting.Value(this.entityTypes, "modules.settings.custom_hitbox.entity_types.players").select();
   private final SelectSetting.Value showMobs = new SelectSetting.Value(this.entityTypes, "modules.settings.custom_hitbox.entity_types.mobs").select();
   private final SelectSetting.Value showItems = new SelectSetting.Value(this.entityTypes, "modules.settings.custom_hitbox.entity_types.items");
   private final Animation fadeAnim = new Animation(300L, 1.0F, Easing.CUBIC_OUT);

   private final EventListener<Render3DEvent> onRender3D = event -> {
      if (EntityUtility.isInGame()) {
         float alpha = fadeAnim.update(1.0F);
         if (alpha < 0.01F) return;

         MatrixStack matrices = event.getMatrices();
         matrices.push();
         RenderSystem.enableBlend();
         RenderSystem.defaultBlendFunc();
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();

         for (Entity entity : mc.world.getEntities()) {
            if (this.shouldRender(entity) && this.isInFieldOfView(entity, event.getTickDelta())) {
               this.renderBox(matrices, entity, event.getTickDelta(), alpha);
            }
         }

         RenderSystem.enableCull();
         RenderSystem.disableBlend();
         RenderSystem.lineWidth(1.0F);
         matrices.pop();
      }
   };

   private boolean shouldRender(Entity entity) {
      if (entity == mc.player) {
         return false;
      } else if (entity.isInvisible()) {
         return false;
      } else if (entity instanceof PlayerEntity) {
         return this.showPlayers.isSelected();
      } else if (entity instanceof MobEntity) {
         return this.showMobs.isSelected();
      } else {
         return entity instanceof ItemEntity ? this.showItems.isSelected() : false;
      }
   }

   private boolean isInFieldOfView(Entity entity, float partialTicks) {
      Camera camera = mc.gameRenderer.getCamera();
      Vec3d cam = camera.getPos();
      double ex = entity.lastRenderX + (entity.getX() - entity.lastRenderX) * partialTicks;
      double ey = entity.lastRenderY + (entity.getY() - entity.lastRenderY) * partialTicks + entity.getHeight() * 0.5;
      double ez = entity.lastRenderZ + (entity.getZ() - entity.lastRenderZ) * partialTicks;
      double dx = ex - cam.x;
      double dy = ey - cam.y;
      double dz = ez - cam.z;
      double distXZ = Math.sqrt(dx * dx + dz * dz);
      if (distXZ < 1.0E-6) {
         return true;
      } else {
         float targetYaw = (float)(MathHelper.atan2(dz, dx) * 180.0 / Math.PI) - 90.0F;
         float targetPitch = (float)(-(MathHelper.atan2(dy, distXZ) * 180.0 / Math.PI));
         float camYaw = camera.getYaw();
         float camPitch = camera.getPitch();
         float dyaw = MathHelper.wrapDegrees(targetYaw - camYaw);
         float dpitch = MathHelper.wrapDegrees(targetPitch - camPitch);
         return Math.abs(dyaw) <= 75.0F && Math.abs(dpitch) <= 75.0F;
      }
   }

   private void renderBox(MatrixStack matrices, Entity target, float partialTicks, float alpha) {
      if (target != null) {
         Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();
         double x = target.lastRenderX + (target.getX() - target.lastRenderX) * partialTicks;
         double y = target.lastRenderY + (target.getY() - target.lastRenderY) * partialTicks;
         double z = target.lastRenderZ + (target.getZ() - target.lastRenderZ) * partialTicks;
         Box boundingBox = target.getBoundingBox();
         double minX = boundingBox.minX - target.getX() + x - cameraPos.x;
         double minY = boundingBox.minY - target.getY() + y - cameraPos.y;
         double minZ = boundingBox.minZ - target.getZ() + z - cameraPos.z;
         double maxX = boundingBox.maxX - target.getX() + x - cameraPos.x;
         double maxY = boundingBox.maxY - target.getY() + y - cameraPos.y;
         double maxZ = boundingBox.maxZ - target.getZ() + z - cameraPos.z;
         ColorRGBA baseColor = this.clientColor.isSelected() ? Colors.getAccentColor() : this.hitboxColor.getColor();
         ColorRGBA color1 = baseColor.withAlpha((int)(baseColor.getAlpha() * 0.3F * alpha));
         ColorRGBA color2 = baseColor.withAlpha((int)(baseColor.getAlpha() * 0.6F * alpha));
         ColorRGBA color3 = baseColor.withAlpha((int)(baseColor.getAlpha() * 0.3F * alpha));
         ColorRGBA color4 = baseColor.withAlpha((int)(baseColor.getAlpha() * 0.6F * alpha));
         ColorRGBA[] gradientColors = new ColorRGBA[]{color1, color2, color3, color4};
         Matrix4f matrix = matrices.peek().getPositionMatrix();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder fillBuffer = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
         this.drawBoxFill(fillBuffer, matrix, minX, minY + 0.01F, minZ, maxX, maxY, maxZ, gradientColors, (int)(85 * alpha));
         BufferRenderer.drawWithGlobalProgram(fillBuffer.end());
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         RenderSystem.lineWidth(3.0F);
         BufferBuilder lineBuffer = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
         this.drawBoxOutline(lineBuffer, matrix, minX, minY + 0.01F, minZ, maxX, maxY, maxZ, gradientColors);
         BufferRenderer.drawWithGlobalProgram(lineBuffer.end());
      }
   }

   private void drawBoxFill(
      BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, ColorRGBA[] colors, int fillAlpha
   ) {
      int[] c = new int[4];
      int[][] rgba = new int[4][4];

      for (int i = 0; i < 4; i++) {
         int color = colors[i].getRGB();
         c[i] = this.replAlpha(color, fillAlpha);
         rgba[i][0] = c[i] >> 16 & 0xFF;
         rgba[i][1] = c[i] >> 8 & 0xFF;
         rgba[i][2] = c[i] & 0xFF;
         rgba[i][3] = c[i] >> 24 & 0xFF;
      }

      buffer.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
      buffer.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(rgba[3][0], rgba[3][1], rgba[3][2], rgba[3][3]);
      buffer.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(rgba[0][0], rgba[0][1], rgba[0][2], rgba[0][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
      buffer.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(rgba[1][0], rgba[1][1], rgba[1][2], rgba[1][3]);
      buffer.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(rgba[2][0], rgba[2][1], rgba[2][2], rgba[2][3]);
   }

   private int replAlpha(int color, int alpha) {
      int r = color >> 16 & 0xFF;
      int g = color >> 8 & 0xFF;
      int b = color & 0xFF;
      return alpha << 24 | r << 16 | g << 8 | b;
   }

   private void drawBoxOutline(
      BufferBuilder buffer, Matrix4f matrix, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, ColorRGBA[] colors
   ) {
      int[] c = new int[4];

      for (int i = 0; i < 4; i++) {
         c[i] = this.replAlpha(colors[i].getRGB(), 255);
      }

      this.drawLine(buffer, matrix, minX, minY, minZ, maxX, minY, minZ, c[0], c[1]);
      this.drawLine(buffer, matrix, maxX, minY, minZ, maxX, minY, maxZ, c[1], c[2]);
      this.drawLine(buffer, matrix, maxX, minY, maxZ, minX, minY, maxZ, c[2], c[3]);
      this.drawLine(buffer, matrix, minX, minY, maxZ, minX, minY, minZ, c[3], c[0]);
      this.drawLine(buffer, matrix, minX, maxY, minZ, maxX, maxY, minZ, c[0], c[1]);
      this.drawLine(buffer, matrix, maxX, maxY, minZ, maxX, maxY, maxZ, c[1], c[2]);
      this.drawLine(buffer, matrix, maxX, maxY, maxZ, minX, maxY, maxZ, c[2], c[3]);
      this.drawLine(buffer, matrix, minX, maxY, maxZ, minX, maxY, minZ, c[3], c[0]);
      this.drawLine(buffer, matrix, minX, minY, minZ, minX, maxY, minZ, c[0], c[0]);
      this.drawLine(buffer, matrix, maxX, minY, minZ, maxX, maxY, minZ, c[1], c[1]);
      this.drawLine(buffer, matrix, maxX, minY, maxZ, maxX, maxY, maxZ, c[2], c[2]);
      this.drawLine(buffer, matrix, minX, minY, maxZ, minX, maxY, maxZ, c[3], c[3]);
   }

   private void drawLine(BufferBuilder buffer, Matrix4f matrix, double x1, double y1, double z1, double x2, double y2, double z2, int color1, int color2) {
      int r1 = color1 >> 16 & 0xFF;
      int g1 = color1 >> 8 & 0xFF;
      int b1 = color1 & 0xFF;
      int a1 = color1 >> 24 & 0xFF;
      int r2 = color2 >> 16 & 0xFF;
      int g2 = color2 >> 8 & 0xFF;
      int b2 = color2 & 0xFF;
      int a2 = color2 >> 24 & 0xFF;
      buffer.vertex(matrix, (float)x1, (float)y1, (float)z1).color(r1, g1, b1, a1);
      buffer.vertex(matrix, (float)x2, (float)y2, (float)z2).color(r2, g2, b2, a2);
   }
}
