package com.isusdlc.ui.menu.dropdown.components.settings.impl;

import com.isusdlc.framework.base.CustomComponent;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.setting.settings.RangeSetting;
import com.isusdlc.ui.menu.dropdown.components.settings.MenuSettingComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.TextUtility;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;

public class RangeSettingComponent extends MenuSettingComponent<RangeSetting> {
   private final Animation xAnim = new Animation(500L, Easing.BAKEK_PAGES);
   private final Animation widthAnim = new Animation(500L, Easing.BAKEK_PAGES);
   private boolean dragFirst;
   private boolean dragSecond;

   public RangeSettingComponent(RangeSetting setting, CustomComponent parent) {
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
      float first = this.setting.getFirstValue();
      float second = this.setting.getSecondValue();
      if (first >= second) {
         first = this.setting.getSecondValue();
         second = this.setting.getFirstValue();
      }

      this.xAnim.update(first);
      this.widthAnim.update(second);
      this.hoverAnimation.update(this.isHovered(context.getMouseX(), context.getMouseY()));
      context.drawRoundedRect(x, y + this.height - 12.0F, width, 2.0F, BorderRadius.all(0.25F), Colors.getAdditionalColor().withAlpha(178.5F));
      context.drawRoundedRect(
         x + width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax()),
         y + this.height - 12.0F,
         width * GuiUtility.getPercent(this.widthAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            - width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax()),
         2.0F,
         BorderRadius.all(0.25F),
         Colors.getAccentColor()
      );
      context.drawShadow(
         x + width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax()) - 3.0F,
         y + this.height - 14.0F,
         6.0F,
         6.0F,
         10.0F,
         BorderRadius.all(3.0F),
         ColorRGBA.BLACK.withAlpha(63.75F)
      );
      context.drawRoundedRect(
         x + width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax()) - 3.0F,
         y + this.height - 14.0F,
         6.0F,
         6.0F,
         BorderRadius.all(3.0F),
         ColorRGBA.WHITE
      );
      context.drawShadow(
         x
            + width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            + width * GuiUtility.getPercent(this.widthAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            - width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            - 3.0F,
         y + this.height - 14.0F,
         6.0F,
         6.0F,
         10.0F,
         BorderRadius.all(3.0F),
         ColorRGBA.BLACK.withAlpha(63.75F)
      );
      context.drawRoundedRect(
         x
            + width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            + width * GuiUtility.getPercent(this.widthAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            - width * GuiUtility.getPercent(this.xAnim.getValue(), this.setting.getMin(), this.setting.getMax())
            - 3.0F,
         y + this.height - 14.0F,
         6.0F,
         6.0F,
         BorderRadius.all(3.0F),
         ColorRGBA.WHITE
      );
      String value = String.format("от %s до %s", TextUtility.formatNumber(this.xAnim.getValue()), TextUtility.formatNumber(this.widthAnim.getValue()));
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

      if (this.dragFirst) {
         float xValue = GuiUtility.getSliderValue(this.setting.getMin(), this.setting.getMax(), x, width, context.getMouseX());
         this.setting.setFirstValue(xValue);
         CursorUtility.set(CursorType.ARROW_HORIZONTAL);
      } else if (this.dragSecond) {
         float xValue = GuiUtility.getSliderValue(this.setting.getMin(), this.setting.getMax(), x, width, context.getMouseX());
         this.setting.setSecondValue(xValue);
         CursorUtility.set(CursorType.ARROW_HORIZONTAL);
      }
   }

   @Override
   public void drawSplit(UIContext context) {
      float separatorHeight = 0.5F;
      context.drawRect(this.x, this.y + this.height, this.width, separatorHeight, Colors.getTextColor().withAlpha(5.1F));
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      float x = this.x + 9.0F;
      float width = this.width - 18.0F;
      if (this.isHovered(mouseX, mouseY)) {
         float firstDist = (float)Math.abs(
            mouseX - (x + width * GuiUtility.getPercent(this.setting.getFirstValue(), this.setting.getMin(), this.setting.getMax()))
         );
         float secondDist = (float)Math.abs(
            mouseX - (x + width * GuiUtility.getPercent(this.setting.getSecondValue(), this.setting.getMin(), this.setting.getMax()))
         );
         if (firstDist < secondDist) {
            this.dragFirst = true;
         } else {
            this.dragSecond = true;
         }
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.dragFirst = false;
      this.dragSecond = false;
      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Override
   public float getHeight() {
      return this.height = 29.0F;
   }
}
