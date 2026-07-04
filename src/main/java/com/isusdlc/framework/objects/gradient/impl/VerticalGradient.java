package com.isusdlc.framework.objects.gradient.impl;

import com.isusdlc.framework.objects.gradient.Gradient;
import com.isusdlc.utility.colors.ColorRGBA;

public class VerticalGradient extends Gradient {
   public VerticalGradient(ColorRGBA startColor, ColorRGBA endColor) {
      super(startColor, endColor, startColor, endColor);
   }

   public VerticalGradient rotate() {
      return new VerticalGradient(this.bottomRightColor, this.topLeftColor);
   }
}
