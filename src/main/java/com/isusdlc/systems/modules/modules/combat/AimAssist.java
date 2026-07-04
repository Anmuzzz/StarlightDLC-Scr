package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

@ModuleInfo(
   name = "Aim Assist",
   category = ModuleCategory.COMBAT,
   desc = "Плавно подводит прицел к цели"
)
public class AimAssist extends BaseModule {

   private final SliderSetting speed = new SliderSetting(this, "Speed")
      .min(0.5F).max(10.0F).step(0.5F).currentValue(3.0F);
   private final SliderSetting fov = new SliderSetting(this, "FOV")
      .min(30.0F).max(180.0F).step(1.0F).currentValue(90.0F);
   private final SliderSetting distance = new SliderSetting(this, "Distance")
      .min(5.0F).max(100.0F).step(1.0F).currentValue(30.0F);
   private final SliderSetting shake = new SliderSetting(this, "Shake")
      .min(0.0F).max(2.0F).step(0.1F).currentValue(0.0F);

   private final BooleanSetting targetPlayers = new BooleanSetting(this, "Players").enable();
   private final BooleanSetting targetMobs = new BooleanSetting(this, "Mobs");
   private final BooleanSetting targetAnimals = new BooleanSetting(this, "Animals");
   private final BooleanSetting targetArmorStands = new BooleanSetting(this, "Armor Stands");

   private float currentYaw;
   private float currentPitch;
   private float shakePhase;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.world == null || mc.player == null) return;
      if (mc.currentScreen != null) return;

      if (shake.getCurrentValue() > 0.0F) {
         shakePhase += 0.08F;
      }

      Entity target = findTarget();
      if (target == null) {
         currentYaw = 0.0F;
         currentPitch = 0.0F;
         return;
      }

      aimAtTarget(target);
   };

   private Entity findTarget() {
      Box box = mc.player.getBoundingBox().expand(distance.getCurrentValue());
      List<Entity> entities = mc.world.getOtherEntities(mc.player, box);
      Entity closest = null;
      double closestFov = Double.MAX_VALUE;

      for (Entity e : entities) {
         if (!isValidTarget(e)) continue;

         double fovTo = getFovToEntity(e);
         if (fovTo > fov.getCurrentValue()) continue;

         double dist = mc.player.distanceTo(e);
         if (dist < closestFov) {
            closestFov = dist;
            closest = e;
         }
      }

      return closest;
   }

   private boolean isValidTarget(Entity entity) {
      if (!(entity instanceof LivingEntity living)) return false;
      if (entity == mc.player) return false;
      if (!living.isAlive()) return false;

      if (entity instanceof PlayerEntity player) {
         return targetPlayers.isEnabled() && !player.isSpectator();
      }
      if (entity instanceof MobEntity) return targetMobs.isEnabled();
      if (entity instanceof AnimalEntity) return targetAnimals.isEnabled();
      if (entity instanceof ArmorStandEntity) return targetArmorStands.isEnabled();

      return false;
   }

   private double getFovToEntity(Entity entity) {
      Vec3d targetPos = entity.getPos().add(0.0, entity.getEyeHeight(entity.getPose()) * 0.85, 0.0);
      Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());
      Vec3d toTarget = targetPos.subtract(eyePos).normalize();
      Vec3d lookVec = Vec3d.fromPolar(mc.player.getPitch(), mc.player.getYaw()).normalize();
      double dot = lookVec.dotProduct(toTarget);
      return Math.toDegrees(Math.acos(MathHelper.clamp(dot, -1.0, 1.0)));
   }

   private void aimAtTarget(Entity target) {
      Vec3d targetPos = target.getPos().add(0.0, target.getEyeHeight(target.getPose()) * 0.85, 0.0);
      Vec3d eyePos = new Vec3d(mc.player.getX(), mc.player.getEyeY(), mc.player.getZ());
      Vec3d delta = targetPos.subtract(eyePos);
      double horizDist = Math.sqrt(delta.x * delta.x + delta.z * delta.z);

      float targetYaw = (float) (Math.atan2(delta.z, delta.x) * (180.0 / Math.PI)) - 90.0F;
      float targetPitch = (float) (-(Math.atan2(delta.y, horizDist) * (180.0 / Math.PI)));

      float playerYaw = mc.player.getYaw();
      float playerPitch = mc.player.getPitch();

      if (currentYaw == 0.0F && currentPitch == 0.0F) {
         currentYaw = playerYaw;
         currentPitch = playerPitch;
      }

      float yawDelta = MathHelper.wrapDegrees(targetYaw - currentYaw);
      float pitchDelta = targetPitch - currentPitch;

      float yawStep = yawDelta * speed.getCurrentValue() * 0.03F;
      float pitchStep = pitchDelta * speed.getCurrentValue() * 0.03F;

      yawStep = MathHelper.clamp(yawStep, -Math.abs(yawDelta), Math.abs(yawDelta));
      pitchStep = MathHelper.clamp(pitchStep, -Math.abs(pitchDelta), Math.abs(pitchDelta));

      currentYaw += yawStep;
      currentPitch = MathHelper.clamp(currentPitch + pitchStep, -90.0F, 90.0F);

      if (shake.getCurrentValue() > 0.0F) {
         float shakeYaw = (float) (Math.sin(shakePhase * 1.3) * shake.getCurrentValue() * 0.3);
         float shakePitch = (float) (Math.cos(shakePhase * 1.7) * shake.getCurrentValue() * 0.3);
         currentYaw = MathHelper.wrapDegrees(currentYaw + shakeYaw);
         currentPitch = MathHelper.clamp(currentPitch + shakePitch, -90.0F, 90.0F);
      }

      mc.player.setYaw(currentYaw);
      mc.player.setPitch(currentPitch);
   }
}
