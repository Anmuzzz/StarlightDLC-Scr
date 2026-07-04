package com.isusdlc.display.screens.autobuy.history;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.manager.AutoBuyManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class HistoryManager {
   private static HistoryManager instance;
   private final List<PurchaseRecord> history = new ArrayList<>();
   private static final int MAX_HISTORY_SIZE = 100;
   private static final Pattern SELLER_PATTERN = Pattern.compile("⚕.*:\\s*([\\w\\d_]+)", 256);
   private static final Pattern PRICE_PATTERN = Pattern.compile("\\$\\s*(\\d+(?:[,\\s.]\\d{3})*(?:\\.\\d{2})?)");
   private static final Pattern PRICE_PATTERN_HOLYWORLD = Pattern.compile("Цена:.*?(\\d+(?:[\\s.]\\d{3})*)\\s*¤");

   private HistoryManager() {
   }

   public static HistoryManager getInstance() {
      if (instance == null) {
         instance = new HistoryManager();
      }

      return instance;
   }

   private void addTestData() {
      this.history.add(new PurchaseRecord(new ItemStack(Items.DIAMOND, 64), "Алмаз", 64, 150000, "TestPlayer1"));
      this.history.add(new PurchaseRecord(new ItemStack(Items.NETHERITE_INGOT, 16), "Незеритовый слиток", 16, 500000, "DiamondKing"));
      this.history.add(new PurchaseRecord(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 8), "Зачарованное яблоко", 8, 250000, "AppleSeller"));
      this.history.add(new PurchaseRecord(new ItemStack(Items.ELYTRA, 1), "Элитры", 1, 1000000, "WingMaster"));
      this.history.add(new PurchaseRecord(new ItemStack(Items.TOTEM_OF_UNDYING, 4), "Тотем бессмертия", 4, 400000, "TotemTrader"));
   }

   public void addPurchase(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         String var2 = var1.getName().getString();
         var2 = var2.replaceAll("^\\s*-\\s*", "").replaceAll("\\s*-\\s*$", "").trim();
         int var3 = var1.getCount();
         int var4 = this.extractPrice(var1);
         String var5 = this.extractSeller(var1);
         PurchaseRecord var6 = new PurchaseRecord(var1, var2, var3, var4, var5);
         this.history.add(0, var6);
         if (this.history.size() > 100) {
            this.history.remove(this.history.size() - 1);
         }
      }
   }

   public void addPurchaseWithItem(ItemStack var1, String var2, int var3) {
      if (var1 != null && !var1.isEmpty()) {
         int var4 = var1.getCount();
         if (var4 <= 0) {
            var4 = 1;
         }

         String var5 = var2.replaceAll("^\\s*-\\s*", "").replaceAll("\\s*-\\s*$", "").trim();
         String var6 = this.extractSeller(var1);
         PurchaseRecord var7 = new PurchaseRecord(var1, var5, var4, var3, var6);
         this.history.add(0, var7);
         if (this.history.size() > 100) {
            this.history.remove(this.history.size() - 1);
         }
      } else {
         this.addPurchaseFromMessage(var2, var3);
      }
   }

   public void addPurchaseFromMessage(String var1, int var2) {
      if (var1 != null && !var1.isEmpty() && var2 > 0) {
         ItemStack var3 = ItemStack.EMPTY;
         int var4 = 1;
         AutoBuyManager var5 = AutoBuyManager.getInstance();
         String var6 = var1.replaceAll("§[0-9a-fk-or]", "").trim();
         String var7 = var6.toLowerCase();

         for (AutoBuyableItem var9 : var5.getAllItems()) {
            String var10 = var9.getDisplayName().replaceAll("§[0-9a-fk-or]", "").trim();
            String var11 = var10.toLowerCase();
            String var12 = var9.getSearchName() != null ? var9.getSearchName().replaceAll("§[0-9a-fk-or]", "").trim() : var10;
            String var13 = var12.toLowerCase();
            if (var11.equals(var7) || var13.equals(var7) || var7.contains(var11) || var11.contains(var7) || var7.contains(var13) || var13.contains(var7)) {
               try {
                  var3 = var9.createItemStack();
                  if (var3 == null) {
                     var3 = ItemStack.EMPTY;
                  }

                  var4 = var3.getCount();
                  if (var4 <= 0) {
                     var4 = 1;
                  }
               } catch (Exception var15) {
                  var3 = ItemStack.EMPTY;
               }
               break;
            }
         }

         String var16 = this.findSellerInInventory(var6);
         String var17 = var6.replaceAll("^\\s*-\\s*", "").replaceAll("\\s*-\\s*$", "").trim();
         PurchaseRecord var18 = new PurchaseRecord(var3, var17, var4, var2, var16);
         this.history.add(0, var18);
         if (this.history.size() > 100) {
            this.history.remove(this.history.size() - 1);
         }
      }
   }

   private String findSellerInInventory(String var1) {
      if (MinecraftClient.getInstance().player == null) {
         return "Неизвестно";
      }

      for (int var2 = 0; var2 < MinecraftClient.getInstance().player.getInventory().size(); var2++) {
         ItemStack var3 = MinecraftClient.getInstance().player.getInventory().getStack(var2);
         if (!var3.isEmpty()) {
            String var4 = var3.getName().getString().replaceAll("§[0-9a-fk-or]", "").trim();
            if (var4.contains(var1) || var1.contains(var4)) {
               String var5 = this.extractSeller(var3);
               if (!var5.equals("Неизвестно")) {
                  return var5;
               }
            }
         }
      }

      return "Неизвестно";
   }

   public List<PurchaseRecord> getHistory() {
      return new ArrayList<>(this.history);
   }

   public void clearHistory() {
      this.history.clear();
   }

   private int extractPrice(ItemStack var1) {
      LoreComponent var2 = (LoreComponent)var1.get(DataComponentTypes.LORE);
      if (var2 != null && !var2.lines().isEmpty()) {
         for (Text var4 : var2.lines()) {
            String var5 = var4.getString();
            Matcher var6 = PRICE_PATTERN_HOLYWORLD.matcher(var5);
            if (var6.find()) {
               try {
                  String var12 = var6.group(1).replaceAll("[\\s.]", "");
                  return Integer.parseInt(var12);
               } catch (NumberFormatException var11) {
               }
            }

            Matcher var7 = PRICE_PATTERN.matcher(var5);
            String var8 = null;

            while (var7.find()) {
               var8 = var7.group(1);
            }

            if (var8 != null) {
               try {
                  return Integer.parseInt(var8.replaceAll("[\\s,.]", ""));
               } catch (NumberFormatException var10) {
               }
            }
         }
      }

      return 0;
   }

   private String extractSeller(ItemStack var1) {
      if (var1 == null) {
         return "Неизвестно";
      }

      LoreComponent var2 = (LoreComponent)var1.get(DataComponentTypes.LORE);
      if (var2 != null && !var2.lines().isEmpty()) {
         for (Text var4 : var2.lines()) {
            String var5 = var4.getString();
            Matcher var6 = SELLER_PATTERN.matcher(var5);
            if (var6.find()) {
               return var6.group(1);
            }

            if (var5.contains("Продавец:") || var5.contains("Продaвeц:")) {
               String[] var7 = var5.split("Продавец:|Продaвeц:");
               if (var7.length > 1) {
                  String var8 = var7[1].trim();
                  var8 = var8.replaceAll("§[0-9a-fk-or]", "");
                  var8 = var8.replaceAll("[▍▶▎]", "").trim();
                  String[] var9 = var8.split("\\s+");
                  if (var9.length > 0 && !var9[0].isEmpty()) {
                     return var9[0];
                  }
               }
            }
         }

         return "Неизвестно";
      } else {
         return "Неизвестно";
      }
   }
}
