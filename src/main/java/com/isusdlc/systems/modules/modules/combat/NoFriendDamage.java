package com.isusdlc.systems.modules.modules.combat;
import com.isusdlc.systems.event.EventListener;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import net.minecraft.entity.player.PlayerEntity;

@ModuleInfo(
   name = "NoFriendDamage",
   category = ModuleCategory.COMBAT,
   desc = "Отключает урон по друзьям"
)
public class NoFriendDamage extends BaseModule {

   private final EventListener<AttackEvent> onAttack = event -> {
      if (event.getEntity() instanceof PlayerEntity player) {
         if (elegant.getInstance().getFriendManager().isFriend(player.getName().getString())) {
            event.cancel();
         }
      }
   };
}
