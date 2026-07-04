package com.isusdlc.ui.menu.visuals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.framework.objects.gradient.impl.VerticalGradient;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.systems.modules.modules.visuals.MenuModule;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.ui.menu.MenuScreen;
import com.isusdlc.ui.menu.api.MenuCategory;
import com.isusdlc.ui.menu.visuals.components.HudSettings;
import com.isusdlc.ui.menu.visuals.components.HudToggle;
import com.isusdlc.ui.menu.visuals.components.VisualCategory;
import com.isusdlc.ui.menu.visuals.components.VisualSettings;
import com.isusdlc.ui.menu.visuals.components.VisualToggle;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.gui.ScrollHandler;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.interfaces.IScaledResolution;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.ScissorUtility;
import com.isusdlc.utility.render.obj.Rect;
import com.isusdlc.utility.sounds.ClientSounds;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import ru.kotopushka.compiler.sdk.annotations.Compile;

public class VisualsScreen extends MenuScreen implements IMinecraft, IScaledResolution {
   private final Rect menuWindow;
   private final ScrollHandler scrollHandler = new ScrollHandler();
   private final List<VisualCategory> categories = new ArrayList<>();
   private final List<VisualSettings> windows = new LinkedList<>();
   private final List<HudSettings> hudWindows = new LinkedList<>();
   private final TextField searchField;
   private VisualCategory currentCategory;
   private final Animation categoryLineAnimation = new Animation(300L, Easing.BAKEK);

   public VisualsScreen() {
      float width = 450.0F;
      float height = 320.0F;
      this.menuWindow = new Rect(sr.getScaledWidth() / 2.0F - width / 2.0F, sr.getScaledHeight() / 2.0F - height / 2.0F, width, height);
      this.categories.add(new VisualCategory("Visuals", MenuCategory.VISUALS));
      this.categories.add(new VisualCategory("HUD", null));
      this.categories.add(new VisualCategory("Utilities", null));
      this.currentCategory = this.categories.get(0);
      this.searchField = new TextField(Fonts.REGULAR.getFont(9.0F));
      this.searchField.setPreview("Search");
   }

   @Compile
   protected void init() {
      this.closing = false;

      for (VisualCategory category : this.categories) {
         category.init();
      }

      super.init();
   }

   public void tick() {
      this.handleMovementKeys();
      super.tick();
   }

   @Override
   public void render(UIContext context) {
      this.menuAnimation.update(this.closing ? 0.0F : 1.0F);
      this.menuAnimation.setEasing(!this.closing ? Easing.BAKEK : Easing.BAKEK_BACK);
      this.menuAnimation.setDuration(400L);
      this.scrollHandler.update();
      float alpha = Math.min(1.0F, this.menuAnimation.getValue());
      boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      RenderUtility.scale(
         context.getMatrices(),
         this.menuWindow.getX() + this.menuWindow.getWidth() / 2.0F,
         this.menuWindow.getY() + this.menuWindow.getHeight() / 2.0F,
         0.5F + 0.5F * this.menuAnimation.getValue()
      );
      context.drawBlurredRect(
         this.menuWindow.getX(),
         this.menuWindow.getY(),
         this.menuWindow.getWidth(),
         this.menuWindow.getHeight(),
         45.0F,
         5.0F,
         BorderRadius.all(16.0F),
         Colors.WHITE.withAlpha(255.0F * alpha)
      );
      ColorRGBA darkBg = new ColorRGBA(15.0F, 15.0F, 20.0F).withAlpha(255.0F * alpha);
      context.drawRoundedRect(
         this.menuWindow.getX(), this.menuWindow.getY(), this.menuWindow.getWidth(), this.menuWindow.getHeight(), BorderRadius.all(16.0F), darkBg
      );
      context.drawRoundedRect(
         this.menuWindow.getX(),
         this.menuWindow.getY(),
         this.menuWindow.getWidth(),
         this.menuWindow.getHeight(),
         BorderRadius.all(16.0F),
         new VerticalGradient(new ColorRGBA(20.0F, 30.0F, 120.0F).withAlpha(40.0F * alpha), new ColorRGBA(40.0F, 20.0F, 80.0F).withAlpha(60.0F * alpha))
      );
      float x = this.menuWindow.getX();
      float y = this.menuWindow.getY();
      this.renderTopBar(context, x, y, alpha, dark);
      this.renderSearchField(context, x, y, alpha, dark);
      this.renderContent(context, x, y, alpha, dark);
      RenderUtility.end(context.getMatrices());

      for (VisualSettings window : this.windows) {
         window.render(context);
      }

      for (HudSettings window : this.hudWindows) {
         window.render(context);
      }

      this.windows.removeIf(windowx -> windowx.getAnimation().getValue() == 0.0F && !windowx.isShowing());
      this.hudWindows.removeIf(windowx -> windowx.getAnimation().getValue() == 0.0F && !windowx.isShowing());
   }

