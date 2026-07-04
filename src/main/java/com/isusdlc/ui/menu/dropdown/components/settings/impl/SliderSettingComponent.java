package com.isusdlc.ui.menu.dropdown.components.settings.impl;

import com.isusdlc.framework.base.CustomComponent;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.ui.menu.dropdown.components.settings.MenuSettingComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.TextUtility;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.render.DrawUtility;
import com.isusdlc.utility.time.Timer;

public class SliderSettingComponent extends MenuSettingComponent<SliderSetting> {
   private final Animation animation = new Animation(500L, Easing.BAKEK_PAGES);
   private final Animation moving = new Animation(500L, Easing.FIGMA_EASE_IN_OUT);
   private final Timer timer = new Timer();
   private boolean drag;
   private static SliderSettingComponent current;

   public SliderSettingComponent(SliderSetting setting, CustomComponent parent) {
      super(setting, parent);
   }

   @Override
   protected void renderComponent(UIContext context) {
      float x = this.x + 9.0F;
      float y = this.y + 2.0F;
      float width = this.width - 18.0F;
      Font nameFont = Fonts.REGULAR.getFont(8.0F);
      float leftPadding = 10.0F;
      float nameHeight = Fonts.REGULAR.getFont(7.0F).height();
      float headerHeight = 19.0F;
      this.animation.update(this.setting.getCurrentValue());
      this.hoverAnimation.update(this.isHovered(context.getMouseX(), context.getMouseY()));
      context.drawRoundedRect(
         x, y + this.height - 12.0F, width, 2.0F, BorderRadius.all(0.25F), Colors.getAdditionalColor().withAlpha((255.0F - 100.0F * Interface.glass()) * 0.7F)
      );
      context.drawRoundedRect(
         x,
         y + this.height - 12.0F,
         width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()),
         2.0F,
         BorderRadius.all(0.25F),
         Colors.getAccentColor()
      );
      if (this.timer.finished(1000L)) {
         DrawUtility.updateBuffer();
         this.timer.reset();
      }

      if (Interface.showGlass()) {
         context.drawShadow(
            x + width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()) - 4.5F - 3.0F * this.moving.getValue(),
            y + this.height - 11.0F - 3.0F - 2.0F * this.moving.getValue(),
            9.0F + 6.0F * this.moving.getValue(),
            6.0F + 4.0F * this.moving.getValue(),
            10.0F,
            BorderRadius.all(3.0F + this.moving.getValue() * 2.0F),
            ColorRGBA.BLACK.withAlpha(255.0F * (0.25F + 0.2F * this.moving.getValue()) * Interface.glass())
         );
         context.drawSquircle(
            x + width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()) - 4.5F - 3.0F * this.moving.getValue(),
            y + this.height - 11.0F - 3.0F - 2.0F * this.moving.getValue(),
            9.0F + 6.0F * this.moving.getValue(),
            6.0F + 4.0F * this.moving.getValue(),
            7.0F,
            BorderRadius.all(3.0F + this.moving.getValue()),
            Colors.getLiquidGlassColor().withAlpha(255.0F * (1.0F - this.moving.getValue()) * Interface.glass())
         );
         context.drawLiquidGlass(
            x + width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()) - 4.5F - 3.0F * this.moving.getValue(),
            y + this.height - 11.0F - 3.0F - 2.0F * this.moving.getValue(),
            9.0F + 6.0F * this.moving.getValue(),
            6.0F + 4.0F * this.moving.getValue(),
            7.0F,
            BorderRadius.all(3.0F + this.moving.getValue()),
            Colors.getLiquidGlassColor().withAlpha(255.0F * this.moving.getValue() * Interface.glass()),
            true
         );
      }

      if (Interface.showMinimalizm()) {
         context.drawShadow(
            x + width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()) - 3.0F,
            y + this.height - 14.0F + this.moving.getValue(),
            6.0F,
            6.0F - this.moving.getValue() * 2.0F,
            10.0F,
            BorderRadius.all(3.0F - this.moving.getValue() * 2.0F),
            ColorRGBA.BLACK.withAlpha(63.75F * Interface.minimalizm())
         );
         context.drawRoundedRect(
            x + width * GuiUtility.getPercent(this.animation.getValue(), this.setting.getMin(), this.setting.getMax()) - 3.0F,
            y + this.height - 14.0F + this.moving.getValue(),
            6.0F,
            6.0F - this.moving.getValue() * 2.0F,
            BorderRadius.all(3.0F - this.moving.getValue() * 2.0F),
            ColorRGBA.WHITE.withAlpha(255.0F * Interface.minimalizm())
         );
      }

      String value = TextUtility.formatNumberClean(this.animation.getValue()) + this.setting.getSuffix();
      context.drawFadeoutText(
         nameFont,
         Localizator.translate(this.setting.getName()),
         this.x + leftPadding,
         y + 11.0F - nameFont.height(),
         Colors.getTextColor().withAlpha(255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue())),
         0.8F,
         1.0F,
         this.getParent().getWidth() - leftPadding - Fonts.REGULAR.getFont(7.0F).width(value) - 10.0F
      );
      context.drawRightText(
         Fonts.REGULAR.getFont(7.0F),
         value,
         x + width,
         y + 11.0F - nameHeight,
         Colors.getTextColor().withAlpha(255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue()))
      );
      if (this.isHovered(context.getMouseX(), context.getMouseY())) {
         CursorUtility.set(CursorType.HAND);
      }

      this.moving.setDuration(200L);
      this.moving.update(this.drag ? 1.0F : 0.0F);
      if (this.drag) {
         float xValue = GuiUtility.getSliderValue(this.setting.getMin(), this.setting.getMax(), x, width, context.getMouseX());
         this.setting.setCurrentValue(xValue);
         CursorUtility.set(CursorType.ARROW_HORIZONTAL);
         current = this;
      }
   }

   @Override
   public void drawSplit(UIContext context) {
      float separatorHeight = 0.5F;
      context.drawRect(this.x, this.y + this.height, this.width, separatorHeight, Colors.getTextColor().withAlpha(5.1F));
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (this.isHovered(mouseX, mouseY)) {
         this.drag = true;
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.drag = false;
      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Override
   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      if ((keyCode == 262 || keyCode == 263) && current == this) {
         current.getSetting().setCurrentValue(current.getSetting().getCurrentValue() + current.getSetting().getStep() * 0.7F * (keyCode == 262 ? 1 : -1));
      }
   }

   @Override
   public float getHeight() {
      return this.height = 29.0F;
   }
}
