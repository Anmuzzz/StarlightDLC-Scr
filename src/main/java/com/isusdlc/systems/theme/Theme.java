package com.isusdlc.systems.theme;

import lombok.Generated;
import com.isusdlc.utility.colors.ColorRGBA;

public enum Theme {
   DARK(
      new ColorRGBA(255.0F, 255.0F, 255.0F),
      new ColorRGBA(5.0F, 8.0F, 25.0F),
      new ColorRGBA(15.0F, 18.0F, 40.0F),
      new ColorRGBA(30.0F, 35.0F, 60.0F),
      ColorRGBA.BLACK
   ),
   LIGHT(
      new ColorRGBA(54.0F, 49.0F, 55.0F),
      new ColorRGBA(255.0F, 255.0F, 255.0F),
      new ColorRGBA(189.0F, 189.0F, 189.0F),
      new ColorRGBA(32.0F, 32.0F, 32.0F),
      ColorRGBA.WHITE
   );

   private final ColorRGBA textColor;
   private final ColorRGBA backgroundColor;
   private final ColorRGBA additionalColor;
   private final ColorRGBA outlineColor;
   private final ColorRGBA flatColor;

   @Generated
   public ColorRGBA getTextColor() {
      return this.textColor;
   }

   @Generated
   public ColorRGBA getBackgroundColor() {
      return this.backgroundColor;
   }

   @Generated
   public ColorRGBA getAdditionalColor() {
      return this.additionalColor;
   }

   @Generated
   public ColorRGBA getOutlineColor() {
      return this.outlineColor;
   }

   @Generated
   public ColorRGBA getFlatColor() {
      return this.flatColor;
   }

   @Generated
   private Theme(
      final ColorRGBA textColor, final ColorRGBA backgroundColor, final ColorRGBA additionalColor, final ColorRGBA outlineColor, final ColorRGBA flatColor
   ) {
      this.textColor = textColor;
      this.backgroundColor = backgroundColor;
      this.additionalColor = additionalColor;
      this.outlineColor = outlineColor;
      this.flatColor = flatColor;
   }
}
