package com.isusdlc.features.autobuy.settings;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class AutoBuyItemSettings {
   private int buyBelow;
   private int sellAbove;
   private int minQuantity;
   private final boolean canHaveQuantity;
   private final String itemName;

   public AutoBuyItemSettings(int defaultPrice, Item item, String itemName) {
      this.itemName = itemName;
      this.buyBelow = defaultPrice;
      this.sellAbove = (int) (defaultPrice * 1.5);
      this.minQuantity = 1;
      this.canHaveQuantity = canItemStack(item);
   }

   private boolean canItemStack(Item item) {
      return item != Items.NETHERITE_HELMET && item != Items.NETHERITE_CHESTPLATE
         && item != Items.NETHERITE_LEGGINGS && item != Items.NETHERITE_BOOTS
         && item != Items.NETHERITE_SWORD && item != Items.NETHERITE_PICKAXE
         && item != Items.CROSSBOW && item != Items.TRIDENT
         && item != Items.MACE && item != Items.ELYTRA
         && item != Items.TOTEM_OF_UNDYING
         ? item.getMaxCount() > 1 : false;
   }

   public int getBuyBelow() { return buyBelow; }
   public int getSellAbove() { return sellAbove; }
   public int getMinQuantity() { return minQuantity; }
   public boolean isCanHaveQuantity() { return canHaveQuantity; }
   public String getItemName() { return itemName; }
   public void setBuyBelow(int v) { this.buyBelow = v; }
   public void setSellAbove(int v) { this.sellAbove = v; }
   public void setMinQuantity(int v) { this.minQuantity = v; }
}
