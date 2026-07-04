package com.isusdlc.systems.modules.modules.combat;
import com.isusdlc.systems.event.EventListener;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

@ModuleInfo(
   name = "Criticals",
   category = ModuleCategory.COMBAT,
   desc = "Наносит критические удары без прыжка"
)
public class Criticals extends BaseModule {

   private final EventListener<AttackEvent> onAttack = event -> {
      if (mc.player == null || mc.world == null) return;
      if (mc.player.isOnGround() && !mc.player.getAbilities().flying && !mc.player.isTouchingWater()) {
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.01250004768372, mc.player.getZ(), false, true));
         mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false, true));
      }
   };
}
