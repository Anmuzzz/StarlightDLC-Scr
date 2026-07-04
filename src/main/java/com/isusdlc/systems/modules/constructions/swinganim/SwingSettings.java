package com.isusdlc.systems.modules.constructions.swinganim;

import java.util.ArrayList;
import java.util.List;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.setting.SettingsContainer;

public class SwingSettings implements SettingsContainer {
   protected final List<Setting> settings = new ArrayList<>();

   @Override
   public List<Setting> getSettings() {
      return this.settings;
   }
}
