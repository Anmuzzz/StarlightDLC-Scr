package com.isusdlc.features.autobuy.items.list;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.customitem.CustomItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class TalismanProvider {
   public static List<AutoBuyableItem> getTalismans() {
      ArrayList var0 = new ArrayList();
      NbtCompound var1 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 4.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.max_health", 2.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.attack_speed", 0.1, 1, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.armor", -3.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Раздора", var1, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Раздора")));
      NbtCompound var2 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 7.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.max_health", -4.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.movement_speed", 0.1, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Карателя", var2, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Карателя")));
      NbtCompound var3 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.max_health", 4.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.armor_toughness", 2.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand")
      );
      var0.add(new CustomItem("Талисман Крушителя", var3, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Крушителя")));
      NbtCompound var4 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.max_health", -4.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Тирана", var4, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Тирана")));
      NbtCompound var5 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 5.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.max_health", -4.0, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Ярости", var5, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Ярости")));
      NbtCompound var6 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.attack_damage", 2.5, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.attack_speed", 0.1, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Демона", var6, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Демона")));
      NbtCompound var7 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.max_health", 2.0, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.movement_speed", 0.15, 1, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.attack_speed", 0.15, 1, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Вихря", var7, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Вихря")));
      NbtCompound var8 = createTalismanNbt(
         new TalismanProvider.AttributeData("minecraft:generic.armor", 1.5, 0, "offhand"),
         new TalismanProvider.AttributeData("minecraft:generic.max_health", 1.5, 0, "offhand")
      );
      var0.add(new CustomItem("[★] Талисман Мрака", var8, Items.TOTEM_OF_UNDYING, Defaultpricec.getPrice("Талисман Мрака")));
      return var0;
   }

   private static NbtCompound createTalismanNbt(TalismanProvider.AttributeData... var0) {
      NbtCompound var1 = new NbtCompound();
      NbtList var2 = new NbtList();

      for (TalismanProvider.AttributeData var6 : var0) {
         NbtCompound var7 = new NbtCompound();
         var7.putString("AttributeName", var6.attributeName);
         var7.putDouble("Amount", var6.amount);
         var7.putInt("Operation", var6.operation);
         var7.putString("Slot", var6.slot);
         var7.putString("Name", UUID.randomUUID().toString());
         var7.putIntArray(
            "UUID",
            new int[]{
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9),
               (int)(Math.random() * 2.147483647E9)
            }
         );
         var2.add(var7);
      }

      var1.put("AttributeModifiers", var2);
      return var1;
   }

   private static class AttributeData {
      final String attributeName;
      final double amount;
      final int operation;
      final String slot;

      AttributeData(String var1, double var2, int var4, String var5) {
         this.attributeName = var1;
         this.amount = var2;
         this.operation = var4;
         this.slot = var5;
      }
   }
}
