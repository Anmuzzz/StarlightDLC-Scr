package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.EventCancellable;
import net.minecraft.entity.Entity;

public class AttackEvent extends EventCancellable {
   private final Entity entity;

   @Generated
   public Entity getEntity() {
      return this.entity;
   }

   @Generated
   public AttackEvent(Entity entity) {
      this.entity = entity;
   }
}
