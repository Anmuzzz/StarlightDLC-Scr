package com.isusdlc.ui.menu.dropdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.localization.Localizator;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.systems.modules.modules.visuals.Interface;
import com.isusdlc.systems.modules.modules.visuals.MenuModule;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.components.ColorPicker;
import com.isusdlc.ui.components.animated.AnimatedText;
import com.isusdlc.ui.components.textfield.FieldAction;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.ui.menu.MenuScreen;
import com.isusdlc.ui.menu.api.MenuCategory;
import com.isusdlc.ui.menu.dropdown.components.MenuPanel;
import com.isusdlc.ui.menu.dropdown.components.module.ModuleComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BezierSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BindSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BooleanSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ButtonSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ColorSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ModeSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.RangeSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.SliderSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.StringSettingComponent;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.ColorRGBA;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.math.MathUtility;
import com.isusdlc.utility.render.DrawUtility;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.ScissorUtility;
import com.isusdlc.utility.render.batching.Batching;
import com.isusdlc.utility.render.batching.impl.FontBatching;
import com.isusdlc.utility.render.batching.impl.IconBatching;
import com.isusdlc.utility.render.batching.impl.RectBatching;
import com.isusdlc.utility.sounds.ClientSounds;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import ru.kotopushka.compiler.sdk.annotations.Compile;

public class DropDownScreen extends MenuScreen implements IMinecraft {
   private final Animation searchAnimation = new Animation(300L, Easing.BAKEK);
   private final Animation appendingAnim = new Animation(300L, Easing.BAKEK);
   private boolean closing;
   private List<MenuPanel> panels = new ArrayList<>();
   private float panelWidth;
   private float panelHeight;
   private String desc = "";
   private AnimatedText descText;
   private final List<ColorPicker> colorPickers = new ArrayList<>();
   private TextField searchField;

   @Compile
   protected void init() {
      this.descText = new AnimatedText(Fonts.REGULAR.getFont(10.0F), 10.0F, 300L, Easing.BAKEK).centered();
      this.closing = false;
      this.panelWidth = 115.0F;
      this.panelHeight = 240.0F;
      this.panels = Arrays.stream(MenuCategory.values()).map(MenuPanel::new).toList();

      for (MenuPanel panel : this.panels) {
         panel.setWidth(this.panelWidth);
         panel.setHeight(this.panelHeight);
         panel.onInit();
      }

      this.searchField = new TextField(Fonts.REGULAR.getFont(12.0F));
      Map<String, FieldAction> append = new HashMap<>();

      for (Module module : elegant.getInstance().getModuleManager().getModules()) {
         if (!module.isHidden()) {
            FieldAction action = new FieldAction(
               module::toggle,
               () -> this.panels
                  .forEach(panelx -> panelx.getModuleComponents().stream().filter(component -> component.getModule() == module).forEach(ModuleComponent::open))
            );
            append.put(module.getName().replace(" ", ""), action);
            append.put(module.getName(), action);
         }
      }

      this.searchField.setAppend(append);
      super.init();
   }

   public void tick() {
      this.handleMovementKeys();
      super.tick();
   }

