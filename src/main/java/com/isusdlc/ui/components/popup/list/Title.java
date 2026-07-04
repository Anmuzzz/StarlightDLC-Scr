package com.isusdlc.ui.components.popup.list;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.ui.components.popup.PopupComponent;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;

public class Title extends PopupComponent {
   private final String text;

   public Title(String text) {
      this.text = text;
   }

   @Override
   protected void renderComponent(UIContext context) {
      Font nameFont = Fonts.REGULAR.getFont(8.0F);
      float nameLeftPadding = 8.0F;
      float nameHeight = nameFont.height();
      context.drawFadeoutText(
         nameFont,
         this.text != null && !this.text.trim().isEmpty() ? Localizator.translate(this.text) : " ",
         this.x + nameLeftPadding,
         this.y + GuiUtility.getMiddleOfBox(nameHeight, this.height),
         Colors.getTextColor().withAlpha(RenderSystem.getShaderColor()[3] * 255.0F),
         0.8F,
         1.0F,
         this.width - 12.0F
      );
   }

   @Override
   public float getHeight() {
      return this.height = 18.0F;
   }
}
