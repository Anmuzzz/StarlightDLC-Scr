package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BindSetting;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Wind Jump",
   category = ModuleCategory.PLAYER,
   desc = "Бросает заряд ветра по кнопке"
)
public class WindJump extends BaseModule {

   private final BindSetting keyBind = new BindSetting(this, "Wind charge");

   private final EventListener<KeyPressEvent> onKey = event -> {
      if (mc.player == null) return;
      if (event.getAction() == 1 && keyBind.isKey(event.getKey())) {
         int slot = findWindCharge();
         if (slot != -1) {
            int prevSlot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = slot;
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.swingHand(Hand.MAIN_HAND);
            mc.player.getInventory().selectedSlot = prevSlot;
         }
      }
   };

   private int findWindCharge() {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getStack(i).getItem() == Items.WIND_CHARGE) {
            return i;
         }
      }
      return -1;
   }
}
