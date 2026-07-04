package com.isusdlc.systems.modules.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.event.impl.game.TotemLossEvent;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ColorSetting;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.render.Draw3DUtility;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.VertexFormat.DrawMode;

@ModuleInfo(
   name = "Hit Effect",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.hiteffect"
)
public class HitEffect extends BaseModule {
   private final List<HitEffect.WaveEffect> waves = new ArrayList<>();
   private final BooleanSetting syncWithTheme = new BooleanSetting(this, "modules.settings.hiteffect.sync_with_theme").enabled(true);
   private final ColorSetting color = new ColorSetting(this, "modules.settings.hiteffect.color", () -> this.syncWithTheme.isEnabled())
      .color(Colors.getAccentColor())
      .alpha(true);
   private final ColorSetting totemColor = new ColorSetting(this, "modules.settings.hiteffect.totem_color", () -> this.syncWithTheme.isEnabled())
      .color(new ColorRGBA(255.0F, 215.0F, 0.0F, 255.0F))
      .alpha(true);
   private final EventListener<AttackEvent> onAttack = event -> {
      if (mc.player != null && event.getEntity() != null) {
         Vec3d pos = event.getEntity().getPos();
         ColorRGBA effectColor = this.syncWithTheme.isEnabled() ? Colors.getAccentColor() : this.color.getColor();
         this.waves.add(new HitEffect.WaveEffect(pos, effectColor));
      }
   };
   private final EventListener<TotemLossEvent> onTotemLoss = event -> {
      if (event.getPlayer() != null) {
         Vec3d pos = event.getPlayer().getPos();
         ColorRGBA effectColor = this.syncWithTheme.isEnabled() ? Colors.getAccentColor() : this.totemColor.getColor();
         this.waves.add(new HitEffect.WaveEffect(pos, effectColor));
      }
   };
   private final EventListener<Render3DEvent> on3DRender = event -> {
      if (!this.waves.isEmpty()) {
         MatrixStack ms = event.getMatrices();
         Camera camera = mc.gameRenderer.getCamera();
         Vec3d cameraPos = camera.getPos();
         ms.push();
         ms.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
         RenderSystem.enableBlend();
         RenderSystem.blendFunc(SrcFactor.SRC_ALPHA, DstFactor.ONE_MINUS_SRC_ALPHA);
         RenderSystem.enableDepthTest();
         RenderSystem.disableCull();
         RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
         BufferBuilder fillBuffer = RenderSystem.renderThreadTesselator().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);

         for (HitEffect.WaveEffect wave : this.waves) {
            wave.renderFill(fillBuffer, ms);
         }

         BuiltBuffer fillBuilt = fillBuffer.endNullable();
         if (fillBuilt != null) {
            BufferRenderer.drawWithGlobalProgram(fillBuilt);
         }

         BufferBuilder linesBuffer = RenderSystem.renderThreadTesselator().begin(DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

         for (HitEffect.WaveEffect wave : this.waves) {
            wave.renderOutline(linesBuffer, ms);
         }

         BuiltBuffer linesBuilt = linesBuffer.endNullable();
         if (linesBuilt != null) {
            BufferRenderer.drawWithGlobalProgram(linesBuilt);
         }

         RenderSystem.enableCull();
         RenderSystem.enableDepthTest();
         RenderSystem.disableBlend();
         ms.pop();
         this.waves.removeIf(wavex -> wavex.animation.getValue() >= 1.0F);
      }
   };

   @Override
   public void onDisable() {
      this.waves.clear();
   }

   static class BlockOutline {
      final BlockPos pos;
      final double distance;

      public BlockOutline(BlockPos pos, double distance) {
         this.pos = pos;
         this.distance = distance;
      }
   }

   static class WaveEffect {
      final Vec3d centerPos;
      final ColorRGBA color;
      final Animation animation = new Animation(1200L, 0.0F, Easing.LINEAR);
      final List<HitEffect.BlockOutline> blocks = new ArrayList<>();
      final int maxRadius = 15;

      public WaveEffect(Vec3d pos, ColorRGBA color) {
         this.centerPos = pos;
         this.color = color;
         this.animation.update(true);
         BlockPos centerBlock = BlockPos.ofFloored(pos);

         for (int x = -15; x <= 15; x++) {
            for (int z = -15; z <= 15; z++) {
               double distance = Math.sqrt(x * x + z * z);
               if (distance <= 15.0 && distance >= 1.0) {
                  BlockPos foundPos = null;

                  for (int y = 3; y >= -10; y--) {
                     BlockPos checkPos = centerBlock.add(x, y, z);
                     if (IMinecraft.mc.world != null && !IMinecraft.mc.world.getBlockState(checkPos).isAir()) {
                        foundPos = checkPos;
                        break;
                     }
                  }

                  if (foundPos != null) {
                     this.blocks.add(new HitEffect.BlockOutline(foundPos, distance));
                  }
               }
            }
         }
      }

      void renderFill(BufferBuilder builder, MatrixStack ms) {
         this.animation.update(true);
         float progress = this.animation.getValue();
         float currentRadius = progress * 15.0F;
         float waveThickness = 1.0F;

         for (HitEffect.BlockOutline block : this.blocks) {
            float distDiff = Math.abs((float)block.distance - currentRadius);
            if (!(distDiff > waveThickness)) {
               float alpha = 1.0F - distDiff / waveThickness;
               alpha = (float)Math.pow(alpha, 0.5);
               float fadeOut = 1.0F;
               if (progress > 0.7F) {
                  fadeOut = 1.0F - (progress - 0.7F) / 0.3F;
               }

               alpha *= fadeOut;
               if (!(alpha <= 0.05F)) {
                  float scale = 1.0F;
                  if (progress > 0.7F) {
                     scale = 0.5F + 0.5F * fadeOut;
                  }

                  Vec3d center = block.pos.toCenterPos();
                  double halfSize = 0.5 * scale;
                  Box box = new Box(
                     center.x - halfSize,
                     center.y - halfSize,
                     center.z - halfSize,
                     center.x + halfSize,
                     center.y + halfSize,
                     center.z + halfSize
                  );
                  Draw3DUtility.renderFilledBox(ms, builder, box, this.color.withAlpha(alpha * 40.0F));
               }
            }
         }
      }

      void renderOutline(BufferBuilder builder, MatrixStack ms) {
         this.animation.update(true);
         float progress = this.animation.getValue();
         float currentRadius = progress * 15.0F;
         float waveThickness = 1.0F;

         for (HitEffect.BlockOutline block : this.blocks) {
            float distDiff = Math.abs((float)block.distance - currentRadius);
            if (!(distDiff > waveThickness)) {
               float alpha = 1.0F - distDiff / waveThickness;
               alpha = (float)Math.pow(alpha, 0.5);
               float fadeOut = 1.0F;
               if (progress > 0.7F) {
                  fadeOut = 1.0F - (progress - 0.7F) / 0.3F;
               }

               alpha *= fadeOut;
               if (!(alpha <= 0.05F)) {
                  float scale = 1.0F;
                  if (progress > 0.7F) {
                     scale = 0.5F + 0.5F * fadeOut;
                  }

                  Vec3d center = block.pos.toCenterPos();
                  double halfSize = 0.5 * scale;
                  Box box = new Box(
                     center.x - halfSize,
                     center.y - halfSize,
                     center.z - halfSize,
                     center.x + halfSize,
                     center.y + halfSize,
                     center.z + halfSize
                  );
                  Draw3DUtility.renderOutlinedBox(ms, builder, box, this.color.withAlpha(alpha * 255.0F));
               }
            }
         }
      }
   }
}
