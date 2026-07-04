package com.isusdlc.systems.modules.modules.movement;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.setting.settings.SliderSetting;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(
   name = "Elytra Motion",
   category = ModuleCategory.MOVEMENT,
   desc = "Замораживает игрока при полёте на элитрах и таргете"
)
public class ElytraMotion extends BaseModule {
   private final SliderSetting distance = new SliderSetting(this, "Distance")
      .min(0.1F).max(5.0F).step(0.1F).currentValue(3.0F);

   private Vec3d freezePosition = Vec3d.ZERO;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null) return;

      if (mc.player.isGliding()) {
         freezePosition = mc.player.getPos();
      }

      if (shouldFreeze()) {
         mc.player.setPosition(freezePosition);
         mc.player.setVelocity(Vec3d.ZERO);
      }
   };

   private boolean shouldFreeze() {
      if (mc.player == null) return false;
      if (!mc.player.isGliding()) return false;

      var world = mc.world;
      if (world == null) return false;

      LivingEntity target = null;
      double nearestDist = this.distance.getCurrentValue();
      for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, mc.player.getBoundingBox().expand(nearestDist), e -> e != mc.player && e.isAlive())) {
         double dist = mc.player.distanceTo(entity);
         if (dist < nearestDist) {
            nearestDist = dist;
            target = entity;
         }
      }

      return target != null;
   }

   @Override
   public void onDisable() {
      freezePosition = Vec3d.ZERO;
   }
}
