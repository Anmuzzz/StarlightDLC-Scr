package com.isusdlc.ui.menu.visuals.components;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomComponent;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Font;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.framework.objects.gradient.impl.VerticalGradient;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.menu.dropdown.components.settings.MenuSettingComponent;
import com.isusdlc.ui.menu.visuals.VisualsScreen;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.animation.types.ColorAnimation;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.gui.ScrollHandler;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.ScissorUtility;

public class VisualSettings extends CustomComponent {
   private final VisualToggle toggle;
   private List<MenuSettingComponent> components = new ArrayList<>();
   private final Animation animation = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   private boolean showing;
   private final ScrollHandler scrollHandler = new ScrollHandler();
   private float dragX;
   private float dragY;
   private boolean drag;
   private final Animation hoverAnimation = new Animation(300L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
   private final Animation circleOpacityAnimation = new Animation(300L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
   private final Animation enableAnimation = new Animation(300L, Easing.BAKEK);
   private final ColorAnimation backgroundColorAnimation = new ColorAnimation(300L, new ColorRGBA(24.0F, 24.0F, 27.0F), Easing.FIGMA_EASE_IN_OUT);

   public VisualSettings(VisualToggle toggle, float x, float y, float width) {
      this.toggle = toggle;
      this.x = x;
      this.y = y;
      this.width = width;
      this.showing = true;

      for (Setting setting : toggle.getModule().getSettings()) {
         MenuSettingComponent settingComponent = GuiUtility.settinge(setting, this);
         if (settingComponent != null) {
            this.components.add(settingComponent);
         }
      }
   }

   @Override
   protected void renderComponent(UIContext context) {
      this.animation.setDuration(this.showing ? 500L : 300L);
      this.animation.update(this.showing && mc.currentScreen instanceof VisualsScreen);
      this.scrollHandler.update();
      if (this.drag) {
         this.x = context.getMouseX() - this.dragX;
         this.y = context.getMouseY() - this.dragY;
      }

      float alpha = Math.min(1.0F, this.animation.getValue());
      boolean check = elegant.getInstance().getMenuScreen().getMenuAnimation().getValue()
         == elegant.getInstance().getMenuScreen().getMenuAnimation().getTargetValue();
      boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      this.animation.setEasing(this.showing ? Easing.QUARTIC_OUT : Easing.BAKEK_BACK);
      float x = MathUtility.interpolate(this.toggle.getX(), this.x, alpha);
      float y = MathUtility.interpolate(this.toggle.getY(), this.y, alpha);
      float width = MathUtility.interpolate(this.toggle.getWidth(), this.width, alpha);
      float height = MathUtility.interpolate(this.toggle.getHeight(), this.height, alpha);
      if (!this.showing || !check || !(mc.currentScreen instanceof VisualsScreen)) {
         x = this.x;
         y = this.y;
         width = this.width;
         height = this.height;
      }

      if (!this.showing || !check) {
         RenderUtility.scale(context.getMatrices(), x + width / 2.0F, y + height / 2.0F, 0.5F + 0.5F * this.animation.getValue());
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      }

      context.drawBlurredRect(x, y, width, height, 11.25F, 5.0F, BorderRadius.all(11.0F), Colors.WHITE);
      context.drawRoundedRect(x, y, width, height, BorderRadius.all(11.0F), new ColorRGBA(15.0F, 15.0F, 20.0F).withAlpha(255.0F * alpha));
      context.drawRoundedRect(
         x,
         y,
         width,
         height,
         BorderRadius.all(11.0F),
         new VerticalGradient(new ColorRGBA(60.0F, 30.0F, 120.0F).withAlpha(40.0F * alpha), new ColorRGBA(40.0F, 20.0F, 80.0F).withAlpha(60.0F * alpha))
      );
      if (this.showing && check) {
         context.drawRoundedRect(
            x + width - 25.0F,
            y + 10.5F + 20.0F * alpha,
            14.5F,
            7.0F,
            BorderRadius.all(3.0F),
            Colors.getAdditionalColor().mix(Colors.getAccentColor(), this.toggle.getModule().isEnabled() ? 1.0F : 0.0F).mulAlpha(1.0F - alpha)
         );
         context.drawRoundedRect(
            x + width - 25.0F + 1.0F + 5 * (this.toggle.getModule().isEnabled() ? 1 : 0),
            y + 11.5F + 20.0F * alpha,
            7.5F,
            5.0F,
            BorderRadius.all(1.75F),
            Colors.WHITE.mulAlpha(1.0F - alpha)
         );
         context.drawFadeoutText(
            Fonts.REGULAR.getFont(6.0F),
            Localizator.translate(this.toggle.getModule().getDescription()),
            x + 7.0F,
            y + 16.0F + 5.0F * alpha,
            Colors.getTextColor().mulAlpha(0.5F).mulAlpha(1.0F - alpha),
            0.9F,
            1.0F,
            width - 30.0F
         );
      }

      if (this.showing) {
         context.drawText(
            Fonts.MEDIUM.getFont(7.0F + 2.0F * alpha),
            Localizator.translate(this.toggle.getModule().getName()),
            x + 7.0F + 2.0F * alpha,
            y + 8.0F + 2.0F * alpha,
            Colors.getTextColor()
         );
      } else {
         context.drawText(Fonts.MEDIUM.getFont(9.0F), Localizator.translate(this.toggle.getModule().getName()), x + 9.0F, y + 10.0F, Colors.getTextColor());
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      context.drawTexture(elegant.id("icons/close.png"), x + width - 17.0F, y + 9.0F, 8.0F, 8.0F, Colors.getTextColor());
      if (GuiUtility.isHovered(x + width - 17.0F, y + 9.0F, 8.0, 8.0, context)) {
         CursorUtility.set(CursorType.HAND);
      }

      if (Interface.showMinimalizm()) {
         context.drawRect(x, y + 24.0F, width, 4.0F, Colors.getSeparatorColor().withAlpha(Colors.getSeparatorColor().getAlpha() * Interface.minimalizm()));
      }

      float settingsY = 28.0F;
      float offset = 0.0F;
      ScissorUtility.push(context.getMatrices(), x, y + 28.0F, width, height - 28.0F - 5.0F);
      this.circleOpacityAnimation.update(this.toggle.getModule().isEnabled() ? 1.0F : 0.75F);
      this.enableAnimation.update(this.toggle.getModule().isEnabled() ? 1.0F : 0.0F);
      this.backgroundColorAnimation
         .update(
            this.toggle.getModule().isEnabled()
               ? new ColorRGBA(151.0F, 71.0F, 255.0F)
               : elegant.getInstance().getThemeManager().getCurrentTheme().getAdditionalColor()
         );
      this.hoverAnimation.update(this.isHovered(context.getMouseX(), context.getMouseY()));
      if (this.isHovered(context.getMouseX(), context.getMouseY())) {
         CursorUtility.set(CursorType.HAND);
      }

      float settingY = (float)(y + 28.0F - this.scrollHandler.getValue());
      float checkWidth = 13.0F;
      float checkHeight = 8.0F;
      Font nameFont = Fonts.REGULAR.getFont(8.0F);
      float leftPadding = 10.0F;
      float headerHeight = 19.0F;
      String name = Localizator.translate("enabled");
      context.drawFadeoutText(
         nameFont,
         name.substring(0, 1).toUpperCase() + name.substring(1),
         x + leftPadding,
         settingY + GuiUtility.getMiddleOfBox(nameFont.height(), headerHeight) - 0.5F,
         Colors.getTextColor().withAlpha(255.0F * (0.75F + 0.25F * this.enableAnimation.getValue() + 0.25F * this.hoverAnimation.getValue())),
         0.7F,
         0.99F,
         width - checkWidth - 20.0F
      );
      context.drawRoundedRect(
         x + width - checkWidth - 9.0F,
         settingY + 5.0F,
         checkWidth,
         checkHeight,
         BorderRadius.all(3.0F),
         this.backgroundColorAnimation.getColor().withAlpha(!this.toggle.getModule().isEnabled() ? 255.0F - 100.0F * Interface.glass() : 255.0F)
      );
      context.drawRoundedRect(
         x + width - checkWidth - 8.0F + 5.0F * this.enableAnimation.getValue(),
         settingY + 6.0F,
         6.0F,
         6.0F,
         BorderRadius.all(4.0F),
         new ColorRGBA(255.0F, 255.0F, 255.0F).withAlpha(this.circleOpacityAnimation.getValue() * 255.0F)
      );
      float separatorHeight = 0.5F;
      context.drawRect(x, settingY + 18.0F, width, separatorHeight, Colors.getTextColor().withAlpha(5.1F));
      offset += 18.0F;

      for (MenuSettingComponent settingComponent : this.components) {
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, settingComponent.getOpacity() * alpha);
         settingComponent.getVisibilityAnimation().update(settingComponent.getSetting().isVisible() ? 1.0F : 0.0F);
         settingComponent.setX(x);
         settingComponent.setY((float)(y + settingsY + offset - this.scrollHandler.getValue()));
         settingComponent.setWidth(width);
         if (GuiUtility.isHovered(
            (double)x,
            (double)(y - settingComponent.getHeight()),
            (double)width,
            (double)(height + settingComponent.getHeight()),
            (double)settingComponent.getX(),
            (double)settingComponent.getY()
         )) {
            context.pushMatrix();
            context.getMatrices()
               .translate(0.0F, (-settingComponent.getHeight() + settingComponent.getHeight() * settingComponent.getOpacity()) / 2.0F, 0.0F);
            settingComponent.render(context);
            context.popMatrix();
         }

         offset += settingComponent.getHeight() * settingComponent.getOpacity();
      }

      ScissorUtility.pop();
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      this.height = Math.min(200.0F, offset + 28.0F + 5.0F);
      this.scrollHandler.setMax(-offset + height - 24.0F - 4.0F - 5.0F);
      if (!this.showing || !check) {
         RenderUtility.end(context.getMatrices());
      }

      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      for (MenuSettingComponent component : this.components) {
         if (component.getOpacity() != 0.0F && (this.isHovered(mouseX, mouseY) || button != MouseButton.LEFT)) {
            component.onMouseClicked(mouseX, mouseY, button);
         }
      }

      if (GuiUtility.isHovered((double)this.x, this.y + 24.0F - this.scrollHandler.getValue(), (double)this.width, 18.0, mouseX, mouseY)
         && button == MouseButton.LEFT) {
         this.toggle.getModule().toggle();
      }

      if (GuiUtility.isHovered((double)this.x, (double)this.y, (double)this.width, 24.0, mouseX, mouseY)) {
         this.drag = true;
         this.dragX = (float)(mouseX - this.x);
         this.dragY = (float)(mouseY - this.y);
      }

      if (GuiUtility.isHovered((double)(this.x + this.width - 17.0F), (double)(this.y + 9.0F), 8.0, 8.0, mouseX, mouseY)) {
         this.showing = false;
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      for (MenuSettingComponent component : this.components) {
         if (component.getOpacity() != 0.0F) {
            component.onMouseReleased(mouseX, mouseY, button);
         }
      }

      this.drag = false;
      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Override
   public void onScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (MenuSettingComponent component : this.components) {
         if (component.getOpacity() != 0.0F) {
            component.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
         }
      }

      if (this.isHovered(mouseX, mouseY)) {
         this.scrollHandler.scroll(verticalAmount);
      }

      super.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Override
   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      for (MenuSettingComponent component : this.components) {
         if (component.getOpacity() != 0.0F) {
            component.onKeyPressed(keyCode, scanCode, modifiers);
         }
      }

      super.onKeyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      for (MenuSettingComponent component : this.components) {
         if (component.getOpacity() != 0.0F) {
            component.charTyped(chr, modifiers);
         }
      }

      return super.charTyped(chr, modifiers);
   }

   @Override
   public float getHeight() {
      float offset = 18.0F;

      for (MenuSettingComponent settingComponent : this.components) {
         offset += settingComponent.getHeight() * settingComponent.getOpacity();
      }

      return this.height = Math.min(200.0F, offset + 28.0F + 5.0F);
   }

   @Generated
   public VisualToggle getToggle() {
      return this.toggle;
   }

   @Generated
   public List<MenuSettingComponent> getComponents() {
      return this.components;
   }

   @Generated
   public Animation getAnimation() {
      return this.animation;
   }

   @Generated
   public boolean isShowing() {
      return this.showing;
   }

   @Generated
   public void setShowing(boolean showing) {
      this.showing = showing;
   }
}
