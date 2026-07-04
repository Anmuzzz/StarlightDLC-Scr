package com.isusdlc.systems.modules.modules.combat;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.utility.game.MessageUtility;
import com.isusdlc.utility.time.Timer;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "ClickPearl",
   category = ModuleCategory.COMBAT,
   desc = "Кидает эндер-жемчуг по бинду"
)
public class ClickPearl extends BaseModule {

   private final BindSetting pearlBind = new BindSetting(this, "Pearl Bind").key(33);
   private final Timer pearlTimer = new Timer();

   private final EventListener<KeyPressEvent> onKey = event -> {
      if (!pearlBind.isKey(event.getKey()) || event.getAction() != 1) return;
      if (mc.world == null || mc.player == null) return;
      if (!pearlTimer.finished(500L)) return;

      int slot = findPearlSlot();
      if (slot == -1) {
         MessageUtility.info(Text.of("§cНет эндер-жемчуга в хотбаре"));
         return;
      }

      int prevSlot = mc.player.getInventory().selectedSlot;
      mc.player.getInventory().selectedSlot = slot;
      mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
      mc.player.swingHand(Hand.MAIN_HAND);
      mc.player.getInventory().selectedSlot = prevSlot;
      pearlTimer.reset();
   };

   private int findPearlSlot() {
      var inv = mc.player.getInventory();
      for (int i = 0; i < 9; i++) {
         if (inv.getStack(i).getItem() == Items.ENDER_PEARL) return i;
      }
      return -1;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      pearlTimer.reset();
   }
}
