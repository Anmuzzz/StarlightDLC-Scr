package com.isusdlc.systems.event.impl.render;

import lombok.Generated;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.event.Event;

public class PreHudRenderEvent extends Event {
   private final CustomDrawContext context;
   private final float tickDelta;

   @Generated
   public CustomDrawContext getContext() {
      return this.context;
   }

   @Generated
   public float getTickDelta() {
      return this.tickDelta;
   }

   @Generated
   public PreHudRenderEvent(CustomDrawContext context, float tickDelta) {
      this.context = context;
      this.tickDelta = tickDelta;
   }
}
