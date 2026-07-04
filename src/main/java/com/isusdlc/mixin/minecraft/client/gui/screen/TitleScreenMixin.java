package com.isusdlc.mixin.minecraft.client.gui.screen;

import com.isusdlc.elegant;
import com.isusdlc.ui.mainmenu.CustomTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public class TitleScreenMixin {
   @Inject(
      method = {"init()V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void setCustomScreen(CallbackInfo ci) {
      if (!elegant.INSTANCE.isPanic()) {
         ci.cancel();
         MinecraftClient.getInstance().setScreen(new CustomTitleScreen());
      }
   }
}
