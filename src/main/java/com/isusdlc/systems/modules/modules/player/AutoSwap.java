package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.event.impl.window.KeyPressEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.notifications.NotificationType;
import com.isusdlc.systems.setting.settings.BindSetting;
import com.isusdlc.systems.setting.settings.ModeSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import com.isusdlc.utility.inventory.InventoryUtility;
import com.isusdlc.utility.inventory.ItemSlot;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.group.SlotGroups;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

@ModuleInfo(
   name = "Auto Swap",
   category = ModuleCategory.COMBAT,
   desc = "Меняет предмет в оффхенде по кнопке"
)
public class AutoSwap extends BaseModule {

   private final ModeSetting firstItem = new ModeSetting(this, "First item");
   private final ModeSetting.Value firstShield = new ModeSetting.Value(firstItem, "Shield").select();
   private final ModeSetting.Value firstSphere = new ModeSetting.Value(firstItem, "Sphere");
   private final ModeSetting.Value firstTotem = new ModeSetting.Value(firstItem, "Totem");
   private final ModeSetting.Value firstGApple = new ModeSetting.Value(firstItem, "GApple");
   private final ModeSetting.Value firstTorch = new ModeSetting.Value(firstItem, "Torch");

   private final ModeSetting secondItem = new ModeSetting(this, "Second item");
   private final ModeSetting.Value secondShield = new ModeSetting.Value(secondItem, "Shield").select();
   private final ModeSetting.Value secondSphere = new ModeSetting.Value(secondItem, "Sphere");
   private final ModeSetting.Value secondTotem = new ModeSetting.Value(secondItem, "Totem");
   private final ModeSetting.Value secondGApple = new ModeSetting.Value(secondItem, "GApple");
   private final ModeSetting.Value secondDirt = new ModeSetting.Value(secondItem, "Dirt");

   private final SliderSetting tickDelay = new SliderSetting(this, "Tick delay").min(0.0F).max(10.0F).step(1.0F).currentValue(1.0F);
   private final BindSetting keyBind = new BindSetting(this, "Swap key");

   private int swapTicks;
   private boolean swapping;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null) return;
      if (!swapping) return;

      if (swapTicks >= (int) tickDelay.getCurrentValue()) {
         swap();
         swapTicks = 0;
         swapping = false;
      } else {
         swapTicks++;
      }
   };

   private final EventListener<KeyPressEvent> onKeyPress = event -> {
      if (mc.player == null || mc.currentScreen != null) return;
      if (event.getAction() == 1 && keyBind.isKey(event.getKey())) {
         swapping = true;
         swapTicks = 0;
      }
   };

   private void swap() {
      Item from = getItem(firstItem.getValue().getName());
      Item to = getItem(secondItem.getValue().getName());
      if (from == null || to == null) return;

      Item current = mc.player.getOffHandStack().getItem();
      Item target = current == from ? to : from;

      SlotGroup<ItemSlot> allSlots = SlotGroups.hotbar().and(SlotGroups.inventory()).and(SlotGroups.offhand());
      ItemSlot slot = allSlots.findItem(target);
      if (slot == null) {
         elegant.getInstance().getNotificationManager()
            .addNotificationOther(NotificationType.ERROR, "Auto Swap", "Предмет не найден");
         return;
      }

      InventoryUtility.moveToOffHand(slot);
   }

   private Item getItem(String name) {
      return switch (name) {
         case "Shield" -> Items.SHIELD;
         case "Sphere" -> Items.PLAYER_HEAD;
         case "Totem" -> Items.TOTEM_OF_UNDYING;
         case "GApple" -> Items.GOLDEN_APPLE;
         case "Dirt" -> Items.DIRT;
         case "Torch" -> Items.TORCH;
         default -> null;
      };
   }
}
