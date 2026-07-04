package com.isusdlc.systems.modules.modules.visuals;

import com.mojang.blaze3d.platform.GlStateManager.DstFactor;
import com.mojang.blaze3d.platform.GlStateManager.SrcFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.event.impl.render.Render3DEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.render.VertexFormat.DrawMode;
import org.joml.Matrix4f;

@ModuleInfo(
   name = "Hit Particles",
   category = ModuleCategory.VISUALS,
   desc = "modules.descriptions.hit_particles"
)
public class HitParticles extends BaseModule implements IMinecraft {
   private final ModeSetting particleType = new ModeSetting(this, "modules.settings.hit_particles.particle_type");
   private final ModeSetting.Value star = new ModeSetting.Value(this.particleType, "Star").select();
   private final ModeSetting.Value glow = new ModeSetting.Value(this.particleType, "Glow");
   private final ModeSetting.Value heart = new ModeSetting.Value(this.particleType, "Heart");
   private final ModeSetting.Value dollar = new ModeSetting.Value(this.particleType, "Dollar");
   private final ModeSetting.Value starNew = new ModeSetting.Value(this.particleType, "Star New");
   private final ModeSetting.Value random = new ModeSetting.Value(this.particleType, "Random");
   private final ModeSetting colorMode = new ModeSetting(this, "modules.settings.hit_particles.color_mode");
   private final ModeSetting.Value themeColor = new ModeSetting.Value(this.colorMode, "Theme").select();
   private final ModeSetting.Value rainbowColor = new ModeSetting.Value(this.colorMode, "Rainbow");
   private final SliderSetting speed = new SliderSetting(this, "modules.settings.hit_particles.speed").min(0.1F).max(3.0F).step(0.1F).currentValue(1.5F);
   private final SliderSetting size = new SliderSetting(this, "modules.settings.hit_particles.size").min(0.0F).max(3.0F).step(0.1F).currentValue(0.8F);
   private final SliderSetting count = new SliderSetting(this, "modules.settings.hit_particles.count").min(5.0F).max(50.0F).step(1.0F).currentValue(30.0F);
   private final List<HitParticles.Particle> particles = new ArrayList<>();
   private final Random rand = new Random();
   private long lastUpdateTime = System.nanoTime();
   private final EventListener<AttackEvent> onAttack = event -> {
      Entity target = event.getEntity();
      if (target != null) {
         for (int i = 0; i < this.count.getCurrentValue(); i++) {
            Identifier texture = this.getParticleTexture();
            Vec3d position = new Vec3d(
               target.getX() + this.randomRange(-0.4, 0.4),
               target.getY() + this.randomRange(0.0, target.getHeight()),
               target.getZ() + this.randomRange(-0.4, 0.4)
            );
            Vec3d velocity = new Vec3d(this.randomRange(-1.35, 1.35), this.randomRange(-1.25, 1.25), this.randomRange(-1.35, 1.35));
            ColorRGBA color = this.getThemeColor(i);
            this.particles
               .add(new HitParticles.Particle(texture, position, velocity, color, 0.15F + this.size.getCurrentValue() * 0.3F, this.speed.getCurrentValue()));
         }
      }
   };
   private final EventListener<ClientPlayerTickEvent> onTick = event -> this.particles.removeIf(particle -> particle.time > 1000L);
   private final EventListener<Render3DEvent> on3DRender = event -> {
      if (!this.particles.isEmpty()) {
         long now = System.nanoTime();
         double deltaTime = (now - this.lastUpdateTime) / 1.0E9;
         this.lastUpdateTime = now;
         MatrixStack matrix = event.getMatrices();
         this.setupRenderState();
         Camera camera = mc.gameRenderer.getCamera();
         Vec3d cameraPos = camera.getPos();

         for (HitParticles.Particle particle : this.particles) {
            particle.update(deltaTime);
            this.renderParticle(matrix, particle, cameraPos);
         }

         this.resetRenderState();
      }
   };

   @Override
   public void onEnable() {
      super.onEnable();
      this.particles.clear();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.particles.clear();
   }

