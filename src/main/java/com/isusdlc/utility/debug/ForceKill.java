package com.isusdlc.utility.debug;

import java.util.concurrent.TimeUnit;
import com.isusdlc.elegant;

public class ForceKill {
   public static void forceKillProcess() {
      try {
         long pid = ProcessHandle.current().pid();
         elegant.LOGGER.info("Force killing process with PID: {}", pid);

         try {
            ProcessBuilder pb = new ProcessBuilder("taskkill", "/F", "/PID", String.valueOf(pid));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor(1000L, TimeUnit.MILLISECONDS);
            elegant.LOGGER.info("Taskkill command executed");
         } catch (Exception var5) {
            elegant.LOGGER.warn("Taskkill failed: {}", var5.getMessage());
         }

         try {
            ProcessHandle.current().destroy();
            Thread.sleep(100L);
            ProcessHandle.current().destroyForcibly();
            elegant.LOGGER.info("ProcessHandle destroy executed");
         } catch (Exception var4) {
            elegant.LOGGER.warn("ProcessHandle destroy failed: {}", var4.getMessage());
         }

         System.exit(-1);
      } catch (Exception var6) {
         elegant.LOGGER.error("Failed to force kill process", var6);
         Runtime.getRuntime().halt(-1);
      }
   }

   public static void scheduleForceKill(int delayMs) {
      Thread killer = new Thread(() -> {
         try {
            Thread.sleep(delayMs);
            elegant.LOGGER.error("Force kill timer expired, terminating process");
            forceKillProcess();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            forceKillProcess();
         }
      });
      killer.setDaemon(false);
      killer.setName("Force-Kill-Timer");
      killer.start();
   }
}
