package com.isusdlc.systems.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.file.FileManager;
import com.isusdlc.utility.game.MessageUtility;
import net.minecraft.text.Text;

public class ConfigManager {
   private final List<ConfigFile> configFiles = new ArrayList<>();
   private ConfigFile current;
   private boolean initialized = false;

   public void handle() {
      if (this.getAutoSaveConfig() == null) {
         this.createConfig("autosave");
      }

      if (!this.initialized) {
         this.scanConfigDirectory();
         this.initialized = true;
      }
   }

   public void directionConfig() {
      try {
         File configDir = new File(FileManager.DIRECTORY, "configs");
         String os = System.getProperty("os.name").toLowerCase();
         String[] commands;
         if (os.contains("win")) {
            commands = new String[]{"explorer", configDir.getAbsolutePath()};
         } else if (os.contains("mac")) {
            commands = new String[]{"open", configDir.getAbsolutePath()};
         } else {
            commands = new String[]{"xdg-open", configDir.getAbsolutePath()};
         }
         Runtime.getRuntime().exec(commands);
      } catch (Exception var31) {
         elegant.LOGGER.error("Не удалось открыть папку с конфигами: {}", var31.getMessage());
      }
   }

   public void createConfig(String name) {
      if (name != null) {
         this.refresh();
         ConfigFile config = new ConfigFile(name);
         config.save();
         this.configFiles.add(config);
      }
   }

   public void listConfigs() {
      this.refresh();
      MessageUtility.info(Text.of("Список конфигов:"));

      for (ConfigFile configFile : this.configFiles) {
         int idx = this.configFiles.indexOf(configFile) + 1;
         MessageUtility.info(Text.of("[" + idx + "] " + configFile.getFileName()));
      }
   }

   private void scanConfigDirectory() {
      this.configFiles.clear();
      Path configPath = Paths.get(FileManager.DIRECTORY.getPath(), "configs");
      if (!Files.exists(configPath)) {
         try {
            Files.createDirectories(configPath);
         } catch (IOException var6) {
            elegant.LOGGER.error("Не удалось создать директорию конфигов: {}", var6.getMessage());
         }
      } else {
         try (Stream<Path> stream = Files.list(configPath)) {
            stream.filter(x$0 -> Files.isRegularFile(x$0)).filter(path -> path.toString().endsWith(".myth")).forEach(path -> {
               String fileName = path.getFileName().toString();
               String name = fileName.substring(0, fileName.lastIndexOf(46));
               ConfigFile configFile = new ConfigFile(name);
               this.configFiles.add(configFile);
            });
         } catch (IOException var81) {
            elegant.LOGGER.error("Ошибка при сканировании директории конфигов: {}", var81.getMessage());
         }
      }
   }

   public ConfigFile getConfig(String name, boolean rescan) {
      if (rescan) {
         this.scanConfigDirectory();
      }

      return this.configFiles.stream().filter(configFile -> configFile.getFileName().equalsIgnoreCase(name)).findFirst().orElse(null);
   }

   public ConfigFile getConfig(String name) {
      return this.getConfig(name, false);
   }

   public ConfigFile getAutoSaveConfig() {
      return this.current != null ? this.current : this.getConfig("autosave", false);
   }

   public void refresh() {
      this.scanConfigDirectory();
   }

   @Generated
   public List<ConfigFile> getConfigFiles() {
      return this.configFiles;
   }

   @Generated
   public ConfigFile getCurrent() {
      return this.current;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setCurrent(ConfigFile current) {
      this.current = current;
   }
}