   private void setupRenderState() {
      RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableBlend();
      RenderSystem.blendFuncSeparate(SrcFactor.SRC_ALPHA, DstFactor.ONE, SrcFactor.ONE, DstFactor.ONE);
      RenderSystem.enableDepthTest();
      RenderSystem.disableCull();
      RenderSystem.depthMask(false);
   }

   private void resetRenderState() {
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
   }

   private void renderParticle(MatrixStack matrix, HitParticles.Particle particle, Vec3d cameraPos) {
      matrix.push();
      Vec3d pos = particle.position.subtract(cameraPos);
      matrix.translate(pos.x, pos.y, pos.z);
      matrix.multiply(mc.getEntityRenderDispatcher().getRotation());
      RenderSystem.setShaderTexture(0, particle.texture);
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder buffer = tessellator.begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
      Matrix4f m = matrix.peek().getPositionMatrix();
      float halfSize = particle.size / 2.0F;
      this.drawQuad(buffer, m, halfSize, particle.color.withAlpha((int)(particle.alpha * 255.0F)).getRGB());
      BufferRenderer.drawWithGlobalProgram(buffer.end());
      matrix.pop();
   }

   private void drawQuad(BufferBuilder buffer, Matrix4f matrix, float size, int color) {
      buffer.vertex(matrix, -size, -size, 0.0F).texture(0.0F, 0.0F).color(color);
      buffer.vertex(matrix, size, -size, 0.0F).texture(1.0F, 0.0F).color(color);
      buffer.vertex(matrix, size, size, 0.0F).texture(1.0F, 1.0F).color(color);
      buffer.vertex(matrix, -size, size, 0.0F).texture(0.0F, 1.0F).color(color);
   }

   private Identifier getParticleTexture() {
      if (this.random.isSelected()) {
         Identifier[] textures = new Identifier[]{
            elegant.id("textures/world/particles/dollar.png"),
            elegant.id("textures/world/particles/heart.png"),
            elegant.id("textures/world/particles/star.png"),
            elegant.id("textures/world/particles/star.png")
         };
         return textures[this.rand.nextInt(textures.length)];
      } else if (this.dollar.isSelected()) {
         return elegant.id("textures/world/particles/dollar.png");
      } else if (this.heart.isSelected()) {
         return elegant.id("textures/world/particles/heart.png");
      } else if (this.starNew.isSelected()) {
         return elegant.id("textures/world/particles/star.png");
      } else {
         return this.glow.isSelected() ? elegant.id("textures/bloom.png") : elegant.id("textures/world/particles/star.png");
      }
   }

   private ColorRGBA getThemeColor(int index) {
      if (this.rainbowColor.isSelected()) {
         float hue = (float)((System.currentTimeMillis() + index * 100) % 3000L) / 3000.0F * 360.0F;
         return ColorRGBA.fromHSB(hue, 0.7F, 1.0F);
      } else {
         ColorRGBA themeCol = elegant.getInstance().getThemeManager().getCurrentTheme().getAdditionalColor();
         return themeCol != null ? themeCol : new ColorRGBA(138.0F, 43.0F, 226.0F);
      }
   }

   private double randomRange(double min, double max) {
      return min + (max - min) * this.rand.nextDouble();
   }

   private class Particle {
      private final Identifier texture;
      private Vec3d position;
      private Vec3d velocity;
      private final ColorRGBA color;
      private final float size;
      private final float speedMultiplier;
      private long time = 0L;
      private float alpha = 1.0F;

      public Particle(Identifier texture, Vec3d position, Vec3d velocity, ColorRGBA color, float size, float speedMultiplier) {
         this.texture = texture;
         this.position = position;
         this.velocity = velocity.multiply(0.05);
         this.color = color;
         this.size = size;
         this.speedMultiplier = speedMultiplier;
      }

      public void update(double deltaTime) {
         this.velocity = this.velocity.multiply(Math.pow(0.999, deltaTime * 60.0));
         this.position = this.position.add(this.velocity.multiply(deltaTime * 60.0 * this.speedMultiplier));
         this.time += (long)(deltaTime * 1000.0);
         if (this.time > 600L) {
            this.alpha = 1.0F - (float)(this.time - 600L) / 400.0F;
            this.alpha = Math.max(0.0F, Math.min(1.0F, this.alpha));
         }
      }
   }
}
