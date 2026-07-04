package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.slot.SlotActionType;

@ModuleInfo(
   name = "Chest Stealer",
   category = ModuleCategory.PLAYER,
   desc = "Автоматически забирает предметы из сундуков"
)
public class ChestStealer extends BaseModule {

   private final SliderSetting delay = new SliderSetting(this, "Delay")
      .min(0.0F).max(500.0F).step(10.0F).currentValue(50.0F);

   private long lastClickTime;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null) return;
      if (!(mc.currentScreen instanceof GenericContainerScreen screen)) return;

      long now = System.currentTimeMillis();
      if (now - lastClickTime < delay.getCurrentValue()) return;

      var handler = screen.getScreenHandler();
      for (int i = 0; i < handler.slots.size(); i++) {
         var slot = handler.getSlot(i);
         if (slot.hasStack() && !slot.inventory.equals(mc.player.getInventory())) {
            mc.interactionManager.clickSlot(handler.syncId, i, 0, SlotActionType.QUICK_MOVE, mc.player);
            lastClickTime = now;
            return;
         }
      }
   };
}
