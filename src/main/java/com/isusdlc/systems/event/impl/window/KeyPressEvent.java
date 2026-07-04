package com.isusdlc.systems.event.impl.window;

import lombok.Generated;
import com.isusdlc.systems.event.Event;

public class KeyPressEvent extends Event {
   private final int action;
   private final int key;

   @Generated
   public int getAction() {
      return this.action;
   }

   @Generated
   public int getKey() {
      return this.key;
   }

   @Generated
   public KeyPressEvent(int action, int key) {
      this.action = action;
      this.key = key;
   }
}
