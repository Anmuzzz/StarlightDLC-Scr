package com.isusdlc.utility.inventory.group.impl;

import java.util.List;
import com.isusdlc.utility.inventory.group.SlotGroup;
import com.isusdlc.utility.inventory.slots.OffhandSlot;

public class OffhandSlotGroup extends SlotGroup<OffhandSlot> {
   public OffhandSlotGroup() {
      super(List.of(new OffhandSlot()));
   }
}
