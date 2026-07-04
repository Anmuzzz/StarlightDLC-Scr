package com.isusdlc.utility.inventory.group.impl;

import java.util.ArrayList;
import java.util.List;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.slots.InventorySlot;

public class InventorySlotsGroup extends SlotGroup<InventorySlot> {
   public InventorySlotsGroup() {
      super(createSlots());
   }

   private static List<InventorySlot> createSlots() {
      List<InventorySlot> slots = new ArrayList<>();

      for (int i = 0; i < 27; i++) {
         slots.add(new InventorySlot(i));
      }

      return slots;
   }
}
