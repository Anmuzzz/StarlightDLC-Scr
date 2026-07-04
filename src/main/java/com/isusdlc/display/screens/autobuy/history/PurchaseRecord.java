package com.isusdlc.display.screens.autobuy.history;

import net.minecraft.item.ItemStack;

public class PurchaseRecord {
   private final ItemStack item;
   private final String itemName;
   private final int quantity;
   private final int price;
   private final String sellerName;
   private final long purchaseTime;

   public PurchaseRecord(ItemStack var1, String var2, int var3, int var4, String var5) {
      this.item = var1.copy();
      this.itemName = var2;
      this.quantity = var3;
      this.price = var4;
      this.sellerName = var5;
      this.purchaseTime = System.currentTimeMillis();
   }

   public String getFormattedTime() {
      long var1 = System.currentTimeMillis() - this.purchaseTime;
      long var3 = var1 / 1000L % 60L;
      long var5 = var1 / 60000L % 60L;
      long var7 = var1 / 3600000L;
      return var7 + "ч " + var5 + "м " + var3 + "с";
   }

   public String getFormattedPrice() {
      StringBuilder var1 = new StringBuilder();
      String var2 = String.valueOf(this.price);
      int var3 = 0;

      for (int var4 = var2.length() - 1; var4 >= 0; var4--) {
         if (var3 > 0 && var3 % 3 == 0) {
            var1.insert(0, '.');
         }

         var1.insert(0, var2.charAt(var4));
         var3++;
      }

      return "$" + var1;
   }

   public String getDisplayName() {
      return this.itemName + (this.quantity > 1 ? " x" + this.quantity : "");
   }

   public ItemStack getItem() {
      return this.item;
   }

   public String getItemName() {
      return this.itemName;
   }

   public int getQuantity() {
      return this.quantity;
   }

   public int getPrice() {
      return this.price;
   }

   public String getSellerName() {
      return this.sellerName;
   }

   public long getPurchaseTime() {
      return this.purchaseTime;
   }
}
