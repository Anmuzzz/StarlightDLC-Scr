package com.isusdlc.ui.menu.modern;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.msdf.Fonts;
import com.isusdlc.framework.objects.BorderRadius;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.modules.Module;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.systems.modules.modules.visuals.MenuModule;
import com.isusdlc.systems.theme.Theme;
import com.isusdlc.ui.components.ColorPicker;
import com.isusdlc.ui.components.textfield.FieldAction;
import com.isusdlc.ui.components.textfield.TextField;
import com.isusdlc.ui.menu.MenuScreen;
import com.isusdlc.ui.menu.api.MenuCategory;
import com.isusdlc.ui.menu.dropdown.components.MenuPanel;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BezierSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BindSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.BooleanSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ButtonSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ColorSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.ModeSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.RangeSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.SliderSettingComponent;
import com.isusdlc.ui.menu.dropdown.components.settings.impl.StringSettingComponent;
import com.isusdlc.ui.menu.modern.components.ModernModule;
import com.isusdlc.ui.menu.modern.components.ModernSettings;
import com.isusdlc.utility.animation.base.Animation;
import com.isusdlc.utility.animation.base.Easing;
import com.isusdlc.utility.colors.Colors;
import com.isusdlc.utility.game.cursor.CursorType;
import com.isusdlc.utility.game.cursor.CursorUtility;
import com.isusdlc.utility.gui.GuiUtility;
import com.isusdlc.utility.gui.ScrollHandler;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.interfaces.IScaledResolution;
import com.isusdlc.utility.render.RenderUtility;
import com.isusdlc.utility.render.ScissorUtility;
import com.isusdlc.utility.render.obj.Rect;
import com.isusdlc.utility.render.penis.PenisPlayer;
import com.isusdlc.utility.sounds.ClientSounds;
import com.isusdlc.utility.time.Timer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.gui.screen.Screen;
import ru.kotopushka.compiler.sdk.annotations.Compile;

public class ModernScreen extends MenuScreen implements IMinecraft, IScaledResolution {
   private final Rect menuWindow;
   private float dragX;
   private float dragY;
   private boolean drag;
   private final ScrollHandler scrollHandler = new ScrollHandler();
   private MenuCategory current = MenuCategory.COMBAT;
   private final List<ColorPicker> colorPickers = new LinkedList<>();
   private final List<ModernCategory> categories = new ArrayList<>();
   private final List<ModernSettings> windows = new LinkedList<>();
   private final Animation currentCategory = new Animation(300L, Easing.BAKEK_SMALLER);
   private final TextField searchField;
   private final PenisPlayer searchPenis;
   private boolean prevFocused;
   Timer timer = new Timer();

   public ModernScreen() {
      float width = 636.0F;
      float height = 320.0F;
      this.menuWindow = new Rect(sr.getScaledWidth() / 2.0F - width / 2.0F, sr.getScaledHeight() / 2.0F - height / 2.0F, width, height);
      this.categories.clear();

      for (MenuCategory category : MenuCategory.values()) {
         List<ModernModule> filteredModules = new LinkedList<>();
         ModernCategory modern = new ModernCategory(category, filteredModules);

         try {
            modern.setPenis(new PenisPlayer(elegant.id("penises/" + category.getName().toLowerCase() + ".penis")));
         } catch (RuntimeException var10) {
         }

         this.categories.add(modern);
         List<Module> modulesInCategory = elegant.getInstance()
            .getModuleManager()
            .getModules()
            .stream()
            .filter(module -> module.getCategory().equals(category.getCategory()))
            .filter(module -> !module.isHidden())
            .toList();

         modulesInCategory.stream()
            .sorted((a, b) -> Integer.compare(a.getName().hashCode(), b.getName().hashCode()))
            .forEach(module -> filteredModules.add(new ModernModule(module, modern)));
      }

      this.searchField = new TextField(Fonts.MEDIUM.getFont(6.0F));
      Map<String, FieldAction> append = new HashMap<>();

      for (Module module : elegant.getInstance().getModuleManager().getModules()) {
         if (!module.isHidden()) {
            FieldAction action = new FieldAction(
               module::toggle,
               () -> this.categories
                  .forEach(
                     panel -> panel.getModules()
                        .stream()
                        .filter(component -> component.getModule() == module)
                        .forEach(modernModule -> System.out.println("poka pichego"))
                  )
            );
            append.put(module.getName().replace(" ", ""), action);
            append.put(module.getName(), action);
         }
      }

      this.searchField.setAppend(append);
      this.searchField.setPreview("Поиск");
      this.searchPenis = new PenisPlayer(elegant.id("penises/search.penis"));
      this.searchPenis.stop();
   }

