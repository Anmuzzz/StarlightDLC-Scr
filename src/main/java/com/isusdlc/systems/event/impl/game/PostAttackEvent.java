package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.Event;
import net.minecraft.entity.Entity;

public class PostAttackEvent extends Event {
   private final Entity entity;

   public PostAttackEvent(Entity entity) {
      this.entity = entity;
   }

   @Generated
   public Entity getEntity() {
      return this.entity;
   }
}
