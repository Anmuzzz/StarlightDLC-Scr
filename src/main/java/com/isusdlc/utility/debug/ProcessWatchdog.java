package com.isusdlc.utility.debug;

import com.isusdlc.elegant;

public class ProcessWatchdog {
   private static volatile boolean shutdownInitiated = false;
   private static Thread watchdogThread;

   public static void startWatchdog() {
      if (watchdogThread == null || !watchdogThread.isAlive()) {
         watchdogThread = new Thread(() -> {
            try {
               while (true) {
                  if (shutdownInitiated) {
                     elegant.LOGGER.info("Watchdog: Shutdown initiated, starting countdown");

                     for (int i = 5; i > 0; i--) {
                        Thread.sleep(1000L);
                     }

                     elegant.LOGGER.error("Watchdog: Normal shutdown failed, forcing halt");
                     Runtime.getRuntime().halt(2);
                     break;
                  }

                  Thread.sleep(1000L);
               }
            } catch (InterruptedException var1) {
               Thread.currentThread().interrupt();
               elegant.LOGGER.info("Watchdog: Interrupted, exiting");
            } catch (Exception var2) {
               elegant.LOGGER.error("Watchdog: Error occurred, forcing halt", var2);
               Runtime.getRuntime().halt(3);
            }
         });
         watchdogThread.setDaemon(true);
         watchdogThread.setName("Process-Watchdog");
         watchdogThread.start();
      }
   }

   public static void triggerShutdown() {
      shutdownInitiated = true;
   }

   public static void stopWatchdog() {
      if (watchdogThread != null && watchdogThread.isAlive()) {
         watchdogThread.interrupt();
      }
   }
}
