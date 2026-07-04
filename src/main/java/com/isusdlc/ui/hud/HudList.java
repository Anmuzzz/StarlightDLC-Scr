package com.isusdlc.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.ScissorUtility;

public abstract class HudList extends HudElement {
   public HudList(String name, String icon) {
      super(name, icon);
   }

   @Override
   public void render(UIContext context) {
      this.update(context);
      float anim = this.animation.getValue() * this.visible.getValue();
      if (anim != 0.0F) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, anim));
         float scale = 0.5F + anim * 0.5F - 0.05F * this.selecting.getValue();
         RenderUtility.scale(context.getMatrices(), this.x + this.width / 2.0F, this.y + this.height / 2.0F, scale);
         context.drawShadow(
            this.x - 5.0F,
            this.y - 5.0F,
            this.width + 10.0F,
            this.height + 10.0F,
            15.0F,
            BorderRadius.all(6.0F),
            ColorRGBA.BLACK.withAlpha(63.75F * this.dragAnim.getValue())
         );
         ScissorUtility.push(context.getMatrices(), this.x, this.y, this.width, Math.max(20.0F, this.height));
         this.renderComponent(context);
         ScissorUtility.pop();
         RenderUtility.end(context.getMatrices());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      }
   }

   @Override
   protected void renderComponent(UIContext context) {
      Font font = Fonts.REGULAR.getFont(7.0F);
      context.drawClientRect(this.x, this.y, this.width, Math.max(20.0F, this.height), this.animation.getValue(), this.dragAnim.getValue(), 7.0F);
      float iconSize = 8.0F;
      context.drawText(
         Fonts.MEDIUM.getFont(7.0F),
         Localizator.translate(this.name),
         this.x + 7.0F,
         this.y + GuiUtility.getMiddleOfBox(font.height(), 18.0F) + 0.5F,
         Colors.getTextColor()
      );
      context.drawTexture(elegant.id(this.icon), this.x + this.width - iconSize - 7.0F, this.y + 6.0F, iconSize, iconSize, Colors.getTextColor());
      if (this.height >= 23.0F) {
         context.drawRect(this.x, this.y + 18.0F, this.width, 4.0F, Colors.getSeparatorColor());
      }
   }
}
