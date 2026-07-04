package com.isusdlc.utility.anticheat;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class DamageCompensator {
   private static DamageCompensator instance;
   private final Map<Entity, DamageCompensator.DamageTracker> damageTrackers = new HashMap<>();

   private DamageCompensator() {
   }

   public static DamageCompensator getInstance() {
      if (instance == null) {
         instance = new DamageCompensator();
      }

      return instance;
   }

   public void resetTracking(Entity entity) {
      if (entity != null) {
         this.damageTrackers.put(entity, new DamageCompensator.DamageTracker());
      }
   }

   public void recordDamage(Entity entity, float damage) {
      DamageCompensator.DamageTracker tracker = this.damageTrackers.get(entity);
      if (tracker != null) {
         tracker.addDamage(damage);
      }
   }

   public float getTotalDamage(Entity entity) {
      DamageCompensator.DamageTracker tracker = this.damageTrackers.get(entity);
      return tracker != null ? tracker.getTotalDamage() : 0.0F;
   }

   public float calculateRealisticDamage(LivingEntity attacker, LivingEntity target) {
      if (attacker != null && target != null) {
         float baseDamage = 1.0F;
         if (!attacker.getMainHandStack().isEmpty()) {
            String itemName = attacker.getMainHandStack().getItem().toString().toLowerCase();
            if (itemName.contains("sword")) {
               baseDamage = 7.0F;
            } else if (itemName.contains("axe")) {
               baseDamage = 9.0F;
            } else if (itemName.contains("pickaxe")) {
               baseDamage = 5.0F;
            } else if (itemName.contains("shovel")) {
               baseDamage = 4.5F;
            }
         }

         if (attacker.fallDistance > 0.0F && !attacker.isOnGround()) {
            baseDamage *= 1.5F;
         }

         if (attacker.hasStatusEffect(StatusEffects.STRENGTH)) {
            int amplifier = attacker.getStatusEffect(StatusEffects.STRENGTH).getAmplifier();
            baseDamage += 3.0F * (amplifier + 1);
         }

         if (attacker.hasStatusEffect(StatusEffects.WEAKNESS)) {
            int amplifier = attacker.getStatusEffect(StatusEffects.WEAKNESS).getAmplifier();
            baseDamage -= 4.0F * (amplifier + 1);
         }

         float randomFactor = 0.8F + (float)(Math.random() * 0.4F);
         return Math.max(0.0F, baseDamage * randomFactor);
      } else {
         return 0.0F;
      }
   }

   public void cleanup() {
      this.damageTrackers.entrySet().removeIf(entry -> entry.getKey() == null || entry.getKey().isRemoved());
   }

   private static class DamageTracker {
      private float totalDamage = 0.0F;
      private long lastDamageTime = System.currentTimeMillis();

      public void addDamage(float damage) {
         this.totalDamage += damage;
         this.lastDamageTime = System.currentTimeMillis();
      }

      public float getTotalDamage() {
         return this.totalDamage;
      }

      public long getLastDamageTime() {
         return this.lastDamageTime;
      }
   }
}
