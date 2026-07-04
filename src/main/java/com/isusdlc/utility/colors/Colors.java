package com.isusdlc.utility.colors;

import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.utility.animation.types.ColorAnimation;

public final class Colors {
   public static final ColorRGBA RED = new ColorRGBA(255.0F, 0.0F, 0.0F);
   public static final ColorRGBA GREEN = new ColorRGBA(0.0F, 255.0F, 0.0F);
   public static final ColorRGBA BLUE = new ColorRGBA(0.0F, 0.0F, 255.0F);
   public static final ColorRGBA WHITE = new ColorRGBA(255.0F, 255.0F, 255.0F);
   public static final ColorRGBA BLACK = new ColorRGBA(0.0F, 0.0F, 0.0F);
   public static final ColorRGBA ACCENT = new ColorRGBA(255.0F, 200.0F, 0.0F);
   private static final long ANIMATION_DURATION = 500L;
   private static final ColorAnimation BACKGROUND_COLOR_ANIMATION = new ColorAnimation(500L);
   private static final ColorAnimation ADDITIONAL_COLOR_ANIMATION = new ColorAnimation(500L);
   private static final ColorAnimation TEXT_COLOR_ANIMATION = new ColorAnimation(500L);
   private static final ColorAnimation OUTLINE_COLOR_ANIMATION = new ColorAnimation(500L);
   private static final ColorAnimation FLAT_COLOR_ANIMATION = new ColorAnimation(500L);
   private static final ColorAnimation ACCENT_COLOR_ANIMATION = new ColorAnimation(500L);

   private static Theme getTheme() {
      return elegant.getInstance().getThemeManager().getCurrentTheme();
   }

   public static ColorRGBA getBackgroundColor() {
      return getAnimatedColor(BACKGROUND_COLOR_ANIMATION, getTheme().getBackgroundColor());
   }

   public static ColorRGBA getAdditionalColor() {
      return getAnimatedColor(ADDITIONAL_COLOR_ANIMATION, getTheme().getAdditionalColor());
   }

   public static ColorRGBA getTextColor() {
      return getAnimatedColor(TEXT_COLOR_ANIMATION, getTheme().getTextColor());
   }

   public static ColorRGBA getOutlineColor() {
      return getAnimatedColor(OUTLINE_COLOR_ANIMATION, getTheme().getOutlineColor());
   }

   public static ColorRGBA getFlatColor() {
      return getAnimatedColor(FLAT_COLOR_ANIMATION, getTheme().getFlatColor());
   }

   public static ColorRGBA getSeparatorColor() {
      return ColorRGBA.BLACK.withAlpha(255.0F * (getTheme() == Theme.DARK ? 0.08F : 0.05F));
   }

   public static ColorRGBA getAccentColor() {
      Interface interfaceModule = elegant.getInstance().getModuleManager().getModule(Interface.class);
      if (interfaceModule == null) {
         return ACCENT;
      } else if (interfaceModule.getMinimalism().isSelected()) {
         ColorRGBA color = interfaceModule.getMinimalismMainColor().getColor();
         return getAnimatedColor(ACCENT_COLOR_ANIMATION, color);
      } else if (interfaceModule.getLiquidGlass().isSelected()) {
         ColorRGBA color = interfaceModule.getLiquidClientColor().getColor();
         return getAnimatedColor(ACCENT_COLOR_ANIMATION, color);
      } else {
         return ACCENT;
      }
   }

   public static ColorRGBA getHudTextColor() {
      return getTextColor();
   }

   public static ColorRGBA getLiquidGlassColor() {
      Interface interfaceModule = elegant.getInstance().getModuleManager().getModule(Interface.class);
      return interfaceModule != null && interfaceModule.getLiquidGlassColor() != null ? interfaceModule.getLiquidGlassColor().getColor() : ColorRGBA.WHITE;
   }

   private static ColorRGBA getAnimatedColor(ColorAnimation animation, ColorRGBA color) {
      animation.update(color);
      return animation.getColor();
   }

   @Generated
   private Colors() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
