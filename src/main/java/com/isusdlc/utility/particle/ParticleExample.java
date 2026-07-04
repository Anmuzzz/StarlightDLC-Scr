package com.isusdlc.utility.particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class ParticleExample {
   private final List<ParticleUtil.Particle> particles = new ArrayList<>();
   private final MinecraftClient mc = MinecraftClient.getInstance();
   private final Random random = new Random();

   public void spawnParticle(Vec3d position, ParticleUtil.ParticleType type, int color, float size) {
      Vec3d velocity = new Vec3d((this.random.nextDouble() - 0.5) * 2.0, this.random.nextDouble() * 2.0, (this.random.nextDouble() - 0.5) * 2.0);
      ParticleUtil.Particle particle = new ParticleUtil.Particle(this.mc, type, position, velocity, this.particles.size(), 0, color, size, 1.0);
      this.particles.add(particle);
   }

   public void spawnExplosion(Vec3d center, int count, ParticleUtil.ParticleType type, int color) {
      for (int i = 0; i < count; i++) {
         double angle = (Math.PI * 2) * i / count;
         double speed = 0.5 + this.random.nextDouble() * 0.5;
         Vec3d velocity = new Vec3d(Math.cos(angle) * speed, this.random.nextDouble() * speed, Math.sin(angle) * speed);
         ParticleUtil.Particle particle = new ParticleUtil.Particle(this.mc, type, center, velocity, i, 0, color, 0.3F, 1.0);
         this.particles.add(particle);
      }
   }

   public void spawnTrail(Vec3d start, Vec3d end, int density, ParticleUtil.ParticleType type, int color) {
      Vec3d direction = end.subtract(start);
      double distance = direction.length();
      Vec3d step = direction.normalize().multiply(distance / density);

      for (int i = 0; i < density; i++) {
         Vec3d position = start.add(step.multiply(i));
         Vec3d velocity = new Vec3d((this.random.nextDouble() - 0.5) * 0.1, this.random.nextDouble() * 0.1, (this.random.nextDouble() - 0.5) * 0.1);
         ParticleUtil.Particle particle = new ParticleUtil.Particle(this.mc, type, position, velocity, i, 0, color, 0.2F, 0.5);
         this.particles.add(particle);
      }
   }

   public void render(MatrixStack matrices, double deltaTime) {
      if (this.mc.player != null && this.mc.world != null) {
         Vec3d cameraPos = this.mc.gameRenderer.getCamera().getPos();
         long fadeInTime = 500L;
         long fadeOutTime = 3000L;
         ParticleUtil.renderParticles(matrices, cameraPos, this.particles, fadeInTime, fadeOutTime, deltaTime);
         this.particles.removeIf(particle -> particle.time().finished(fadeOutTime + 1000L) && particle.animation().getValue() <= 0.0);
      }
   }

   public void clear() {
      this.particles.clear();
   }

   public int getParticleCount() {
      return this.particles.size();
   }

   public void exampleHearts() {
      if (this.mc.player != null) {
         Vec3d playerPos = this.mc.player.getPos();

         for (int i = 0; i < 10; i++) {
            Vec3d offset = new Vec3d((this.random.nextDouble() - 0.5) * 2.0, this.random.nextDouble() * 2.0, (this.random.nextDouble() - 0.5) * 2.0);
            this.spawnParticle(playerPos.add(offset), ParticleUtil.ParticleType.HEART, 16716947, 0.3F);
         }
      }
   }

   public void exampleCriticalHit(Vec3d hitPosition) {
      this.spawnExplosion(hitPosition, 15, ParticleUtil.ParticleType.STAR, 16766720);
   }

   public void exampleSakuraTrail(Vec3d from, Vec3d to) {
      this.spawnTrail(from, to, 10, ParticleUtil.ParticleType.SAKURA, 16758725);
   }

   public void exampleSnowfall() {
      if (this.mc.player != null) {
         Vec3d playerPos = this.mc.player.getPos();

         for (int i = 0; i < 5; i++) {
            Vec3d spawnPos = playerPos.add((this.random.nextDouble() - 0.5) * 10.0, 5.0, (this.random.nextDouble() - 0.5) * 10.0);
            Vec3d velocity = new Vec3d((this.random.nextDouble() - 0.5) * 0.5, -1.0, (this.random.nextDouble() - 0.5) * 0.5);
            ParticleUtil.Particle particle = new ParticleUtil.Particle(
               this.mc, ParticleUtil.ParticleType.SNOW, spawnPos, velocity, this.particles.size(), 0, 16777215, 0.25F, 0.3
            );
            this.particles.add(particle);
         }
      }
   }
}
