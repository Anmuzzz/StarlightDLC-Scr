package com.isusdlc.framework.objects.gradient.impl;

import com.isusdlc.framework.objects.gradient.Gradient;
import com.isusdlc.utility.colors.ColorRGBA;

class DiagonalGradient extends Gradient {
   public DiagonalGradient(ColorRGBA startColor, ColorRGBA endColor) {
      super(startColor, endColor, endColor, startColor);
   }

   public DiagonalGradient rotate() {
      return new DiagonalGradient(this.topRightColor, this.bottomLeftColor);
   }
}
