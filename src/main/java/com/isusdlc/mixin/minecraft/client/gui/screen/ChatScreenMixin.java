package com.isusdlc.mixin.minecraft.client.gui.screen;

import com.isusdlc.elegant;
import com.isusdlc.framework.base.CustomDrawContext;
import com.isusdlc.systems.event.impl.render.ChatRenderEvent;
import com.isusdlc.systems.event.impl.window.ChatClickEvent;
import com.isusdlc.systems.event.impl.window.ChatReleaseEvent;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ChatScreen.class})
public class ChatScreenMixin extends Screen implements IMinecraft {
   @Shadow
   protected TextFieldWidget chatField;
   @Shadow
   private ChatInputSuggestor chatInputSuggestor;

   protected ChatScreenMixin(Text title) {
      super(title);
   }

   @Inject(
      method = {"sendMessage(Ljava/lang/String;Z)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSendMessage(String text, boolean addToHistory, CallbackInfo ci) {
      if (elegant.getInstance().getCommandManager().dispatch(text)) {
         mc.inGameHud.getChatHud().addToMessageHistory(text);
         ci.cancel();
      }
   }

   @Inject(
      method = {"render(Lnet/minecraft/client/gui/DrawContext;IIF)V"},
      at = {@At("RETURN")}
   )
   public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatRenderEvent(CustomDrawContext.of(context), delta));
   }

   @Inject(
      method = {"mouseClicked(DDI)Z"},
      at = {@At("HEAD")}
   )
   private void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatClickEvent((float)mouseX, (float)mouseY, button));
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      elegant.getInstance().getEventManager().triggerEvent(new ChatReleaseEvent((float)mouseX, (float)mouseY, button));
      return super.mouseReleased(mouseX, mouseY, button);
   }
}
