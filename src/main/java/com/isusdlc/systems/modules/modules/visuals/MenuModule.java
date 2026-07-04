package com.isusdlc.systems.modules.modules.visuals;

import com.isusdlc.elegant;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.modules.modules.other.Sounds;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.ui.menu.MenuScreen;
import com.isusdlc.ui.menu.api.MenuCloseListener;
import com.isusdlc.utility.sounds.ClientSounds;

@ModuleInfo(
   name = "Menu",
   category = ModuleCategory.VISUALS,
   key = 344,
   desc = "modules.descriptions.menu"
)
public class MenuModule extends BaseModule {
   private static final MenuCloseListener menuCloseListener = new MenuCloseListener();
   private final ModeSetting mode = new ModeSetting(this, "modules.settings.menu.mode");
   private final ModeSetting.Value dropdown = new ModeSetting.Value(this.mode, "modules.settings.menu.mode.dropdown");

   @Override
   public void onEnable() {
      if (!(mc.currentScreen instanceof MenuScreen)) {
         MenuScreen menuScreen = elegant.getInstance().getMenuScreen();
         mc.setScreen(menuScreen);
         Sounds soundsModule = elegant.getInstance().getModuleManager().getModule(Sounds.class);
         if (soundsModule.isEnabled()) {
            ClientSounds.CLICKGUI_OPEN.play(soundsModule.getVolume().getCurrentValue());
         }

         super.onEnable();
      }
   }

   @Override
   public void onDisable() {
      if (mc.currentScreen instanceof MenuScreen) {
         mc.setScreen(null);
         elegant.getInstance().getMenuScreen().setClosing(true);
      }

      super.onDisable();
   }
}
