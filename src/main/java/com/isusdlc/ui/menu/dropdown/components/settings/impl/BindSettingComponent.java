package com.isusdlc.ui.menu.dropdown.components.settings.impl;

import lombok.Generated;
import com.isusdlc.framework.base.CustomComponent;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.ui.menu.dropdown.components.settings.MenuSettingComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.animation.types.ColorAnimation;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.TextUtility;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.render.ScissorUtility;

public class BindSettingComponent extends MenuSettingComponent<BindSetting> {
   private final ColorAnimation bindColorAnimation = new ColorAnimation(300L, new ColorRGBA(24.0F, 24.0F, 27.0F), Easing.FIGMA_EASE_IN_OUT);
   private final Animation widthAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);
   private Animation changeAnimation = new Animation(300L, 1.0F, Easing.FIGMA_EASE_IN_OUT);
   private int prevKey;
   private boolean bindingMode;

   public BindSettingComponent(BindSetting setting, CustomComponent parent) {
      super(setting, parent);
   }

   @Override
   protected void renderComponent(UIContext context) {
      Font nameFont = Fonts.REGULAR.getFont(8.0F);
      Font keyFont = Fonts.REGULAR.getFont(7.0F);
      float leftPadding = 10.0F;
      float headerHeight = 19.0F;
      this.bindColorAnimation.update(this.bindingMode ? Colors.getAccentColor() : Colors.getTextColor());
      this.changeAnimation.setDuration(500L);
      this.changeAnimation.update(1.0F);
      String key = TextUtility.getKeyName(this.setting.getKey());
      String prev = TextUtility.getKeyName(this.prevKey);
      float keyWidth = keyFont.width(key) + 7.0F;
      this.widthAnimation.update(keyWidth);
      context.drawRoundedRect(
         this.x + this.width - 9.0F - this.widthAnimation.getValue(),
         this.y + 4.0F,
         this.widthAnimation.getValue(),
         11.0F,
         BorderRadius.all(3.0F),
         Colors.getAdditionalColor()
      );
      ScissorUtility.push(
         context.getMatrices(), this.x + this.width - 9.0F - this.widthAnimation.getValue(), this.y + 4.0F, this.widthAnimation.getValue(), 11.0F
      );
      context.drawText(
         keyFont,
         prev,
         this.x + this.width - 9.0F - this.widthAnimation.getValue() + 4.0F + 4.0F * this.changeAnimation.getValue(),
         this.y + 7.0F,
         this.bindColorAnimation.getColor().withAlpha(255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue()) * (1.0F - this.changeAnimation.getValue()))
      );
      context.drawText(
         keyFont,
         key,
         this.x + this.width - 9.0F - this.widthAnimation.getValue() + 4.0F - 4.0F + 4.0F * this.changeAnimation.getValue(),
         this.y + 7.0F,
         this.bindColorAnimation.getColor().withAlpha(255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue()) * this.changeAnimation.getValue())
      );
      ScissorUtility.pop();
      context.drawFadeoutText(
         nameFont,
         Localizator.translate(this.setting.getName()),
         this.x + leftPadding,
         this.y + GuiUtility.getMiddleOfBox(nameFont.height(), headerHeight),
         Colors.getTextColor().withAlpha(255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue())),
         0.7F,
         0.99F,
         this.width - this.widthAnimation.getValue() - 20.0F
      );
      if (this.isHovered(context)) {
         CursorUtility.set(CursorType.HAND);
      }
   }

   @Override
   public void drawSplit(UIContext context) {
      float separatorHeight = 0.5F;
      context.drawRect(this.x, this.y + this.height, this.width, separatorHeight, Colors.getTextColor().withAlpha(5.1F));
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (this.isHovered(mouseX, mouseY) && button == MouseButton.LEFT) {
         this.bindingMode = !this.bindingMode;
      }

      if (this.bindingMode && button != MouseButton.LEFT) {
         int buttonIndex = button.getButtonIndex();
         this.setting.setKey(buttonIndex);
         this.bindingMode = false;
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      if (!this.bindingMode) {
         super.onKeyPressed(keyCode, scanCode, modifiers);
      } else {
         this.prevKey = this.setting.getKey();
         if (keyCode != 256 && keyCode != 261) {
            this.setting.setKey(keyCode);
         } else {
            this.setting.setKey(-1);
         }

         this.changeAnimation = new Animation(500L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
         this.bindingMode = false;
      }
   }

   @Override
   public float getHeight() {
      return this.height = 19.0F;
   }

   @Generated
   public void setBindingMode(boolean bindingMode) {
      this.bindingMode = bindingMode;
   }
}
