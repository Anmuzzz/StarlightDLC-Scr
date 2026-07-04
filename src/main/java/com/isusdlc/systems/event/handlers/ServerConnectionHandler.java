package com.isusdlc.systems.event.handlers;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.network.ServerConnectionEvent;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.utility.interfaces.IMinecraft;
import net.fabricmc.loader.api.FabricLoader;

public class ServerConnectionHandler implements IMinecraft {
   private boolean messageSent = false;
   private boolean connected;
   private final EventListener<ServerConnectionEvent> onServerConnection = event -> {
      this.connected = true;
      this.messageSent = false;
   };
   private final EventListener<ClientPlayerTickEvent> onClientPlayerTick = event -> {
      if (this.connected
         && !this.messageSent
         && mc.player != null
         && mc.player.age > 100
         && mc.getCurrentServerEntry() != null
         && FabricLoader.getInstance().isModLoaded("viafabricplus")) {
      }
   };

   public ServerConnectionHandler() {
      elegant.getInstance().getEventManager().subscribe(this);
   }
}
