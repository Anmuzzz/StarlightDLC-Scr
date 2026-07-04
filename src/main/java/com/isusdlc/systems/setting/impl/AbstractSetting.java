package com.isusdlc.systems.setting.impl;

import java.util.function.BooleanSupplier;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.config.ConfigFile;
import com.isusdlc.systems.setting.Setting;
import com.isusdlc.systems.setting.SettingsContainer;
import com.isusdlc.ui.hud.HudElement;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSetting implements Setting {
   private final String name;
   @NotNull
   private final BooleanSupplier hideCondition;
   @NotNull
   private final SettingsContainer parent;

   public AbstractSetting(@NotNull SettingsContainer parent, String name, @NotNull BooleanSupplier hideCondition) {
      this.name = name;
      this.hideCondition = hideCondition;
      this.parent = parent;
      this.register(parent);
   }

   public AbstractSetting(@NotNull SettingsContainer parent, String name) {
      this(parent, name, () -> false);
   }

   @Override
   public void register(SettingsContainer parent) {
      parent.getSettings().add(this);
   }

   @Override
   public String getDescription() {
      return this.getName() + ".description";
   }

   @Generated
   @Override
   public String getName() {
      return this.name;
   }

   @NotNull
   @Generated
   @Override
   public BooleanSupplier getHideCondition() {
      return this.hideCondition;
   }

   @NotNull
   @Generated
   public SettingsContainer getParent() {
      return this.parent;
   }

   protected void autoSaveConfig() {
      try {
         elegant elegant = com.isusdlc.elegant.getInstance();
         if (elegant == null) {
            return;
         }

         if (this.parent instanceof HudElement) {
            if (elegant.getFileManager() != null) {
               elegant.getFileManager().writeFile("client");
            }
         } else if (elegant.getConfigManager() != null) {
            ConfigFile currentConfig = elegant.getConfigManager().getCurrent();
            if (currentConfig != null) {
               currentConfig.save();
            }
         }
      } catch (Exception var3) {
         elegant.LOGGER.error("Error during auto-save for setting '{}': {}", this.name, var3.getMessage());
      }
   }
}
