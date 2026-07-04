package com.isusdlc.systems.modules.constructions.swinganim.presets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import com.isusdlc.utility.interfaces.IMinecraft;

public class SwingPresetManager implements IMinecraft {
   private final List<SwingPresetFile> swingPresetFiles = new ArrayList<>();
   private SwingPresetFile current;

   public void handle() {
      this.refresh();
      if (this.getAutoSavePreset() == null) {
         SwingPresetFile autosave = new SwingPresetFile("autosave");
         this.swingPresetFiles.add(autosave);
         this.current = autosave;
      }
   }

   public void refresh() {
      File presetsFolder = new File(mc.runDirectory, "elegant/presets/swing");
      if (!presetsFolder.exists()) {
         presetsFolder.mkdirs();
      } else {
         File[] files = presetsFolder.listFiles((dir, name) -> name.endsWith(".myth"));
         if (files != null) {
            for (File file : files) {
               String fileName = file.getName().replace(".myth", "");
               boolean exists = this.swingPresetFiles.stream().anyMatch(presetx -> presetx.getFileName().equals(fileName));
               if (!exists) {
                  SwingPresetFile preset = new SwingPresetFile(fileName);
                  this.swingPresetFiles.add(preset);
               }
            }
         }
      }
   }

   public void createPreset(String name) {
      SwingPresetFile preset = new SwingPresetFile(name);
      this.swingPresetFiles.add(preset);
      preset.save();
   }

   public SwingPresetFile getPreset(String name) {
      return this.swingPresetFiles.stream().filter(preset -> preset.getFileName().equals(name)).findFirst().orElse(null);
   }

   public SwingPresetFile getAutoSavePreset() {
      return this.current != null
         ? this.current
         : this.swingPresetFiles.stream().filter(preset -> preset.getFileName().equals("autosave")).findFirst().orElse(null);
   }

   @Generated
   public List<SwingPresetFile> getSwingPresetFiles() {
      return this.swingPresetFiles;
   }

   @Generated
   public SwingPresetFile getCurrent() {
      return this.current;
   }

   @Generated
   public void setCurrent(SwingPresetFile current) {
      this.current = current;
   }
}
