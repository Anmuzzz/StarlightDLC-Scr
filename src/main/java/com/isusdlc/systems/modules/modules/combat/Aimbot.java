package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.rotations.MoveCorrection;
import com.isusdlc.utility.rotations.Rotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

@ModuleInfo(
   name = "Aimbot",
   category = ModuleCategory.COMBAT,
   desc = "Наводится на цель при использовании лука, арбалета или трезубца"
)
public class Aimbot extends BaseModule {

   private final SliderSetting range = new SliderSetting(this, "Range")
      .min(10.0F).max(100.0F).step(1.0F).currentValue(50.0F);
   private final SliderSetting speed = new SliderSetting(this, "Speed")
      .min(1.0F).max(20.0F).step(0.5F).currentValue(5.0F);
   private final BooleanSetting players = new BooleanSetting(this, "Players").enable();
   private final BooleanSetting mobs = new BooleanSetting(this, "Mobs");

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.world == null || mc.player == null) return;

      var hand = mc.player.getActiveHand();
      var stack = mc.player.getStackInHand(hand);
      boolean isBow = stack.getItem() instanceof BowItem && mc.player.isUsingItem();
      boolean isCrossbow = stack.getItem() instanceof CrossbowItem && mc.player.isUsingItem();
      boolean isTrident = stack.getItem() instanceof TridentItem && mc.player.isUsingItem();

      if (!isBow && !isCrossbow && !isTrident) return;

      double maxDist = range.getCurrentValue();
      var box = mc.player.getBoundingBox().expand(maxDist);
      var target = mc.world.getEntitiesByClass(LivingEntity.class, box,
            e -> e != mc.player && e.isAlive() && !e.isInvisible() && isValidTarget(e))
         .stream()
         .min(Comparator.comparingDouble(e -> mc.player.getEyePos().squaredDistanceTo(e.getEyePos())))
         .orElse(null);

      if (target == null) return;

      Vec3d aimPoint = target.getEyePos();
      Vec3d delta = aimPoint.subtract(mc.player.getEyePos());
      float targetYaw = (float) Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90F;
      float targetPitch = (float) (-Math.toDegrees(Math.atan2(delta.y, Math.sqrt(delta.x * delta.x + delta.z * delta.z))));

      float currentYaw = mc.player.getYaw();
      float currentPitch = mc.player.getPitch();
      float yawDelta = MathHelper.wrapDegrees(targetYaw - currentYaw);
      float pitchDelta = MathHelper.wrapDegrees(targetPitch - currentPitch);

      float s = speed.getCurrentValue();
      float moveYaw = MathHelper.clamp(yawDelta, -s, s);
      float movePitch = MathHelper.clamp(pitchDelta, -s, s);

      float resultYaw = currentYaw + moveYaw;
      float resultPitch = currentPitch + movePitch;

      Rotation rot = new Rotation(resultYaw, resultPitch);
      elegant.getInstance().getRotationHandler().rotate(rot, MoveCorrection.SILENT, 180F, 180F, 180F);
   };

   private boolean isValidTarget(LivingEntity e) {
      if (e instanceof PlayerEntity) return players.isEnabled();
      if (e instanceof MobEntity) return mobs.isEnabled();
      return false;
   }
}