   @Compile
   protected void init() {
      this.closing = false;

      for (ModernCategory category : this.categories) {
         if (category.getPenis() != null) {
            category.getPenis().stop();
         }
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
      if (this.drag) {
         this.menuWindow.setX(context.getMouseX() - this.dragX);
         this.menuWindow.setY(context.getMouseY() - this.dragY);
      }

      if (this.searchField.isFocused() && !this.prevFocused) {
         this.searchPenis.playOnce();
      }

      this.prevFocused = this.searchField.isFocused();
      float scroll = (float)(-this.scrollHandler.getValue());
      float alpha = Math.min(1.0F, this.menuAnimation.getValue());

      for (ModernCategory category : this.categories) {
         if (category.getY() - scroll <= -this.scrollHandler.getTargetValue() && this.current != category.getCategory()) {
            this.current = category.getCategory();
         }
      }

      boolean dark = elegant.getInstance().getThemeManager().getCurrentTheme() == Theme.DARK;
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
      RenderUtility.scale(
         context.getMatrices(),
         this.menuWindow.getX() + this.menuWindow.getWidth() / 2.0F,
         this.menuWindow.getY() + this.menuWindow.getHeight() / 2.0F,
         1.0F
      );
      float x = this.menuWindow.getX();
      float y = this.menuWindow.getY();
      float yOff = 0.0F;
      float xOff = 0.0F;
      float moduleWidth = 177.0F;
      yOff = 0.0F;
      float startX = x + 6.0F;
      float baseTop = y + 12.0F;
      float columnWidth = 128.0F;
      float columnSpacing = 6.0F;
      float headerHeight = 16.0F;
      float maxBottomOffset = 0.0F;
      float colTopWithScroll = baseTop + scroll;
      ScissorUtility.push(
         context.getMatrices(), this.menuWindow.getX(), this.menuWindow.getY() + 1.0F, this.menuWindow.getWidth(), this.menuWindow.getHeight() - 2.0F
      );

      int index = 0;
      for (ModernCategory categoryx : this.categories) {
         float colX = startX + index * (columnWidth + columnSpacing);
         float colY = colTopWithScroll;
         float colHeight = 280.0F;

         context.drawBlurredRect(colX, colY, columnWidth, colHeight, 30.0F, BorderRadius.all(10.0F), Colors.WHITE);
         context.drawRoundedRect(
            colX,
            colY,
            columnWidth,
            colHeight,
            BorderRadius.all(10.0F),
            Colors.getBackgroundColor().mulAlpha(dark ? 0.96F : 0.82F)
         );

         context.drawRoundedRect(
            colX,
            colY,
            columnWidth,
            headerHeight,
            BorderRadius.all(10.0F),
            Colors.getBackgroundColor().mix(Colors.BLACK, dark ? 0.45F : 0.3F).mulAlpha(0.98F)
         );
         context.drawText(
            Fonts.SEMIBOLD.getFont(7.5F),
            categoryx.getCategory().getName(),
            colX + 8.0F,
            colY + 5.0F,
            Colors.getTextColor()
         );

         float moduleY = colY + headerHeight + 4.0F;
         categoryx.setY(colY - baseTop);

         for (ModernModule module : categoryx.getModules()) {
            boolean cond = !this.opened(module);
            module.getVisible().update(cond);
            module.getOffset().update(cond);
            if (!this.visibleCheck(module)) {
               module.set(colX + 4.0F, moduleY, columnWidth - 8.0F, 18.0F);
               if (GuiUtility.isHovered(
                  (double)x,
                  (double)(y - module.getHeight()),
                  (double)this.menuWindow.getWidth(),
                  (double)(this.menuWindow.getHeight() + module.getHeight()),
                  (double)module.getX(),
                  (double)module.getY()
               )) {
                  module.render(context);
                  module.renderRounds(context);
                  module.renderInto(context);
                  module.renderMedium(context);
                  module.renderRegular(context);
                  if (GuiUtility.isHovered(module.getX(), module.getY(), module.getWidth(), module.getHeight(), context)) {
                     CursorUtility.set(CursorType.HAND);
                  }
               }

               moduleY += 20.0F * module.getOffset().getValue();
            }
         }

         float columnBottomOffset = moduleY - baseTop;
         if (columnBottomOffset > maxBottomOffset) {
            maxBottomOffset = columnBottomOffset;
         }

         ++index;
      }

      float totalContentHeight = maxBottomOffset;
      float visibleHeight = this.menuWindow.getHeight() - 40.0F;
      float maxScroll = -Math.max(0.0F, totalContentHeight - visibleHeight);
      this.scrollHandler.setMax(maxScroll - 10.0F);
      ScissorUtility.pop();
      RenderUtility.end(context.getMatrices());
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      for (ModernSettings window : this.windows) {
         window.render(context);
      }

      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.render(context);
      }

      this.windows.removeIf(window -> window.getAnimation().getValue() == 0.0F && !window.isShowing());
      this.colorPickers.removeIf(colorPickerx -> colorPickerx.getAnimation().getValue() == 0.0F && !colorPickerx.isShowing());
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
         for (ColorPicker colorPicker : this.colorPickers) {
            boolean isPick = colorPicker.isPick();
            colorPicker.onMouseClicked(mouseX, mouseY, button);
            if (colorPicker.isHovered(mouseX, mouseY) || isPick) {
               return;
            }

            colorPicker.setShowing(false);
         }

         for (ModernSettings window : this.windows) {
            window.onMouseClicked(mouseX, mouseY, button);
            if (window.isHovered(mouseX, mouseY)) {
               return;
            }

            if (!GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
               boolean can = true;

               for (ModernSettings window1 : this.windows) {
                  if (GuiUtility.isHovered(window1, mouseX, mouseY)) {
                     can = false;
                  }
               }

               if (can) {
                  window.setShowing(false);
               }
            }
         }

         float x = this.menuWindow.getX();
         float y = this.menuWindow.getY();
         float yOff = 0.0F;
         float xOff = 0.0F;

         for (ModernCategory category : this.categories) {
         if (GuiUtility.isHovered((double)(x + 15.0F), (double)(y + 68.0F + yOff), 100.0, 18.0, mouseX, mouseY) && category.getCategory() != this.current) {
               this.scrollHandler.scroll((-this.scrollHandler.getValue() - (category.getY() - this.scrollHandler.getValue())) / 20.0);
               if (category.getPenis() != null) {
                  category.getPenis().playOnce();
               }

               return;
            }

            yOff += 20.0F;
         }

         for (ModernCategory category : this.categories) {
            for (ModernModule module : category.getModules()) {
               if (!this.visibleCheck(module)
                  && (GuiUtility.isHovered(this.menuWindow, mouseX, mouseY) || button != MouseButton.LEFT && button != MouseButton.RIGHT)
                  && GuiUtility.isHovered((double)module.getX(), (double)module.getY(), (double)module.getWidth(), (double)module.getHeight(), mouseX, mouseY)) {
                  module.onMouseClicked(mouseX, mouseY, button);
                  return;
               }
            }
         }

         if (button != MouseButton.MIDDLE) {
            this.searchField.onMouseClicked(mouseX, mouseY, button);
         }

         if (GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
            this.drag = true;
            this.dragX = (float)(mouseX - this.menuWindow.getX());
            this.dragY = (float)(mouseY - this.menuWindow.getY());
         }

         super.onMouseClicked(mouseX, mouseY, button);
      }
   }

   @Compile
   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      this.drag = false;

      for (ModernSettings window : this.windows) {
         window.onMouseReleased(mouseX, mouseY, button);
      }

      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.onMouseReleased(mouseX, mouseY, button);
      }

      if (this.searchField.isFocused()) {
         this.searchField.onMouseReleased(mouseX, mouseY, button);
      }

      super.onMouseReleased(mouseX, mouseY, button);
   }

   @Compile
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      for (ModernSettings window : this.windows) {
         window.onScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
      }

      if (GuiUtility.isHovered(this.menuWindow, mouseX, mouseY)) {
         this.scrollHandler.scroll(verticalAmount);
      }

      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Compile
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (!this.searchField.isFocused() && Screen.hasControlDown() && keyCode == 70) {
         this.searchField.setFocused(true);
      }

      this.scrollHandler.onKeyPressed(keyCode);

      for (ModernSettings window : this.windows) {
         window.onKeyPressed(keyCode, scanCode, modifiers);
      }

      for (ColorPicker colorPicker : this.colorPickers) {
         colorPicker.onKeyPressed(keyCode, scanCode, modifiers);
      }

      if (this.searchField.isFocused() && !this.isBindingModule()) {
         this.searchField.onKeyPressed(keyCode, scanCode, modifiers);
      }

      for (ModernCategory category : this.categories) {
         for (ModernModule module : category.getModules()) {
            if (!this.visibleCheck(module)) {
               module.onKeyPressed(keyCode, scanCode, modifiers);
            }
         }
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Compile
   public boolean charTyped(char chr, int modifiers) {
      if (this.searchField.isFocused() && !this.isBindingModule()) {
         this.searchField.charTyped(chr, modifiers);
      }

      for (ModernSettings window : this.windows) {
         window.charTyped(chr, modifiers);
      }

      for (ModernCategory category : this.categories) {
         for (ModernModule module : category.getModules()) {
            if (!this.visibleCheck(module)) {
               module.charTyped(chr, modifiers);
            }
         }
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

   private boolean searchCheck(ModernModule component) {
      TextField search = this.searchField;
      return search != null
         && !search.getBuiltText().isBlank()
         && !component.getModule().getName().toLowerCase().contains(search.getBuiltText().toLowerCase())
         && !component.getModule().getName().replace(" ", "").toLowerCase().contains(search.getBuiltText().toLowerCase());
   }

   private boolean visibleCheck(ModernModule component) {
      return component.getOffset().getValue() == 0.0F || this.searchCheck(component) || component.getModule().isHidden();
   }

   private boolean opened(ModernModule component) {
      return this.windows.stream().anyMatch(window -> window.getModule() == component);
   }

   public boolean isBindingModule() {
      return this.categories.stream().flatMap(panel -> panel.getModules().stream()).anyMatch(ModernModule::isBinding);
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
   public float getDragX() {
      return this.dragX;
   }

   @Generated
   public float getDragY() {
      return this.dragY;
   }

   @Generated
   public boolean isDrag() {
      return this.drag;
   }

   @Generated
   public ScrollHandler getScrollHandler() {
      return this.scrollHandler;
   }

   @Generated
   public MenuCategory getCurrent() {
      return this.current;
   }

   @Generated
   public List<ColorPicker> getColorPickers() {
      return this.colorPickers;
   }

   @Generated
   public List<ModernCategory> getCategories() {
      return this.categories;
   }

   @Generated
   public List<ModernSettings> getWindows() {
      return this.windows;
   }

   @Generated
   public Animation getCurrentCategory() {
      return this.currentCategory;
   }

   @Generated
   public TextField getSearchField() {
      return this.searchField;
   }

   @Generated
   public PenisPlayer getSearchPenis() {
      return this.searchPenis;
   }

   @Generated
   public boolean isPrevFocused() {
      return this.prevFocused;
   }

   @Generated
   public Timer getTimer() {
      return this.timer;
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
