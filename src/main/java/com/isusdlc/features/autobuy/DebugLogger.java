package com.isusdlc.features.autobuy;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DebugLogger {
   private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

   public static void log(String message) {
      String timestamp = LocalDateTime.now().format(formatter);
      String line = "[" + timestamp + "] " + message;
      System.out.println(line);

      try (FileWriter fw = new FileWriter("autobuy_debug.log", true)) {
         fw.write(line + "\n");
         fw.flush();
      } catch (IOException e) {
         System.err.println("Failed to write to log file: " + e.getMessage());
      }
   }

   public static void logError(String message, Exception e) {
      log("ERROR: " + message + " - " + e.getMessage());
      e.printStackTrace();
   }

   public static void logState(String state, String message) {
      log("[" + state + "] " + message);
   }
}
