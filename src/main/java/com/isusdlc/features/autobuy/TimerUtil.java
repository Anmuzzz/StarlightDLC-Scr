package com.isusdlc.features.autobuy;

public class TimerUtil {
   private long lastMS = System.currentTimeMillis();

   public TimerUtil() {
      this.resetCounter();
   }

   public static TimerUtil create() {
      return new TimerUtil();
   }

   public void resetCounter() {
      this.lastMS = System.currentTimeMillis();
   }

   public boolean isReached(long ms) {
      return System.currentTimeMillis() - this.lastMS > ms;
   }

   public void setLastMS(long ms) {
      this.lastMS = System.currentTimeMillis() + ms;
   }

   public void setTime(long time) {
      this.lastMS = time;
   }

   public long getTime() {
      return System.currentTimeMillis() - this.lastMS;
   }

   public boolean isRunning() {
      return System.currentTimeMillis() - this.lastMS <= 0L;
   }

   public boolean hasTimeElapsed(long ms) {
      return System.currentTimeMillis() - this.lastMS > ms;
   }

   public boolean hasTimeElapsed() {
      return this.lastMS < System.currentTimeMillis();
   }

   public long getLastMS() {
      return this.lastMS;
   }
}
