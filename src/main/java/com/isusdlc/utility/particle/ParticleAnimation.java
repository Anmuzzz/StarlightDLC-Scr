package com.isusdlc.utility.particle;

public class ParticleAnimation {
   private long start;
   private double duration;
   private double fromValue;
   private double toValue;
   private double value;
   private double prevValue;
   private ParticleEasing easing = ParticleEasings.LINEAR;
   private Runnable finishAction;

   public ParticleAnimation run(double valueTo, double duration) {
      return this.run(valueTo, duration, ParticleEasings.LINEAR, false);
   }

   public ParticleAnimation run(double valueTo, double duration, ParticleEasing easing) {
      return this.run(valueTo, duration, easing, false);
   }

   public ParticleAnimation run(double valueTo, double duration, boolean safe) {
      return this.run(valueTo, duration, ParticleEasings.LINEAR, safe);
   }

   public ParticleAnimation run(double valueTo, double duration, ParticleEasing easing, boolean safe) {
      if (!this.check(safe, valueTo)) {
         this.setEasing(easing).setDuration(duration * 1000.0).setStart(System.currentTimeMillis()).setFromValue(this.getValue()).setToValue(valueTo);
      }

      return this;
   }

   public boolean update() {
      this.setPrevValue(this.getValue());
      boolean alive = this.isAlive();
      if (alive) {
         this.setValue(this.interpolate(this.getFromValue(), this.getToValue(), this.getEasing().ease(this.calculatePart())));
      } else {
         this.setStart(0L);
         this.setValue(this.getToValue());
         if (this.finishAction != null) {
            this.finishAction.run();
            this.finishAction = null;
         }
      }

      return alive;
   }

   public boolean isAlive() {
      return !this.isFinished();
   }

   public boolean isFinished() {
      return this.calculatePart() >= 1.0;
   }

   public double calculatePart() {
      return (System.currentTimeMillis() - this.getStart()) / this.getDuration();
   }

   public boolean check(boolean safe, double valueTo) {
      return safe && this.isAlive() && (valueTo == this.getFromValue() || valueTo == this.getToValue() || valueTo == this.getValue());
   }

   public double interpolate(double start, double end, double pct) {
      return start + (end - start) * pct;
   }

   public ParticleAnimation setStart(long start) {
      this.start = start;
      return this;
   }

   public ParticleAnimation setDuration(double duration) {
      this.duration = duration;
      return this;
   }

   public ParticleAnimation setFromValue(double fromValue) {
      this.fromValue = fromValue;
      return this;
   }

   public ParticleAnimation setToValue(double toValue) {
      this.toValue = toValue;
      return this;
   }

   public ParticleAnimation setValue(double value) {
      this.value = value;
      return this;
   }

   public ParticleAnimation setPrevValue(double prevValue) {
      this.prevValue = prevValue;
      return this;
   }

   public ParticleAnimation setEasing(ParticleEasing easing) {
      this.easing = easing;
      return this;
   }

   public ParticleAnimation onFinished(Runnable action) {
      this.finishAction = action;
      return this;
   }

   public float get() {
      return (float)this.getValue();
   }

   public float getPrev() {
      return (float)this.getPrevValue();
   }

   public void set(double value) {
      this.run(value, 0.0);
      this.update();
      this.setValue(value);
   }

   public long getStart() {
      return this.start;
   }

   public double getDuration() {
      return this.duration;
   }

   public double getFromValue() {
      return this.fromValue;
   }

   public double getToValue() {
      return this.toValue;
   }

   public double getValue() {
      return this.value;
   }

   public double getPrevValue() {
      return this.prevValue;
   }

   public ParticleEasing getEasing() {
      return this.easing;
   }

   public Runnable getFinishAction() {
      return this.finishAction;
   }
}