   private void renderTopBar(UIContext context, float x, float y, float alpha, boolean dark) {
      float tabWidth = 80.0F;
      float tabHeight = 25.0F;
      float tabSpacing = 8.0F;
      float startX = x + 20.0F;
      int currentIndex = this.categories.indexOf(this.currentCategory);
      float targetLineX = startX + currentIndex * (tabWidth + tabSpacing);
      this.categoryLineAnimation.update(targetLineX);
      float lineWidth = 60.0F;
      float lineHeight = 2.0F;
      float lineY = y + 35.0F;
      context.drawRoundedRect(
         this.categoryLineAnimation.getValue() + (tabWidth - lineWidth) / 2.0F,
         lineY,
         lineWidth,
         lineHeight,
         BorderRadius.all(1.0F),
         Colors.WHITE.withAlpha(255.0F * alpha)
      );

      for (int i = 0; i < this.categories.size(); i++) {
         VisualCategory category = this.categories.get(i);
         float tabX = startX + i * (tabWidth + tabSpacing);
         boolean isSelected = category == this.currentCategory;
         category.getSelectedAnimation().update(isSelected);
         ColorRGBA textColor = isSelected ? Colors.WHITE.withAlpha(255.0F * alpha) : new ColorRGBA(120.0F, 120.0F, 130.0F).withAlpha(255.0F * alpha);
         context.drawText(Fonts.MEDIUM.getFont(9.0F), category.getName(), tabX, y + 18.0F, textColor);
         if (i < this.categories.size() - 1) {
            float separatorX = tabX + tabWidth + tabSpacing / 2.0F - 1.0F;
            context.drawRect(separatorX, y + 15.0F, 1.0F, 20.0F, new ColorRGBA(60.0F, 60.0F, 70.0F).withAlpha(255.0F * alpha));
         }
      }
   }

   private void renderSearchField(UIContext context, float x, float y, float alpha, boolean dark) {
      float searchWidth = 120.0F;
      float searchHeight = 22.0F;
      float searchX = x + this.menuWindow.getWidth() - searchWidth - 15.0F;
      float searchY = y + 13.0F;
      context.drawRoundedRect(
         searchX,
         searchY,
         searchWidth,
         searchHeight,
         BorderRadius.all(5.0F),
         dark ? Colors.getAdditionalColor().mulAlpha(0.6F * alpha) : Colors.getBackgroundColor().mulAlpha(0.6F * alpha)
      );
      this.searchField.set(searchX + 8.0F, searchY + 2.0F, searchWidth - 16.0F, searchHeight - 4.0F);
      this.searchField.setTextColor(Colors.getTextColor().withAlpha(255.0F * alpha));
      this.searchField.setAlpha(alpha);
      this.searchField.render(context);
   }

