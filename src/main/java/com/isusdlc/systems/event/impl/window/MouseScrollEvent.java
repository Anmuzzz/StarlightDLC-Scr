package com.isusdlc.systems.event.impl.window;

import lombok.Generated;
import com.isusdlc.systems.event.Event;

public class MouseScrollEvent extends Event {
   private final double verticalAmount;

   @Generated
   public double getVerticalAmount() {
      return this.verticalAmount;
   }

   @Generated
   public MouseScrollEvent(double verticalAmount) {
      this.verticalAmount = verticalAmount;
   }
}
