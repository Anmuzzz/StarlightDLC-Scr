package com.isusdlc.systems.modules.modules.movement;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.setting.settings.ModeSetting;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.ModeSetting;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Strafe",
   category = ModuleCategory.MOVEMENT,
   desc = "Быстрое перемещение"
)
public class Strafe extends BaseModule {
   private final ModeSetting mode = new ModeSetting(this, "Mode");
   private final ModeSetting.Value metaHvH = new ModeSetting.Value(this.mode, "MetaHvH").select();

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null || mc.world == null) return;

      if (this.mode.is(this.metaHvH)) {
         if (mc.player.isGliding() || mc.player.isTouchingWater() || mc.player.isSwimming()) return;
         if (mc.player.forwardSpeed == 0 && mc.player.sidewaysSpeed == 0) return;

         float motion = 0.19F;
         var speedEffect = mc.player.getStatusEffect(StatusEffects.SPEED);
         if (speedEffect != null) {
            int amplifier = speedEffect.getAmplifier();
            switch (amplifier) {
               case 0 -> motion = 0.25F;
               case 1 -> motion = 0.37F;
               case 2 -> motion = 0.46F;
               case 3 -> motion = 0.70F;
               default -> motion = 0.75F + (amplifier - 3) * 0.05F;
            }
         }

         if (mc.options.jumpKey.isPressed()) {
            motion += 0.1F;
         }

         float yaw = mc.player.getYaw() * 0.017453292F;
         float forward = mc.player.input.movementForward;
         float strafe = mc.player.input.movementSideways;
         Vec3d vel = mc.player.getVelocity();

         double sin = -Math.sin(yaw);
         double cos = Math.cos(yaw);
         Vec3d dir = new Vec3d(
            forward * sin + strafe * cos,
            0,
            forward * cos - strafe * sin
         ).normalize();

         mc.player.setVelocity(dir.x * motion, vel.y, dir.z * motion);
      }
   };
}
