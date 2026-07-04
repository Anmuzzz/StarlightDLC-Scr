package com.isusdlc.ui.components.popup.list;

import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.ui.components.popup.Popup;
import com.isusdlc.ui.components.popup.PopupAction;
import com.isusdlc.ui.components.popup.PopupComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;

public class Button extends PopupComponent {
   private final Popup popup;
   private final String text;
   private final String icon;
   private PopupAction action;
   private final Animation hoverAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);

   @Override
   protected void renderComponent(UIContext context) {
      Font nameFont = Fonts.REGULAR.getFont(8.0F);
      float nameLeftPadding = 8.0F;
      float nameHeight = nameFont.height();
      this.hoverAnimation.update(this.isHovered(context.getMouseX(), context.getMouseY()));
      if (this.isHovered(context.getMouseX(), context.getMouseY())) {
         CursorUtility.set(CursorType.HAND);
      }

      ColorRGBA color = !this.text.equals(Localizator.translate("remove")) ? Colors.getTextColor() : ColorRGBA.RED.mix(ColorRGBA.WHITE, 0.3F);
      context.drawFadeoutText(
         nameFont,
         this.text,
         this.x + nameLeftPadding,
         this.y + GuiUtility.getMiddleOfBox(nameHeight, this.height),
         color.withAlpha(RenderSystem.getShaderColor()[3] * 255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue())),
         0.8F,
         1.0F,
         this.width - 24.0F
      );
      context.drawTexture(
         elegant.id(this.icon),
         this.x + this.width - 16.0F,
         this.y + 6.0F,
         8.0F,
         8.0F,
         color.withAlpha(RenderSystem.getShaderColor()[3] * 255.0F * (0.75F + 0.25F * this.hoverAnimation.getValue()))
      );
      if (this.isHovered(context)) {
         CursorUtility.set(CursorType.HAND);
      }
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (this.isHovered(mouseX, mouseY) && button == MouseButton.LEFT) {
         this.action.run(this.popup);
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public float getHeight() {
      return this.height = 19.0F;
   }

   @Generated
   public Button(Popup popup, String text, String icon, PopupAction action) {
      this.popup = popup;
      this.text = text;
      this.icon = icon;
      this.action = action;
   }
}
