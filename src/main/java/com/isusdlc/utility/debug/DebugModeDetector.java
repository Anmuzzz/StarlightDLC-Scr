package com.isusdlc.utility.debug;

import java.lang.management.ManagementFactory;

public class DebugModeDetector {
   public static boolean isDebugMode() {
      String jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
      return jvmArgs.contains("jdwp") || jvmArgs.contains("idea_rt.jar");
   }
}
