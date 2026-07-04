package com.isusdlc.features.autobuy.util.krushprovider;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.settings.AutoBuyItemSettings;
import com.isusdlc.features.autobuy.settings.AutoBuySettingsManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class KrushItem implements AutoBuyableItem {
   private final String displayName;
   private final ItemStack reference;
   private final Item material;
   private final int price;
   private final AutoBuyItemSettings settings;
   private boolean enabled;

   public KrushItem(String var1, Item var2, ItemStack var3, int var4) {
      this.displayName = var1;
      this.material = var2;
      this.reference = var3;
      this.price = var4;
      this.enabled = true;
      this.settings = new AutoBuyItemSettings(var4, var2, var1);
      AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
   }

   @Override
   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public String getSearchName() {
      return this.displayName;
   }

   @Override
   public ItemStack createItemStack() {
      return this.reference.copy();
   }

   @Override
   public int getPrice() {
      return this.price;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   @Override
   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   @Override
   public AutoBuyItemSettings getSettings() {
      return this.settings;
   }
}
