package com.isusdlc.utility.math.calculator;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.network.SendPacketEvent;
import com.isusdlc.utility.interfaces.IMinecraft;
import com.isusdlc.utility.math.MathUtility;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;

public class ChatListener implements IMinecraft {
   private final EventListener<SendPacketEvent> onSendPacket = event -> {
      if (event.getPacket() instanceof ChatCommandSignedC2SPacket packet) {
         if (mc.player == null) {
            return;
         }

         String message = packet.command();
         if (message.startsWith("ah me")) {
            mc.player.networkHandler.sendChatMessage("/ah " + mc.player.getName().getString());
            event.cancel();
         }

         if (message.startsWith("ah sell ")) {
            String expression = message.replaceFirst("ah sell ", "");
            String result = MathUtility.calculate(expression);
            mc.player.networkHandler.sendChatMessage("/ah sell " + Math.round(Float.parseFloat(result)));
            event.cancel();
         }
      }
   };

   public ChatListener() {
      elegant.getInstance().getEventManager().subscribe(this);
   }
}
