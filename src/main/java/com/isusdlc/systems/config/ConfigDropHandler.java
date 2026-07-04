package com.isusdlc.systems.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import lombok.Generated;
import com.isusdlc.elegant;
import com.isusdlc.systems.file.FileManager;
import com.isusdlc.systems.notifications.NotificationType;
import com.isusdlc.utility.game.MessageUtility;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWDropCallback;
import org.lwjgl.glfw.GLFWDropCallbackI;
import ru.kotopushka.compiler.sdk.annotations.Compile;
import ru.kotopushka.compiler.sdk.annotations.Initialization;

public final class ConfigDropHandler implements IMinecraft {
   private static boolean initialized;

   @Compile
   @Initialization
   public static void init() {
      if (!initialized) {
         initialized = true;
         if (mc.getWindow() == null) {
            return;
         }
         long handle = mc.getWindow().getHandle();
         GLFWDropCallbackI[] previous = new GLFWDropCallbackI[1];
         GLFWDropCallbackI callback = (window, count, names) -> {
            if (previous[0] != null) {
               previous[0].invoke(window, count, names);
            }

            for (int i = 0; i < count; i++) {
               String path = GLFWDropCallback.getName(names, i);
               handleDrop(path);
            }
         };
         previous[0] = GLFW.glfwSetDropCallback(handle, callback);
      }
   }

   private static void handleDrop(String path) {
      try {
         File src = new File(path);
         if (!src.isFile()) {
            return;
         }

         if (!src.getName().endsWith(".myth")) {
            return;
         }

         File destDir = new File(FileManager.DIRECTORY, "configs");
         if (!destDir.exists() && !destDir.mkdirs()) {
            elegant.LOGGER.error("Failed to create directory {}", destDir.getAbsolutePath());
            return;
         }

         File dest = new File(destDir, src.getName());
         Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
         String name = src.getName().substring(0, src.getName().lastIndexOf(46));
         ConfigManager manager = elegant.getInstance().getConfigManager();
         manager.refresh();
         ConfigFile cfg = manager.getConfig(name);
         if (cfg == null) {
            cfg = new ConfigFile(name);
            manager.getConfigFiles().add(cfg);
         }

         cfg.load();
         MessageUtility.info(Text.of("Конфиг " + name + " загружен"));
         elegant.getInstance().getNotificationManager().addNotification(NotificationType.SUCCESS, Text.translatable("configs.loaded").getString());
      } catch (Exception var71) {
         elegant.LOGGER.error("Failed to load dropped config {}", path, var71);
      }
   }

   @Generated
   private ConfigDropHandler() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
