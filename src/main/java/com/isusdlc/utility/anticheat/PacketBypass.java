package com.isusdlc.utility.anticheat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class PacketBypass {
   public static boolean canAttackSafely(Entity target) {
      if (target == null) {
         return false;
      } else {
         if (target instanceof PlayerEntity player) {
            if (player.isInvulnerable()) {
               return false;
            }

            if (player.hurtTime > 0) {
               return false;
            }

            if (player.isDead()) {
               return false;
            }
         }

         return true;
      }
   }

   public static long getSafeAttackDelay() {
      return 50L + (long)(Math.random() * 100.0);
   }

   public static boolean isAttackTimingSafe(long lastAttackTime) {
      long currentTime = System.currentTimeMillis();
      return currentTime - lastAttackTime >= getSafeAttackDelay();
   }
}
