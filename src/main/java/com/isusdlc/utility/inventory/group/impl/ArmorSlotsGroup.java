package com.isusdlc.utility.inventory.group.impl;

import java.util.ArrayList;
import java.util.List;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.slots.ArmorSlot;

public class ArmorSlotsGroup extends SlotGroup<ArmorSlot> {
   public ArmorSlotsGroup() {
      super(createSlots());
   }

   private static List<ArmorSlot> createSlots() {
      List<ArmorSlot> slots = new ArrayList<>();

      for (int i = 0; i < 4; i++) {
         slots.add(new ArmorSlot(i));
      }

      return slots;
   }
}