   @Compile
   @Override
   public void render(UIContext context) {
      this.menuAnimation.setEasing(Easing.LINEAR);
      this.menuAnimation.update(this.isClosing() ? 0.0F : 1.0F);
      this.menuAnimation.setDuration(this.isClosing() ? 300L : 500L);
      this.desc = "";

      float spacing = 10.0F;
      float containerWidth = 650.0F;
      float containerHeight = 275.0F;
      float containerX = (this.width - containerWidth) / 2.0F;
      float containerY = (this.height - containerHeight) / 2.0F;

      float x = containerX + (containerWidth - (this.panelWidth + spacing) * this.panels.size() + spacing) / 2.0F;
      float y = containerY + (containerHeight - this.panelHeight) / 2.0F;

      float revealProgress = this.menuAnimation.getValue();
      float scale = MathUtility.interpolate(1.06F, 1.0F, revealProgress);
      float centerX = containerX + containerWidth / 2.0F;
      float centerY = containerY + containerHeight / 2.0F;

      context.pushMatrix();
      context.getMatrices().translate(centerX, centerY, 0.0F);
      context.getMatrices().scale(scale, scale, 1.0F);
      context.getMatrices().translate(-centerX, -centerY, 0.0F);

      boolean darkTheme = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      float cardAlpha = this.menuAnimation.getValue();

      if (Interface.showMinimalizm()) {
         context.drawBlurredRect(
            containerX,
            containerY,
            containerWidth,
            containerHeight,
            18.0F,
            BorderRadius.all(12.0F),
            ColorRGBA.WHITE.withAlpha(220.0F * cardAlpha)
         );
      }

      if (Interface.showGlass()) {
         context.drawLiquidGlass(
            containerX,
            containerY,
            containerWidth,
            containerHeight,
            8.0F,
            0.12F,
            BorderRadius.all(12.0F),
            Colors.getLiquidGlassColor().withAlpha(255.0F * cardAlpha)
         );
      }

      context.drawRoundedRect(
         containerX,
         containerY,
         containerWidth,
         containerHeight,
         BorderRadius.all(12.0F),
         Colors.getBackgroundColor().withAlpha(255.0F * (darkTheme ? 0.94F : 0.85F) * cardAlpha)
      );

      float offset = 0.0F;

      for (MenuPanel panel : this.panels) {
         panel.setX(
            MathUtility.interpolate(x + offset, centerX - this.panelWidth / 2.0F, this.closing ? 1.0F - this.menuAnimation.getValue() : 0.0)
         );
         panel.setY(y);
         panel.setWidth(this.panelWidth);
         panel.setHeight(this.panelHeight);
         offset += this.panelWidth + spacing;
      }

      if (Interface.showGlass()) {
         DrawUtility.updateBuffer();
      }

      for (MenuPanel panel : this.panels) {
         panel.renderBlur(context);
      }

      for (MenuPanel panel : this.panels) {
         panel.render(context);
      }

      Batching icon = new IconBatching(VertexFormats.POSITION_TEXTURE_COLOR, context.getMatrices());

      for (MenuPanel panel : this.panels) {
         panel.drawType(context);
      }

      icon.draw();

      for (MenuPanel panel : this.panels) {
         this.scissor(context, panel, () -> {
            Batching font = new FontBatching(VertexFormats.POSITION_TEXTURE_COLOR, Fonts.REGULAR);
            panel.drawRegular8(context);
            font.draw();
            Batching icon1 = new IconBatching(VertexFormats.POSITION_TEXTURE_COLOR, context.getMatrices());
            panel.drawIcons(context);
            icon1.draw();
            Batching split = new RectBatching(VertexFormats.POSITION_COLOR, context.getMatrices());
            panel.drawSplit(context);
            split.draw();
         });
      }

      this.searchAnimation.update(this.searchField.isFocused());
      float searchAlpha = this.menuAnimation.getValue() * this.searchAnimation.getValue();
      if (searchAlpha > 0.0F) {
         float searchWidth = 125.0F;
         float searchHeight = 23.0F;
         float searchX = containerX + (containerWidth - searchWidth) / 2.0F;
         float searchY = containerY + containerHeight + 8.0F;

         this.searchField.set(searchX, searchY, searchWidth, searchHeight);
         this.searchField.setAlpha(searchAlpha);
         this.searchField.setTextColor(Colors.getTextColor());

         if (Interface.showMinimalizm()) {
            context.drawBlurredRect(
               this.searchField.getX(),
               this.searchField.getY(),
               this.searchField.getWidth(),
               this.searchField.getHeight(),
               45.0F,
               BorderRadius.all(6.0F),
               ColorRGBA.WHITE.withAlpha(255.0F * searchAlpha)
            );
         }

         if (Interface.showGlass()) {
            context.drawLiquidGlass(
               this.searchField.getX(),
               this.searchField.getY(),
               this.searchField.getWidth(),
               this.searchField.getHeight(),
               2.0F,
               0.08F,
               BorderRadius.all(6.0F),
               ColorRGBA.WHITE.withAlpha(255.0F * searchAlpha)
            );
         }

         context.drawRoundedRect(
            this.searchField.getX(),
            this.searchField.getY(),
            this.searchField.getWidth(),
            this.searchField.getHeight(),
            BorderRadius.all(6.0F),
            Colors.getBackgroundColor().withAlpha(255.0F * (darkTheme ? 0.9F - 0.7F * Interface.glass() : 0.7F) * searchAlpha)
         );

         this.searchField.render(context);
         this.appendingAnim.update(!this.searchField.getAppending().isBlank());
         context.drawCenteredText(
            Fonts.MEDIUM.getFont(11.0F),
            Localizator.translate("search.tooltip.tab"),
            this.width / 2.0F,
            this.height - 65 - 10.0F * searchAlpha * this.appendingAnim.getValue(),
            ColorRGBA.WHITE.withAlpha(150.0F * searchAlpha * this.appendingAnim.getValue())
         );
         context.drawCenteredText(
            Fonts.MEDIUM.getFont(11.0F),
            Localizator.translate("search.tooltip.enter"),
            this.width / 2.0F,
            this.height - 50 - 10.0F * searchAlpha * this.appendingAnim.getValue(),
            ColorRGBA.WHITE.withAlpha(150.0F * searchAlpha * this.appendingAnim.getValue())
         );
      } else {
         this.searchField.clear();
      }

      context.popMatrix();

      if (this.menuAnimation.getValue() < 0.5F) {
         this.desc = "";
      }

      context.drawCenteredText(
         Fonts.MEDIUM.getFont(11.0F),
         Localizator.translate("search.tooltip"),
         this.width / 2.0F,
         this.height - 20 - 10.0F * this.menuAnimation.getValue() * (1.0F - this.searchAnimation.getValue()),
         ColorRGBA.WHITE.withAlpha(150.0F * this.menuAnimation.getValue() * (1.0F - this.searchAnimation.getValue()))
      );
      this.descText.pos(this.width / 2.0F, this.height / 2.0F - 150.0F);
      if (!this.desc.contains(".description")) {
         this.descText.update(this.desc);
         this.descText.render(context);
      }

      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.render(context);
         if (!(mc.currentScreen instanceof DropDownScreen)) {
            colorPicker.setShowing(false);
         }
      }

