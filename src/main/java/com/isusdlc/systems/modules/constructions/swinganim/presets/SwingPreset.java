package com.isusdlc.systems.modules.constructions.swinganim.presets;

import lombok.Generated;
import com.isusdlc.systems.modules.constructions.swinganim.SwingTransformations;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import net.minecraft.util.math.Vec2f;

public final class SwingPreset {
   private final String name;
   private final Vec2f bezierStart;
   private final Vec2f bezierEnd;
   private final boolean swingBack;
   private final float speed;
   private final SwingTransformations from;
   private final SwingTransformations to;
   private final Animation hoverAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);
   private final Animation activeAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);

   @Generated
   public SwingPreset(
      String name, Vec2f bezierStart, Vec2f bezierEnd, boolean swingBack, float speed, SwingTransformations from, SwingTransformations to
   ) {
      this.name = name;
      this.bezierStart = bezierStart;
      this.bezierEnd = bezierEnd;
      this.swingBack = swingBack;
      this.speed = speed;
      this.from = from;
      this.to = to;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public Vec2f getBezierStart() {
      return this.bezierStart;
   }

   @Generated
   public Vec2f getBezierEnd() {
      return this.bezierEnd;
   }

   @Generated
   public boolean isSwingBack() {
      return this.swingBack;
   }

   @Generated
   public float getSpeed() {
      return this.speed;
   }

   @Generated
   public SwingTransformations getFrom() {
      return this.from;
   }

   @Generated
   public SwingTransformations getTo() {
      return this.to;
   }

   @Generated
   public Animation getHoverAnimation() {
      return this.hoverAnimation;
   }

   @Generated
   public Animation getActiveAnimation() {
      return this.activeAnimation;
   }
}
