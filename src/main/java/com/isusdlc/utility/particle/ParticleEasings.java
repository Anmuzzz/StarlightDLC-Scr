package com.isusdlc.utility.particle;

public final class ParticleEasings {
   public final double c1 = 1.70158;
   public final double c2 = 2.5949095;
   public final double c3 = 2.70158;
   public final double c4 = Math.PI * 2.0 / 3.0;
   public final double c5 = Math.PI * 4.0 / 9.0;
   public static final ParticleEasing LINEAR = value -> value;
   public static final ParticleEasing QUAD_IN = powIn(2);
   public static final ParticleEasing QUAD_OUT = powOut(2);
   public static final ParticleEasing QUAD_IN_OUT = powIN_OUT(2.0);
   public static final ParticleEasing CUBIC_IN = powIn(3);
   public static final ParticleEasing CUBIC_OUT = powOut(3);
   public static final ParticleEasing CUBIC_IN_OUT = powIN_OUT(3.0);
   public static final ParticleEasing QUART_IN = powIn(4);
   public static final ParticleEasing QUART_OUT = powOut(4);
   public static final ParticleEasing QUART_IN_OUT = powIN_OUT(4.0);
   public static final ParticleEasing QUINT_IN = powIn(5);
   public static final ParticleEasing QUINT_OUT = powOut(5);
   public static final ParticleEasing QUINT_IN_OUT = powIN_OUT(5.0);
   public static final ParticleEasing SINE_IN = value -> 1.0 - Math.cos(value * Math.PI / 2.0);
   public static final ParticleEasing SINE_OUT = value -> Math.sin(value * Math.PI / 2.0);
   public static final ParticleEasing SINE_IN_OUT = value -> -(Math.cos(Math.PI * value) - 1.0) / 2.0;
   public static final ParticleEasing CIRC_IN = value -> 1.0 - Math.sqrt(1.0 - Math.pow(value, 2.0));
   public static final ParticleEasing CIRC_OUT = value -> Math.sqrt(1.0 - Math.pow(value - 1.0, 2.0));
   public static final ParticleEasing CIRC_IN_OUT = value -> value < 0.5
      ? (1.0 - Math.sqrt(1.0 - Math.pow(2.0 * value, 2.0))) / 2.0
      : (Math.sqrt(1.0 - Math.pow(-2.0 * value + 2.0, 2.0)) + 1.0) / 2.0;
   public static final ParticleEasing ELASTIC_IN = value -> value != 0.0 && value != 1.0
      ? Math.pow(-2.0, 10.0 * value - 10.0) * Math.sin((value * 10.0 - 10.75) * (Math.PI * 2.0 / 3.0))
      : value;
   public static final ParticleEasing ELASTIC_OUT = value -> value != 0.0 && value != 1.0
      ? Math.pow(2.0, -10.0 * value) * Math.sin((value * 10.0 - 0.75) * (Math.PI * 2.0 / 3.0)) + 1.0
      : value;
   public static final ParticleEasing ELASTIC_IN_OUT = value -> {
      if (value != 0.0 && value != 1.0) {
         return value < 0.5
            ? -(Math.pow(2.0, 20.0 * value - 10.0) * Math.sin((20.0 * value - 11.125) * (Math.PI * 4.0 / 9.0))) / 2.0
            : Math.pow(2.0, -20.0 * value + 10.0) * Math.sin((20.0 * value - 11.125) * (Math.PI * 4.0 / 9.0)) / 2.0 + 1.0;
      } else {
         return value;
      }
   };
   public static final ParticleEasing EXPO_IN = value -> value != 0.0 ? Math.pow(2.0, 10.0 * value - 10.0) : value;
   public static final ParticleEasing EXPO_OUT = value -> value != 1.0 ? 1.0 - Math.pow(2.0, -10.0 * value) : value;
   public static final ParticleEasing EXPO_IN_OUT = value -> {
      if (value != 0.0 && value != 1.0) {
         return value < 0.5 ? Math.pow(2.0, 20.0 * value - 10.0) / 2.0 : (2.0 - Math.pow(2.0, -20.0 * value + 10.0)) / 2.0;
      } else {
         return value;
      }
   };
   public static final ParticleEasing BACK_IN = value -> 2.70158 * Math.pow(value, 3.0) - 1.70158 * Math.pow(value, 2.0);
   public static final ParticleEasing BACK_OUT = value -> 1.0 + 2.70158 * Math.pow(value - 1.0, 3.0) + 1.70158 * Math.pow(value - 1.0, 2.0);
   public static final ParticleEasing BACK_IN_OUT = value -> value < 0.5
      ? Math.pow(2.0 * value, 2.0) * (7.189819 * value - 2.5949095) / 2.0
      : (Math.pow(2.0 * value - 2.0, 2.0) * (3.5949095 * (value * 2.0 - 2.0) + 2.5949095) + 2.0) / 2.0;
   public static final ParticleEasing BOUNCE_OUT = x -> {
      double n1 = 7.5625;
      double d1 = 2.75;
      if (x < 1.0 / d1) {
         return n1 * Math.pow(x, 2.0);
      } else if (x < 2.0 / d1) {
         return n1 * Math.pow(x - 1.5 / d1, 2.0) + 0.75;
      } else {
         return x < 2.5 / d1 ? n1 * Math.pow(x - 2.25 / d1, 2.0) + 0.9375 : n1 * Math.pow(x - 2.625 / d1, 2.0) + 0.984375;
      }
   };
   public static final ParticleEasing BOUNCE_IN = value -> 1.0 - BOUNCE_OUT.ease(1.0 - value);
   public static final ParticleEasing BOUNCE_IN_OUT = value -> value < 0.5
      ? (1.0 - BOUNCE_OUT.ease(1.0 - 2.0 * value)) / 2.0
      : (1.0 + BOUNCE_OUT.ease(2.0 * value - 1.0)) / 2.0;

   public static ParticleEasing powIn(double n) {
      return value -> Math.pow(value, n);
   }

   public static ParticleEasing powIn(int n) {
      return powIn((double)n);
   }

   public static ParticleEasing powOut(double n) {
      return value -> 1.0 - Math.pow(1.0 - value, n);
   }

   public static ParticleEasing powOut(int n) {
      return powOut((double)n);
   }

   public static ParticleEasing powIN_OUT(double n) {
      return value -> value < 0.5 ? Math.pow(2.0, n - 1.0) * Math.pow(value, n) : 1.0 - Math.pow(-2.0 * value + 2.0, n) / 2.0;
   }
}
