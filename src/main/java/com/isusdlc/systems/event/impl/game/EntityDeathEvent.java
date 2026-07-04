package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.Event;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class EntityDeathEvent extends Event {
   private final LivingEntity entity;
   private final DamageSource source;

   public EntityDeathEvent(LivingEntity entity, DamageSource source) {
      this.entity = entity;
      this.source = source;
   }

   @Nullable
   public LivingEntity getKillerEntity() {
      return this.entity.getPrimeAdversary();
   }

   @Generated
   public LivingEntity getEntity() {
      return this.entity;
   }

   @Generated
   public DamageSource getSource() {
      return this.source;
   }
}