   private void renderContent(UIContext context, float x, float y, float alpha, boolean dark) {
      float contentX = x + 15.0F;
      float contentY = y + 50.0F;
      float contentWidth = this.menuWindow.getWidth() - 30.0F;
      float contentHeight = this.menuWindow.getHeight() - 65.0F;
      ScissorUtility.push(context.getMatrices(), contentX, contentY, contentWidth, contentHeight);
      float scroll = (float)(-this.scrollHandler.getValue());
      float yOffset = scroll;
      float toggleWidth = (contentWidth - 10.0F) / 2.0F;
      float toggleHeight = 32.0F;
      float spacing = 8.0F;
      if (this.currentCategory.isHudCategory()) {
         List<HudToggle> hudToggles = this.currentCategory.getHudToggles();

         for (int i = 0; i < hudToggles.size(); i++) {
            HudToggle toggle = hudToggles.get(i);
            int column = i % 2;
            int row = i / 2;
            float toggleX = contentX + column * (toggleWidth + spacing);
            float toggleY = contentY + yOffset + row * (toggleHeight + spacing);
            toggle.set(toggleX, toggleY, toggleWidth, toggleHeight);
            toggle.render(context, alpha);
         }

         float totalHeight = (hudToggles.size() + 1) / 2 * (toggleHeight + spacing);
         float maxScroll = -Math.max(0.0F, totalHeight - contentHeight);
         this.scrollHandler.setMax(maxScroll);
      } else {
         List<VisualToggle> toggles = this.currentCategory.getToggles();

         for (int i = 0; i < toggles.size(); i++) {
            VisualToggle toggle = toggles.get(i);
            int column = i % 2;
            int row = i / 2;
            float toggleX = contentX + column * (toggleWidth + spacing);
            float toggleY = contentY + yOffset + row * (toggleHeight + spacing);
            toggle.set(toggleX, toggleY, toggleWidth, toggleHeight);
            toggle.render(context, alpha);
         }

         float totalHeight = (toggles.size() + 1) / 2 * (toggleHeight + spacing);
         float maxScroll = -Math.max(0.0F, totalHeight - contentHeight);
         this.scrollHandler.setMax(maxScroll);
      }

      ScissorUtility.pop();
   }

