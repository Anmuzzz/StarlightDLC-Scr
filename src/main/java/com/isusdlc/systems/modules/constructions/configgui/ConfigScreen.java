package com.isusdlc.systems.modules.constructions.configgui;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomScreen;
import com.isusdlc.framework.base.UIContext;
import com.isusdlc.framework.objects.MouseButton;
import com.isusdlc.systems.config.ConfigFile;
import com.isusdlc.systems.config.ConfigManager;
import com.isusdlc.systems.setting.SettingsContainer;
import com.isusdlc.systems.setting.settings.ButtonSetting;
import com.isusdlc.systems.setting.settings.StringSetting;
import com.isusdlc.ui.components.popup.Popup;
import com.isusdlc.ui.components.popup.list.Button;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreen extends CustomScreen implements IMinecraft {

   private Popup popup;
   private ConfigFile selected;
   private final SettingsContainer container = new SettingsContainer() {
      private final List<com.isusdlc.systems.setting.Setting> list = new ArrayList<>();
      @Override
      public List<com.isusdlc.systems.setting.Setting> getSettings() { return list; }
   };

   @Override
   public void init() {
      super.init();
      rebuild();
   }

   private void rebuild() { rebuild(null); }

   private void rebuild(String presetName) {
      ConfigManager cm = elegant.getInstance().getConfigManager();
      cm.refresh();

      popup = new Popup(width / 2.0F - 90, 30.0F, 180.0F);
      popup.title("Config Manager");
      popup.separator();

      for (ConfigFile cf : cm.getConfigFiles()) {
         ConfigFile file = cf;
         popup.add(new Button(popup, cf.getFileName(), "icons/search.png", p -> {
            selected = file;
            rebuild(file.getFileName());
         }));
      }

      popup.separator();

      StringSetting nameSetting = new StringSetting(container, "Config Name").text(presetName != null ? presetName : "");
      popup.setting(nameSetting);

      ButtonSetting saveBtn = new ButtonSetting(container, "Save");
      saveBtn.action(() -> {
         String n = nameSetting.getText();
         if (n != null && !n.trim().isEmpty()) {
            cm.refresh();
            ConfigFile cf = cm.getConfig(n.trim(), false);
            if (cf == null) {
               cm.createConfig(n.trim());
               cf = cm.getConfig(n.trim(), false);
            }
            if (cf != null) {
               cf.save();
               rebuild();
            }
         }
      });
      popup.setting(saveBtn);

      ButtonSetting loadBtn = new ButtonSetting(container, "Load");
      loadBtn.action(() -> { if (selected != null) selected.load(); });
      popup.setting(loadBtn);

      ButtonSetting deleteBtn = new ButtonSetting(container, "Delete");
      deleteBtn.action(() -> {
         if (selected != null) {
            selected.delete();
            selected = null;
            rebuild();
         }
      });
      popup.setting(deleteBtn);

      popup.separator();

      ButtonSetting refreshBtn = new ButtonSetting(container, "Refresh");
      refreshBtn.action(() -> rebuild());
      popup.setting(refreshBtn);

      ButtonSetting openBtn = new ButtonSetting(container, "Open Folder");
      openBtn.action(cm::directionConfig);
      popup.setting(openBtn);
   }

   @Override
   public void render(UIContext context) {
      popup.render(context);
   }

   @Override
   public void onMouseClicked(double mouseX, double mouseY, MouseButton button) {
      popup.onMouseClicked(mouseX, mouseY, button);
   }

   @Override
   public void onMouseReleased(double mouseX, double mouseY, MouseButton button) {
      popup.onMouseReleased(mouseX, mouseY, button);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
      popup.onScroll(mouseX, mouseY, horizontal, vertical);
      return super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      popup.onKeyPressed(keyCode, scanCode, modifiers);
      if (keyCode == 256) { close(); return true; }
      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public boolean charTyped(char chr, int modifiers) {
      popup.charTyped(chr, modifiers);
      return super.charTyped(chr, modifiers);
   }

   @Override
   public void close() {
      super.close();
      mc.setScreen(elegant.getInstance().getMenuScreen());
   }

   @Override
   public boolean shouldPause() { return false; }

   public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}
}
