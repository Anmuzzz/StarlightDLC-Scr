package com.isusdlc.protection.client;

import com.isusdlc.elegant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.kotopushka.compiler.sdk.annotations.VMProtect;
import ru.kotopushka.compiler.sdk.enums.VMProtectType;

public class MinecraftClientMixinProtection {
   @VMProtect(
      type = VMProtectType.MUTATION
   )
   public static void init() {
      elegant.INSTANCE.initialize();
   }

   @VMProtect(
      type = VMProtectType.MUTATION
   )
   public static void shutdown() {
      elegant.INSTANCE.shutdown();
   }

   public static void updateTitle(CallbackInfoReturnable<String> cir) {
      if (!elegant.INSTANCE.isPanic()) {
         String title = "%s %s (%s)".formatted("StarlightDLC", "1.0.0", "by @framekid");
         cir.setReturnValue(title);
      }
   }
}