   @Compile
   private void handleMovementKeys() {
      if (mc.player != null && !this.isTyping()) {
         long windowHandle = mc.getWindow().getHandle();
         KeyBinding[] movementKeys = new KeyBinding[]{
            mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey
         };

         for (KeyBinding key : movementKeys) {
            int keyCode = InputUtil.fromTranslationKey(key.getBoundKeyTranslationKey()).getCode();
            key.setPressed(InputUtil.isKeyPressed(windowHandle, keyCode));
         }

         if (mc.player.getAbilities().flying) {
            int keyCode = InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode();
            mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(windowHandle, keyCode));
         }
      }
   }

   private boolean isTyping() {
      return mc.currentScreen != null && TextField.LAST_FIELD != null && TextField.LAST_FIELD.isFocused();
   }

   @Compile
   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (!elegant.getInstance().getHud().getIsland().handleClick((float)mouseX, (float)mouseY, button.getButtonIndex())) {
         for (VisualSettings window : this.windows) {
            window.onMouseClicked(mouseX, mouseY, button);
            if (window.isHovered(mouseX, mouseY)) {
               return;
            }

            if (!GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
               boolean can = true;

               for (VisualSettings window1 : this.windows) {
                  if (GuiUtility.isHovered(window1, mouseX, mouseY)) {
                     can = false;
                  }
               }

               if (can) {
                  window.setShowing(false);
               }
            }
         }

         for (HudSettings window : this.hudWindows) {
            window.onMouseClicked(mouseX, mouseY, button);
            if (window.isHovered(mouseX, mouseY)) {
               return;
            }

            if (!GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
               boolean can = true;

               for (HudSettings window1x : this.hudWindows) {
                  if (GuiUtility.isHovered(window1x, mouseX, mouseY)) {
                     can = false;
                  }
               }

               if (can) {
                  window.setShowing(false);
               }
            }
         }

         float tabWidth = 80.0F;
         float tabHeight = 25.0F;
         float tabSpacing = 8.0F;
         float startX = this.menuWindow.getX() + 20.0F;
         float y = this.menuWindow.getY();

         for (int i = 0; i < this.categories.size(); i++) {
            VisualCategory category = this.categories.get(i);
            float tabX = startX + i * (tabWidth + tabSpacing);
            if (GuiUtility.isHovered((double)(tabX - 5.0F), (double)(y + 12.0F), (double)tabWidth, (double)tabHeight, mouseX, mouseY)) {
               this.currentCategory = category;
               this.scrollHandler.setValue(0.0);
               return;
            }
         }

         if (this.currentCategory.isHudCategory()) {
            for (HudToggle toggle : this.currentCategory.getHudToggles()) {
               if (toggle.isHovered(mouseX, mouseY)) {
                  toggle.onMouseClicked(mouseX, mouseY, button, this);
                  return;
               }
            }
         } else {
            for (VisualToggle togglex : this.currentCategory.getToggles()) {
               if (togglex.isHovered(mouseX, mouseY)) {
                  togglex.onMouseClicked(mouseX, mouseY, button, this);
                  return;
               }
            }
         }

         if (button != MouseButton.MIDDLE) {
            this.searchField.onMouseClicked(mouseX, mouseY, button);
         }

         super.onMouseClicked(mouseX, mouseY, button);
      }
   }

   @Compile
   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      for (VisualSettings window : this.windows) {
         window.onMouseReleased(mouseX, mouseY, button);
      }

      for (HudSettings window : this.hudWindows) {
         window.onMouseReleased(mouseX, mouseY, button);
      }

      if (this.currentCategory.isHudCategory()) {
         for (HudToggle toggle : this.currentCategory.getHudToggles()) {
            toggle.onMouseReleased(mouseX, mouseY, button);
         }
      } else {
         for (VisualToggle toggle : this.currentCategory.getToggles()) {
            toggle.onMouseReleased(mouseX, mouseY, button);
         }
      }

      if (this.searchField.isFocused()) {
         this.searchField.onMouseReleased(mouseX, mouseY, button);
      }

      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Compile
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (VisualSettings window : this.windows) {
         window.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
      }

      for (HudSettings window : this.hudWindows) {
         window.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
      }

      if (GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
         this.scrollHandler.scroll(verticalAmount);
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Compile
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      for (VisualSettings window : this.windows) {
         window.onKeyPressed(keyCode, scanCode, modifiers);
      }

      for (HudSettings window : this.hudWindows) {
         window.onKeyPressed(keyCode, scanCode, modifiers);
      }

      if (this.searchField.isFocused()) {
         this.searchField.onKeyPressed(keyCode, scanCode, modifiers);
      }

      this.scrollHandler.onKeyPressed(keyCode);
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Compile
   public boolean charTyped(char chr, int modifiers) {
      for (VisualSettings window : this.windows) {
         window.charTyped(chr, modifiers);
      }

      for (HudSettings window : this.hudWindows) {
         window.charTyped(chr, modifiers);
      }

      if (this.searchField.isFocused()) {
         this.searchField.charTyped(chr, modifiers);
      }

      return super.charTyped(chr, modifiers);
   }

   @Compile
   public void close() {
      this.closing = true;
      elegant.getInstance().getModuleManager().getModule(MenuModule.class).disable();
      Sounds soundsModule = elegant.getInstance().getModuleManager().getModule(Sounds.class);
      if (soundsModule.isEnabled()) {
         ClientSounds.CLICKGUI_OPEN.play(soundsModule.getVolume().getCurrentValue(), 1.0F);
      }

      elegant.getInstance().getFileManager().writeFile("client");
      if (TextField.LAST_FIELD != null) {
         TextField.LAST_FIELD.setFocused(false);
      }

      super.close();
   }

   public boolean shouldPause() {
      return false;
   }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
   }

   public boolean shouldCloseOnEsc() {
      return true;
   }

   @Generated
   public Rect getMenuWindow() {
      return this.menuWindow;
   }

   @Generated
   public ScrollHandler getScrollHandler() {
      return this.scrollHandler;
   }

   @Generated
   public List<VisualCategory> getCategories() {
      return this.categories;
   }

   @Generated
   public TextField getSearchField() {
      return this.searchField;
   }

   @Generated
   public VisualCategory getCurrentCategory() {
      return this.currentCategory;
   }

   @Generated
   public List<VisualSettings> getWindows() {
      return this.windows;
   }

   @Generated
   public List<HudSettings> getHudWindows() {
      return this.hudWindows;
   }
}
