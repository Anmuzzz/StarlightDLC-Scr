package com.isusdlc.systems.event.impl.game;

import lombok.Generated;
import com.isusdlc.systems.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class TotemLossEvent extends Event {
   private final PlayerEntity player;
   private final ItemStack totemStack;
   private final boolean wasEnchanted;

   public TotemLossEvent(PlayerEntity player, ItemStack totemStack, boolean wasEnchanted) {
      this.player = player;
      this.totemStack = totemStack;
      this.wasEnchanted = wasEnchanted;
   }

   @Generated
   public PlayerEntity getPlayer() {
      return this.player;
   }

   @Generated
   public ItemStack getTotemStack() {
      return this.totemStack;
   }

   @Generated
   public boolean wasEnchanted() {
      return this.wasEnchanted;
   }
}
