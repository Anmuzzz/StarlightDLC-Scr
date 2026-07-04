package com.isusdlc.utility.inventory.group;

import com.isusdlc.utility.inventory.group.impl.ArmorSlotsGroup;
import com.isusdlc.utility.inventory.group.impl.HotbarSlotsGroup;
import com.isusdlc.utility.inventory.group.impl.InventorySlotsGroup;
import com.isusdlc.utility.inventory.group.impl.OffhandSlotGroup;
import com.isusdlc.utility.inventory.slots.ArmorSlot;
import com.isusdlc.utility.inventory.slots.HotbarSlot;
import com.isusdlc.utility.inventory.slots.InventorySlot;
import com.isusdlc.utility.inventory.slots.OffhandSlot;

public class SlotGroups {
   private SlotGroups() {
   }

   public static SlotGroup<HotbarSlot> hotbar() {
      return new HotbarSlotsGroup();
   }

   public static SlotGroup<InventorySlot> inventory() {
      return new InventorySlotsGroup();
   }

   public static SlotGroup<ArmorSlot> armor() {
      return new ArmorSlotsGroup();
   }

   public static SlotGroup<OffhandSlot> offhand() {
      return new OffhandSlotGroup();
   }
}
