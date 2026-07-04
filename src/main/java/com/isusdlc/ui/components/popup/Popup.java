package com.isusdlc.ui.components.popup;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomComponent;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.components.popup.list.Button;
import com.isusdlc.ui.components.popup.list.CheckBox;
import com.isusdlc.ui.components.popup.list.Separator;
import com.isusdlc.ui.components.popup.list.Text;
import com.isusdlc.ui.components.popup.list.Title;
import com.isusdlc.ui.menu.dropdown.components.settings.MenuSettingComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.batching.Batching;
import com.isusdlc.utility.render.batching.impl.FontBatching;
import net.minecraft.client.render.VertexFormats;

public class Popup extends CustomComponent {
   private final Animation animation = new Animation(300L, 0.0F, Easing.BAKEK_SIZE);
   private final Animation blurAnim = new Animation(300L, 0.0F, Easing.FIGMA_EASE_IN_OUT);
   private final List<CustomComponent> components = new ArrayList<>();
   private boolean showing;
   private final float offsetFactor;
   private Runnable onClose = () -> {};
   private boolean closed;

   public Popup(float x, float y) {
      this(x, y, 90.0F);
   }

   public Popup(float x, float y, float width) {
      this(x, y, width, 2.0F);
   }

   public Popup(float x, float y, float width, float offsetFactor) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.offsetFactor = offsetFactor;
      this.showing = true;
   }

   @Override
   protected void renderComponent(UIContext context) {
      this.animation.setEasing(this.showing ? Easing.BAKEK : Easing.BAKEK_BACK);
      this.animation.update(this.showing);
      this.blurAnim.update(this.animation.getValue() >= 0.6F);
      this.height = 0.0F;

      for (CustomComponent component : this.components) {
         if (component instanceof MenuSettingComponent) {
            component.set(this.x - 2.0F, this.y + this.height, this.width + 4.0F, 0.0F);
         } else {
            component.set(this.x, this.y + this.height, this.width, 0.0F);
         }

         this.height = this.height
            + (component.getHeight() + 0.5F) * (component instanceof MenuSettingComponent settingComponent ? settingComponent.getOpacity() : 1.0F);
      }

      this.height += 2.0F;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Math.min(1.0F, this.animation.getValue()));
      RenderUtility.scale(
         context.getMatrices(), this.x + this.width / this.offsetFactor, this.y + this.height / this.offsetFactor, 0.5F + this.animation.getValue() * 0.5F
      );
      context.drawShadow(this.x, this.y, this.width, this.height, 15.0F, BorderRadius.all(6.0F), ColorRGBA.BLACK.withAlpha(127.5F));
      if (Interface.showMinimalizm()) {
         context.drawBlurredRect(
            this.x,
            this.y,
            this.width,
            this.height,
            11.25F,
            7.0F,
            BorderRadius.all(6.0F),
            ColorRGBA.WHITE.withAlpha(255.0F * this.animation.getValue() * Interface.minimalizm())
         );
      }

      if (Interface.showGlass()) {
         context.drawLiquidGlass(
            this.x,
            this.y,
            this.width,
            this.height,
            7.0F,
            Interface.getDistortion(),
            BorderRadius.all(6.0F),
            Colors.getLiquidGlassColor().withAlpha(255.0F * this.animation.getValue() * Interface.glass())
         );
      }

      boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      context.drawSquircle(
         this.x,
         this.y,
         this.width,
         this.height,
         7.0F,
         BorderRadius.all(6.0F),
         Colors.getBackgroundColor().withAlpha(255.0F * (dark ? 0.8F - 0.6F * Interface.glass() : 0.7F))
      );

      for (CustomComponent component : this.components) {
         if (component instanceof MenuSettingComponent settingComponent) {
            settingComponent.getVisibilityAnimation().update(settingComponent.getSetting().isVisible() ? 1.0F : 0.0F);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, settingComponent.getOpacity() * this.animation.getValue());
         }

         int index = this.components.indexOf(component);
         if (index != 0 && !(component instanceof Separator) && !(this.components.get(index - 1) instanceof Separator)) {
            float separatorHeight = 0.5F;
            context.drawRect(this.x, component.getY() - 1.0F, this.width, separatorHeight, Colors.getTextColor().withAlpha(5.1F));
         }

         component.render(context);
         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.animation.getValue());
      }

      for (CustomComponent component : this.components) {
         if (component instanceof MenuSettingComponent comp) {
            Batching font = new FontBatching(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.REGULAR);
            comp.drawRegular8(context);
            font.draw();
         }
      }

      RenderUtility.end(context.getMatrices());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public Popup setting(Setting setting) {
      MenuSettingComponent settingComponent = GuiUtility.settinge(setting, this);
      if (settingComponent != null) {
         this.components.add(settingComponent);
      }

      return this;
   }

   public Popup add(CustomComponent component) {
      this.components.add(component);
      return this;
   }

   public Popup text(String text) {
      this.components.add(new Text(text));
      return this;
   }

   public Popup title(String text) {
      this.components.add(new Title(text));
      return this;
   }

   public Popup separator() {
      this.components.add(new Separator());
      return this;
   }

   public Popup checkbox(String text, boolean enabled) {
      this.components.add(new CheckBox(text).enabled(enabled));
      return this;
   }

   public Popup checkbox(String text, boolean enabled, CheckBoxAction action) {
      this.components.add(new CheckBox(text).enabled(enabled).action(action));
      return this;
   }

   public Popup button(String text, String icon, PopupAction runnable) {
      this.components.add(new Button(this, text, icon, runnable));
      return this;
   }

   public Popup onClose(Runnable onClose) {
      this.onClose = onClose;
      return this;
   }

   public void setShowing(boolean showing) {
      this.showing = showing;
      if (!showing && !this.closed) {
         this.onClose.run();
         this.closed = true;
      }
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      for (CustomComponent component : this.components) {
         if (!(component instanceof MenuSettingComponent settingComponent && settingComponent.getOpacity() == 0.0F)) {
            component.onMouseClicked(mouseX, mouseY, button);
         }
      }

      super.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      for (CustomComponent component : this.components) {
         if (!(component instanceof MenuSettingComponent settingComponent && settingComponent.getOpacity() == 0.0F)) {
            component.onMouseReleased(mouseX, mouseY, button);
         }
      }

      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Override
   public void onScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (CustomComponent component : this.components) {
         if (!(component instanceof MenuSettingComponent settingComponent && settingComponent.getOpacity() == 0.0F)) {
            component.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
         }
      }

      super.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Override
   public void onKeyPressed(int keyCode, int scanCode, int modifiers) {
      for (CustomComponent component : this.components) {
         if (!(component instanceof MenuSettingComponent settingComponent && settingComponent.getOpacity() == 0.0F)) {
            component.onKeyPressed(keyCode, scanCode, modifiers);
         }
      }

      super.onKeyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      for (CustomComponent component : this.components) {
         if (!(component instanceof MenuSettingComponent settingComponent && settingComponent.getOpacity() == 0.0F)) {
            component.charTyped(chr, modifiers);
         }
      }

      return super.charTyped(chr, modifiers);
   }

   @Generated
   public Animation getAnimation() {
      return this.animation;
   }

   @Generated
   public boolean isShowing() {
      return this.showing;
   }
}
