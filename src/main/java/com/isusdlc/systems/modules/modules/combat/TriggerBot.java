package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.hud.legacy.StopWatch;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.Random;

@ModuleInfo(
   name = "Trigger",
   category = ModuleCategory.COMBAT,
   desc = "auto attacks when crosshair is on entity"
)
public class TriggerBot extends BaseModule {

   private final BooleanSetting armoredPlayers = new BooleanSetting(this, "Armored Players").enable();
   private final BooleanSetting unarmoredPlayers = new BooleanSetting(this, "Unarmored Players").enable();
   private final BooleanSetting invisiblePlayers = new BooleanSetting(this, "Invisible Players");
   private final BooleanSetting hostileMobs = new BooleanSetting(this, "Hostile Mobs");
   private final BooleanSetting animals = new BooleanSetting(this, "Animals");
   private final BooleanSetting dynamicCooldown = new BooleanSetting(this, "Dynamic cooldown");
   private final BooleanSetting unpressShield = new BooleanSetting(this, "Un press shield");
   private final BooleanSetting checkUse = new BooleanSetting(this, "Check use");
   private final ModeSetting criticalMode = new ModeSetting(this, "Critical mode");
   private final ModeSetting.Value critNone = new ModeSetting.Value(this.criticalMode, "None").select();
   private final ModeSetting.Value critOnly = new ModeSetting.Value(this.criticalMode, "Only critical");
   private final ModeSetting.Value critAdaptive = new ModeSetting.Value(this.criticalMode, "Adaptive");
   private final ModeSetting versionMode = new ModeSetting(this, "Version");
   private final ModeSetting.Value v1_8 = new ModeSetting.Value(this.versionMode, "1.8");
   private final ModeSetting.Value v1_9 = new ModeSetting.Value(this.versionMode, "1.9").select();

   private static final int[] CLICK_INTERVALS = {
      12, 13, 11, 12, 12, 12, 12, 12, 12, 12, 12, 13, 12, 12, 12, 12, 12, 10,
      12, 13, 13, 12, 12, 11, 13, 11, 12, 12, 12, 11, 12, 11, 13, 13, 13, 11, 12, 11, 12, 12, 11, 12, 14, 13,
      12, 12, 11, 12, 11, 12, 13, 12, 12, 11, 13, 12, 12, 11, 13, 12, 12, 13, 12, 12, 12, 12, 11, 13, 11, 11,
      13, 13, 12, 11, 13, 11, 12, 11, 13
   };
   private int clickIndex;
   private boolean alternateTickDelay;
   private long lastClickTime = System.currentTimeMillis();
   private final StopWatch attackWatch = new StopWatch();
   private final Random random = new Random();
   private int airTicks;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.world == null || mc.player == null) return;

      airTicks++;
      if (mc.player.isOnGround()) airTicks = 0;

      if (mc.crosshairTarget == null || mc.crosshairTarget.getType() != HitResult.Type.ENTITY) return;
      Entity entity = ((EntityHitResult) mc.crosshairTarget).getEntity();
      if (!(entity instanceof LivingEntity living) || !isValidTarget(living)) return;

      attack(living);
   };

   private boolean isValidTarget(LivingEntity entity) {
      if (!entity.isAlive() || entity == mc.player) return false;

      if (entity instanceof PlayerEntity player) {
         boolean hasArmor = hasAnyArmor(player);
         boolean isInvisible = player.isInvisible();
         if (hasArmor) return armoredPlayers.isEnabled();
         return isInvisible ? invisiblePlayers.isEnabled() : unarmoredPlayers.isEnabled();
      }
      if (entity instanceof HostileEntity) return hostileMobs.isEnabled();
      if (entity instanceof AnimalEntity) return animals.isEnabled();
      return false;
   }

   private boolean hasAnyArmor(PlayerEntity player) {
      for (ItemStack item : player.getArmorItems()) {
         if (!item.isEmpty()) return true;
      }
      return false;
   }

   private void attack(LivingEntity target) {
      if (!canAttack()) return;

      mc.interactionManager.attackEntity(mc.player, target);
      mc.player.resetLastAttackedTicks();
      mc.player.swingHand(mc.player.getActiveHand());
      airTicks = 0;
      lastClickTime = System.currentTimeMillis();
      attackWatch.reset();
   }

   private boolean canAttack() {
      if (isCooldownBlocked()) return false;
      if (isPlayerUsingBlocked()) return false;
      if (critOnly.isSelected() && !hasMovementRestrictions()) return isPlayerInCriticalState();
      if (critAdaptive.isSelected() && !hasMovementRestrictions()) {
         return mc.player.isOnGround() ? true : isPlayerInCriticalState();
      }
      return true;
   }

   private boolean isCooldownBlocked() {
      if (v1_8.isSelected()) {
         if (dynamicCooldown.isEnabled()) {
            int tickDelay = alternateTickDelay ? 1 : 2;
            alternateTickDelay = !alternateTickDelay;
            return lastClickPassed() < (tickDelay * 50L);
         }
         return false;
      }

      long delay = dynamicCooldown.isEnabled() ? calculateAverageCooldown() * 45L : 50L;
      float cooldownProgress = mc.player.getAttackCooldownProgress(0.5F);
      return lastClickPassed() < delay || cooldownProgress < 0.9F;
   }

   private long lastClickPassed() {
      return System.currentTimeMillis() - lastClickTime;
   }

   private int calculateAverageCooldown() {
      if (clickIndex >= CLICK_INTERVALS.length) clickIndex = 0;
      return CLICK_INTERVALS[clickIndex++];
   }

   private boolean isPlayerUsingBlocked() {
      if (!checkUse.isEnabled()) return false;
      if (mc.player.isUsingItem()) {
         if (mc.player.isBlocking() && unpressShield.isEnabled()) {
            mc.interactionManager.stopUsingItem(mc.player);
            return false;
         }
         return true;
      }
      return false;
   }

   private boolean hasMovementRestrictions() {
      return mc.player.hasStatusEffect(StatusEffects.BLINDNESS)
         || mc.player.hasStatusEffect(StatusEffects.LEVITATION)
         || mc.player.isSubmergedInWater()
         || mc.player.isInLava()
         || mc.player.isClimbing()
         || mc.player.getAbilities().flying;
   }

   private boolean isPlayerInCriticalState() {
      double randomThreshold = 0.05 + random.nextDouble() * 0.05;
      return !mc.player.isOnGround()
         && mc.player.fallDistance > randomThreshold
         && !mc.player.isClimbing()
         && !mc.player.isTouchingWater()
         && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS);
   }
}
