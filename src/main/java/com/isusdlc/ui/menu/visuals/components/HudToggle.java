package com.isusdlc.ui.menu.visuals.components;

import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.framework.objects.gradient.impl.VerticalGradient;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.hud.HudElement;
import com.isusdlc.ui.menu.visuals.VisualsScreen;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.interfaces.IScaledResolution;

public class HudToggle {
   private final HudElement element;
   private final Animation toggleAnimation = new Animation(300L, Easing.BAKEK);
   private final Animation hoverAnimation = new Animation(200L, Easing.LINEAR);
   private float x;
   private float y;
   private float width;
   private float height;

   public HudToggle(HudElement element) {
      this.element = element;
   }

   public void set(float x, float y, float width, float height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
   }

   public void render(UIContext context, float alpha) {
      boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      this.toggleAnimation.update(this.element.isShowing());
      boolean hovered = GuiUtility.isHovered((double)this.x, (double)this.y, (double)this.width, (double)this.height, context.getMouseX(), context.getMouseY());
      this.hoverAnimation.update(hovered);
      ColorRGBA bgColor;
      if (this.element.isShowing()) {
         bgColor = new ColorRGBA(30.0F, 25.0F, 40.0F).withAlpha(200.0F * alpha);
      } else {
         bgColor = new ColorRGBA(25.0F, 25.0F, 30.0F).withAlpha(150.0F * alpha);
      }

      if (this.hoverAnimation.getValue() > 0.0F) {
         bgColor = bgColor.mix(new ColorRGBA(255.0F, 255.0F, 255.0F).withAlpha(20.0F), this.hoverAnimation.getValue());
      }

      context.drawRoundedRect(this.x, this.y, this.width, this.height, BorderRadius.all(12.0F), bgColor);
      if (this.element.isShowing()) {
         context.drawRoundedRect(
            this.x,
            this.y,
            this.width,
            this.height,
            BorderRadius.all(12.0F),
            new VerticalGradient(new ColorRGBA(30.0F, 30.0F, 100.0F).withAlpha(60.0F * alpha), new ColorRGBA(30.0F, 20.0F, 140.0F).withAlpha(80.0F * alpha))
         );
      }

      if (this.element.getIcon() != null && !this.element.getIcon().isEmpty()) {
         context.drawTexture(
            elegant.id(this.element.getIcon()), this.x + 10.0F, this.y + this.height / 2.0F - 6.0F, 12.0F, 12.0F, Colors.WHITE.withAlpha(255.0F * alpha)
         );
      }

      float textX = this.element.getIcon() != null && !this.element.getIcon().isEmpty() ? this.x + 28.0F : this.x + 10.0F;
      context.drawText(
         Fonts.MEDIUM.getFont(8.0F),
         Localizator.translate(this.element.getName()),
         textX,
         this.y + this.height / 2.0F - 3.5F,
         Colors.WHITE.withAlpha(255.0F * alpha)
      );
      float switchWidth = 28.0F;
      float switchHeight = 14.0F;
      float switchX = this.x + this.width - switchWidth - 10.0F;
      float switchY = this.y + this.height / 2.0F - switchHeight / 2.0F;
      if (this.element.isShowing()) {
         context.drawRoundedRect(
            switchX,
            switchY,
            switchWidth,
            switchHeight,
            BorderRadius.all(switchHeight / 2.0F),
            new VerticalGradient(new ColorRGBA(140.0F, 80.0F, 240.0F).withAlpha(255.0F * alpha), new ColorRGBA(100.0F, 50.0F, 200.0F).withAlpha(255.0F * alpha))
         );
      } else {
         context.drawRoundedRect(
            switchX, switchY, switchWidth, switchHeight, BorderRadius.all(switchHeight / 2.0F), new ColorRGBA(50.0F, 50.0F, 60.0F).withAlpha(255.0F * alpha)
         );
      }

      float knobSize = 10.0F;
      float knobPadding = 2.0F;
      float knobX = switchX + knobPadding + (switchWidth - knobSize - knobPadding * 2.0F) * this.toggleAnimation.getValue();
      float knobY = switchY + (switchHeight - knobSize) / 2.0F;
      context.drawRoundedRect(knobX, knobY, knobSize, knobSize, BorderRadius.all(knobSize / 2.0F), Colors.WHITE.withAlpha(255.0F * alpha));
   }

   public void onMouseClicked(double mouseX, double mouseY, MouseButton button, VisualsScreen screen) {
      if (button == MouseButton.LEFT) {
         boolean wasShowing = this.element.isShowing();
         this.element.setShowing(!wasShowing);
         if (!wasShowing && this.element.isShowing()) {
            int showingCount = 0;

            for (HudElement el : elegant.getInstance().getHud().getElements()) {
               if (el.isShowing() && el != this.element) {
                  showingCount++;
               }
            }

            float baseX = IScaledResolution.sr.getScaledWidth() / 2.0F - this.element.getWidth() / 2.0F;
            float baseY = IScaledResolution.sr.getScaledHeight() / 2.0F - this.element.getHeight() / 2.0F;
            float offsetX = showingCount % 3 * 50.0F - 50.0F;
            float offsetY = showingCount / 3 * 50.0F;
            this.element.pos(baseX + offsetX, baseY + offsetY);
         }

         elegant.getInstance().getFileManager().writeFile("client");
      } else if (button == MouseButton.RIGHT && !this.element.getSettings().isEmpty()) {
         HudSettings settings = new HudSettings(
            this, screen.getMenuWindow().getX() + screen.getMenuWindow().getWidth() + 10.0F, screen.getMenuWindow().getY(), 200.0F
         );
         screen.getHudWindows().add(settings);
      }
   }

   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return GuiUtility.isHovered((double)this.x, (double)this.y, (double)this.width, (double)this.height, mouseX, mouseY);
   }

   @Generated
   public HudElement getElement() {
      return this.element;
   }

   @Generated
   public Animation getToggleAnimation() {
      return this.toggleAnimation;
   }

   @Generated
   public Animation getHoverAnimation() {
      return this.hoverAnimation;
   }

   @Generated
   public float getX() {
      return this.x;
   }

   @Generated
   public float getY() {
      return this.y;
   }

   @Generated
   public float getWidth() {
      return this.width;
   }

   @Generated
   public float getHeight() {
      return this.height;
   }
}
