package com.isusdlc.ui.hud;

import lombok.Generated;

public class GridLine {
   private final Type type;
   private final float pos;
   private boolean active;

   @Generated
   public Type getType() {
      return this.type;
   }

   @Generated
   public float getPos() {
      return this.pos;
   }

   @Generated
   public boolean isActive() {
      return this.active;
   }

   @Generated
   public GridLine(Type type, float pos) {
      this.type = type;
      this.pos = pos;
   }

   @Generated
   public void setActive(boolean active) {
      this.active = active;
   }

   public static enum Type {
      HORIZONTAL,
      VERTICAL;
   }
}
