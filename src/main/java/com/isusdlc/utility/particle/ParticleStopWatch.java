package com.isusdlc.utility.particle;

public class ParticleStopWatch {
   private long startTime;

   public ParticleStopWatch() {
      this.reset();
   }

   public boolean finished(double delay) {
      return System.currentTimeMillis() - delay >= this.startTime;
   }

   public boolean every(double delay) {
      boolean finished = this.finished(delay);
      if (finished) {
         this.reset();
      }

      return finished;
   }

   public void reset() {
      this.startTime = System.currentTimeMillis();
   }

   public long elapsedTime() {
      return System.currentTimeMillis() - this.startTime;
   }

   public void setMs(long ms) {
      this.startTime = System.currentTimeMillis() - ms;
   }
}
