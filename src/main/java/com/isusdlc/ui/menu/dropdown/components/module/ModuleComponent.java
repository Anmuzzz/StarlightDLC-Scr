package com.isusdlc.ui.menu.dropdown.components.module;

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
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.ui.menu.dropdown.DropDownScreen;
import com.isusdlc.ui.menu.dropdown.components.MenuPanel;
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
import com.isusdlc.utility.render.DrawUtility;
import com.isusdlc.utility.render.penis.PenisPlayer;
import com.isusdlc.utility.sounds.ClientSounds;

public class ModuleComponent extends CustomComponent {
   private final Module module;
   private final MenuPanel parent;
   private final Animation hoverAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);
   private final Animation enableAnimation = new Animation(300L, Easing.FIGMA_EASE_IN_OUT);
   private final Animation shakeAnimation = new Animation(100L, Easing.FIGMA_EASE_IN_OUT);
   private final Animation blockingAnimation = new Animation(500L, Easing.FIGMA_EASE_IN_OUT);
   private final ColorAnimation blockingColorAnimation = new ColorAnimation(500L, ColorRGBA.WHITE, Easing.FIGMA_EASE_IN_OUT);
   private boolean blocking;
   private boolean shakeValue;
   private final PenisPlayer enablePenis;
   private final PenisPlayer disablePenis;
   private PenisPlayer currentPenis;
   private boolean lastModuleState;
   private Font nameFont;
   private float headerHeight;
   private final List<MenuSettingComponent<?>> settingComponents = new ArrayList<>();
   private boolean bindingMode;

   public ModuleComponent(Module module, MenuPanel parent) {
      this.module = module;
      this.parent = parent;
      this.enablePenis = new PenisPlayer(elegant.id("penises/check_enable.penis"));
      this.disablePenis = new PenisPlayer(elegant.id("penises/check_disable.penis"));
      this.lastModuleState = module.isEnabled();
      this.currentPenis = this.lastModuleState ? this.enablePenis : this.disablePenis;
      if (this.lastModuleState) {
         this.enablePenis.playOnce();
         this.currentPenis = this.enablePenis;
      } else {
         this.disablePenis.setFrame(0);
         this.disablePenis.stop();
         this.currentPenis = this.disablePenis;
      }
   }

   @Override
   public void onInit() {
      this.nameFont = Fonts.REGULAR.getFont(8.0F);
      this.headerHeight = 16.0F;
      this.settingComponents.clear();

      for (Setting setting : this.module.getSettings()) {
         MenuSettingComponent<?> settingComponent = GuiUtility.settinge(setting, this);
         if (settingComponent != null) {
            this.settingComponents.add(settingComponent);
         }
      }

      this.settingComponents.forEach(MenuSettingComponent::onInit);

      for (MenuSettingComponent<?> settingComponent : this.settingComponents) {
         settingComponent.getVisibilityAnimation().setValue(settingComponent.getSetting().isVisible() ? 1.0F : 0.0F);
      }

      super.onInit();
   }

   @Override
   protected void renderComponent(UIContext context) {
      this.enableAnimation.update(this.module.isEnabled() ? 1.0F : 0.0F);
      boolean currentState = this.module.isEnabled();
      if (currentState != this.lastModuleState) {
         if (currentState) {
            this.currentPenis = this.enablePenis;
         } else {
            this.currentPenis = this.disablePenis;
         }

         this.currentPenis.playOnce();
         this.lastModuleState = currentState;
      }

      this.currentPenis.update();
      this.blockingAnimation.update(this.blocking);
      this.blockingColorAnimation
         .update(this.blocking ? new ColorRGBA(255.0F, 150.0F, 150.0F) : elegant.getInstance().getThemeManager().getCurrentTheme().getTextColor());
      this.shakeAnimation.update(this.blocking ? (this.shakeValue ? 1.0F : -1.0F) : 0.0F);
      if (this.blockingAnimation.getValue() == 1.0F) {
         this.blocking = false;
      }

      if (this.shakeAnimation.getValue() == 1.0F) {
         this.shakeValue = false;
      }

      if (this.shakeAnimation.getValue() == -1.0F) {
         this.shakeValue = true;
      }

      boolean hovered = this.isHovered(context.getMouseX(), context.getMouseY());
      this.hoverAnimation.update(hovered);
      float enable = this.enableAnimation.getValue();
      float hover = this.hoverAnimation.getValue();
      float baseAlpha = 0.55F + 0.15F * enable + 0.1F * hover;
      float width = this.width - 4.0F;
      float height = this.headerHeight - 2.0F;
      context.drawRoundedRect(
         this.x + 2.0F,
         this.y + 1.0F,
         width,
         height,
         BorderRadius.all(4.0F),
         Colors.getBackgroundColor()
            .mix(Colors.getAccentColor(), 0.15F * enable)
            .withAlpha(255.0F * baseAlpha)
      );
      float dotSize = 4.0F;
      float dotX = this.x + this.width - 8.0F;
      float dotY = this.y + this.headerHeight / 2.0F - dotSize / 2.0F;
      context.drawRoundedRect(
         dotX,
         dotY,
         dotSize,
         dotSize,
         BorderRadius.all(2.0F),
         (enable > 0.0F ? Colors.WHITE.mix(Colors.getAccentColor(), 0.6F) : Colors.WHITE.mulAlpha(0.6F))
      );

      if (this.parent.isHovered(context) && this.isHovered(context)) {
         CursorUtility.set(CursorType.HAND);
         if (elegant.getInstance().getMenuScreen() instanceof DropDownScreen dropDownScreen) {
            dropDownScreen.setDesc(this.module.getDescription());
         }
      }
   }

   public void drawRegular8(UIContext context) {
      float nameLeftPadding = 10.0F + 2.0F * this.enableAnimation.getValue();
      float nameHeight = this.nameFont.height();
      int key = this.module.getKey();
      this.hoverAnimation.update(this.isHovered(context.getMouseX(), context.getMouseY()));
      String bindingText;
      if (key == -1) {
         bindingText = Localizator.translate("menu.binding");
      } else {
         bindingText = Localizator.translate("key") + ": " + TextUtility.getKeyName(key);
      }

      context.drawText(
         this.nameFont,
         this.bindingMode && this.parent.getSelectedModuleComponent() == null ? bindingText : this.module.getName(),
         this.x + nameLeftPadding + this.shakeAnimation.getValue(),
         this.y + GuiUtility.getMiddleOfBox(nameHeight, this.headerHeight) - 0.5F,
         this.blockingColorAnimation
            .getColor()
            .withAlpha(RenderSystem.getShaderColor()[3] * 255.0F * (0.75F + 0.25F * this.enableAnimation.getValue() + 0.25F * this.hoverAnimation.getValue()))
      );
   }

   public void drawIcons(UIContext context) {
   }

   public void drawSplit(UIContext context) {
      float separatorHeight = 0.5F;
      context.drawRect(
         this.x, this.y + this.height, this.width, separatorHeight, Colors.getTextColor().withAlpha(RenderSystem.getShaderColor()[3] * 255.0F * 0.02F)
      );
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (this.isHovered(mouseX, mouseY)) {
         if (this.bindingMode && button != MouseButton.LEFT && button != MouseButton.RIGHT) {
            this.module.setKey(button.getButtonIndex());
            this.bindingMode = false;
         } else {
            switch (button) {
               case LEFT:
                  this.module.toggle();
                  break;
               case MIDDLE:
                  for (ModuleComponent comp : this.parent.getModuleComponents()) {
                     comp.setBindingMode(false);
                  }

                  this.bindingMode = true;
                  break;
               case RIGHT:
                  this.open();
            }

            super.onMouseClicked(mouseX, mouseY, button);
         }
      }
   }

   public void open() {
      if (this.module.getSettings().isEmpty()) {
         if (elegant.getInstance().getModuleManager().getModule(Sounds.class).isEnabled() && !this.blocking) {
            ClientSounds.CRITICAL.play(1.0F, 1.0F);
         }

         this.blocking = true;
         this.shakeValue = true;
      } else {
         this.parent.setSelectedModuleComponent(this);
         this.onInit();
         if (elegant.getInstance().getModuleManager().getModule(Sounds.class).isEnabled()) {
            ClientSounds.CLICKGUI_OPEN.play(0.8F, 1.3F);
         }
      }
   }

   @Override
   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      if (this.bindingMode) {
         if (keyCode != 256 && keyCode != 261) {
            this.module.setKey(keyCode);
         } else {
            this.module.setKey(-1);
         }

         this.bindingMode = false;
         if (elegant.getInstance().getMenuScreen() instanceof DropDownScreen dropDownScreen) {
            dropDownScreen.getSearchField().setFocused(false);
         }
      }

      super.onKeyPressed(keyCode, scanCode, modifiers);
   }

   @Generated
   public Module getModule() {
      return this.module;
   }

   @Generated
   public MenuPanel getParent() {
      return this.parent;
   }

   @Generated
   public Animation getHoverAnimation() {
      return this.hoverAnimation;
   }

   @Generated
   public Animation getEnableAnimation() {
      return this.enableAnimation;
   }

   @Generated
   public Animation getShakeAnimation() {
      return this.shakeAnimation;
   }

   @Generated
   public Animation getBlockingAnimation() {
      return this.blockingAnimation;
   }

   @Generated
   public ColorAnimation getBlockingColorAnimation() {
      return this.blockingColorAnimation;
   }

   @Generated
   public boolean isBlocking() {
      return this.blocking;
   }

   @Generated
   public boolean isShakeValue() {
      return this.shakeValue;
   }

   @Generated
   public PenisPlayer getEnablePenis() {
      return this.enablePenis;
   }

   @Generated
   public PenisPlayer getDisablePenis() {
      return this.disablePenis;
   }

   @Generated
   public PenisPlayer getCurrentPenis() {
      return this.currentPenis;
   }

   @Generated
   public boolean isLastModuleState() {
      return this.lastModuleState;
   }

   @Generated
   public Font getNameFont() {
      return this.nameFont;
   }

   @Generated
   public float getHeaderHeight() {
      return this.headerHeight;
   }

   @Generated
   public List<MenuSettingComponent<?>> getSettingComponents() {
      return this.settingComponents;
   }

   @Generated
   public boolean isBindingMode() {
      return this.bindingMode;
   }

   @Generated
   public void setBindingMode(boolean bindingMode) {
      this.bindingMode = bindingMode;
   }
}