      this.colorPickers.removeIf(popup -> popup.getAnimation().getValue() == 0.0F && !popup.isShowing());
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

   public boolean isBindingModule() {
      return this.panels.stream().flatMap(panel -> panel.getModuleComponents().stream()).anyMatch(ModuleComponent::isBindingMode);
   }

   private void scissor(UIContext context, MenuPanel panel, Runnable runnable) {
      panel.scale(context);
      panel.push(context);
      runnable.run();
      ScissorUtility.pop();
      RenderUtility.end(context.getMatrices());
   }

   @Compile
   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      if (!elegant.getInstance().getHud().getIsland().handleClick((float)mouseX, (float)mouseY, button.getButtonIndex())) {
         for (ColorPicker colorPicker : this.colorPickers) {
            boolean isPick = colorPicker.isPick();
            colorPicker.onMouseClicked(mouseX, mouseY, button);
            if (colorPicker.isHovered(mouseX, mouseY) || isPick) {
               return;
            }

            colorPicker.setShowing(false);
         }

         for (MenuPanel panel : this.panels) {
            if (panel.isHovered(mouseX, mouseY)) {
               panel.onMouseClicked(mouseX, mouseY, button);
            }
         }

         if (this.searchField.isFocused() && button != MouseButton.MIDDLE) {
            this.searchField.onMouseClicked(mouseX, mouseY, button);
         }

         super.onMouseClicked(mouseX, mouseY, button);
      }
   }

   @Compile
   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.onMouseReleased(mouseX, mouseY, button);
      }

      for (MenuPanel panel : this.panels) {
         panel.onMouseReleased(mouseX, mouseY, button);
      }

      if (this.searchField.isFocused()) {
         this.searchField.onMouseReleased(mouseX, mouseY, button);
      }

      super.onMouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (MenuPanel panel : this.panels) {
         panel.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Compile
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.onKeyPressed(keyCode, scanCode, modifiers);
      }

      if (this.searchField != null && !this.searchField.isFocused() && Screen.hasControlDown() && keyCode == 70) {
         this.searchField.setFocused(true);
      }

      for (MenuPanel panel : this.panels) {
         panel.onKeyPressed(keyCode, scanCode, modifiers);
      }

      if (this.searchField.isFocused() && !this.isBindingModule()) {
         this.searchField.onKeyPressed(keyCode, scanCode, modifiers);
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Compile
   public boolean charTyped(char chr, int modifiers) {
      if (this.searchField.isFocused() && !this.isBindingModule()) {
         this.searchField.charTyped(chr, modifiers);
      }

      for (MenuPanel panel : this.panels) {
         panel.charTyped(chr, modifiers);
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
   public Animation getSearchAnimation() {
      return this.searchAnimation;
   }

   @Generated
   public Animation getAppendingAnim() {
      return this.appendingAnim;
   }

   @Generated
   @Override
   public boolean isClosing() {
      return this.closing;
   }

   @Generated
   public List<MenuPanel> getPanels() {
      return this.panels;
   }

   @Generated
   public float getPanelWidth() {
      return this.panelWidth;
   }

   @Generated
   public float getPanelHeight() {
      return this.panelHeight;
   }

   @Generated
   public String getDesc() {
      return this.desc;
   }

   @Generated
   public AnimatedText getDescText() {
      return this.descText;
   }

   @Generated
   public List<ColorPicker> getColorPickers() {
      return this.colorPickers;
   }

   @Generated
   @Override
   public void setClosing(boolean closing) {
      this.closing = closing;
   }

   @Generated
   public void setDesc(String desc) {
      this.desc = desc;
   }

   @Generated
   public TextField getSearchField() {
      return this.searchField;
   }

   static {
      new MenuPanel(null);
      new BezierSettingComponent(null, null);
      new BindSettingComponent(null, null);
      new BooleanSettingComponent(null, null);
      new ModeSettingComponent(null, null);
      new ButtonSettingComponent(null, null);
      new ColorSettingComponent(null, null);
      new StringSettingComponent(null, null);
      new RangeSettingComponent(null, null);
      new SliderSettingComponent(null, null);
   }
}
