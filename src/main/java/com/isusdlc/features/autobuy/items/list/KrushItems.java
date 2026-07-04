package com.isusdlc.features.autobuy.items.list;

import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent.Builder;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class KrushItems {
   public static ItemStack getHelmet() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_HELMET);
      addEnchantments(
         var0,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.AQUA_AFFINITY,
         1,
         Enchantments.RESPIRATION,
         3,
         Enchantments.PROTECTION,
         5
      );
      setupItem(var0, createStyledName("Шлем Крушителя"), List.of(Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)));
      return var0;
   }

   public static ItemStack getChestplate() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_CHESTPLATE);
      addEnchantments(
         var0,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      setupItem(var0, createStyledName("Нагрудник Крушителя"), List.of(Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)));
      return var0;
   }

   public static ItemStack getLeggings() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_LEGGINGS);
      addEnchantments(
         var0,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      setupItem(var0, createStyledName("Поножи Крушителя"), List.of(Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)));
      return var0;
   }

   public static ItemStack getBoots() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_BOOTS);
      addEnchantments(
         var0,
         Enchantments.MENDING,
         1,
         Enchantments.FIRE_PROTECTION,
         5,
         Enchantments.DEPTH_STRIDER,
         3,
         Enchantments.PROJECTILE_PROTECTION,
         5,
         Enchantments.FEATHER_FALLING,
         4,
         Enchantments.SOUL_SPEED,
         3,
         Enchantments.BLAST_PROTECTION,
         5,
         Enchantments.PROTECTION,
         5,
         Enchantments.UNBREAKING,
         5
      );
      setupItem(var0, createStyledName("Ботинки Крушителя"), List.of(Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)));
      return var0;
   }

   public static ItemStack getSword() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_SWORD);
      addEnchantments(
         var0,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.SMITE,
         7,
         Enchantments.SWEEPING_EDGE,
         3,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.BANE_OF_ARTHROPODS,
         7,
         Enchantments.SHARPNESS,
         7,
         Enchantments.LOOTING,
         5
      );
      setupItem(
         var0,
         createStyledName("Меч Крушителя"),
         List.of(
            Text.literal("Опытный III").formatted(Formatting.GRAY),
            Text.literal("Вампиризм II").formatted(Formatting.GRAY),
            Text.literal("Окисление II").formatted(Formatting.GRAY),
            Text.literal("Яд III").formatted(Formatting.GRAY),
            Text.literal("Детекция III").formatted(Formatting.GRAY),
            Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)
         )
      );
      return var0;
   }

   public static ItemStack getPickaxe() {
      ItemStack var0 = new ItemStack(Items.NETHERITE_PICKAXE);
      addEnchantments(var0, Enchantments.UNBREAKING, 5, Enchantments.MENDING, 1, Enchantments.EFFICIENCY, 10, Enchantments.FORTUNE, 5);
      setupItem(
         var0,
         createStyledName("Кирка Крушителя"),
         List.of(
            Text.literal("Бульдозер II").formatted(Formatting.GRAY),
            Text.literal("Опытный III").formatted(Formatting.GRAY),
            Text.literal("Магнит").formatted(Formatting.GRAY),
            Text.literal("Авто-Плавка").formatted(Formatting.GRAY),
            Text.literal("Паутина").formatted(Formatting.GRAY),
            Text.literal("Пингер").formatted(Formatting.GRAY),
            Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)
         )
      );
      return var0;
   }

   public static ItemStack getCrossbow() {
      ItemStack var0 = new ItemStack(Items.CROSSBOW);
      addEnchantments(
         var0, Enchantments.QUICK_CHARGE, 3, Enchantments.MENDING, 1, Enchantments.PIERCING, 5, Enchantments.UNBREAKING, 3, Enchantments.MULTISHOT, 1
      );
      setupItem(var0, createStyledName("Арбалет Крушителя"), List.of(Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)));
      return var0;
   }

   public static ItemStack getTrident() {
      ItemStack var0 = new ItemStack(Items.TRIDENT);
      addEnchantments(
         var0,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1,
         Enchantments.CHANNELING,
         1,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.IMPALING,
         5,
         Enchantments.SHARPNESS,
         7,
         Enchantments.LOYALTY,
         3
      );
      setupItem(
         var0,
         createStyledName("Трезубец Крушителя"),
         List.of(
            Text.literal("Скаут III").formatted(Formatting.GRAY),
            Text.literal("Опытный III").formatted(Formatting.GRAY),
            Text.literal("Вампиризм II").formatted(Formatting.GRAY),
            Text.literal("Ступор III").formatted(Formatting.GRAY),
            Text.literal("Притяжение II").formatted(Formatting.GRAY),
            Text.literal("Окисление II").formatted(Formatting.GRAY),
            Text.literal("Возвращение").formatted(Formatting.GRAY),
            Text.literal("Подрывник").formatted(Formatting.GRAY),
            Text.literal("Яд III").formatted(Formatting.GRAY),
            Text.literal("Детекция III").formatted(Formatting.GRAY),
            Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)
         )
      );
      return var0;
   }

   public static ItemStack getMace() {
      ItemStack var0 = new ItemStack(Items.MACE);
      addEnchantments(
         var0,
         Enchantments.SHARPNESS,
         7,
         Enchantments.SMITE,
         7,
         Enchantments.BANE_OF_ARTHROPODS,
         7,
         Enchantments.DENSITY,
         5,
         Enchantments.BREACH,
         3,
         Enchantments.SWEEPING_EDGE,
         3,
         Enchantments.FIRE_ASPECT,
         2,
         Enchantments.LOOTING,
         5,
         Enchantments.UNBREAKING,
         5,
         Enchantments.MENDING,
         1
      );
      setupItem(
         var0,
         createStyledName("Булава Крушителя"),
         List.of(
            Text.literal("Опытный III").formatted(Formatting.GRAY),
            Text.literal("Вампиризм II").formatted(Formatting.GRAY),
            Text.literal("Окисление II").formatted(Formatting.GRAY),
            Text.literal("Яд III").formatted(Formatting.GRAY),
            Text.literal("Детекция III").formatted(Formatting.GRAY),
            Text.literal("[★] Оригинальный предмет").formatted(Formatting.GRAY)
         )
      );
      return var0;
   }

   private static void addEnchantments(ItemStack var0, Object... var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.world != null) {
         DynamicRegistryManager var3 = var2.world.getRegistryManager();
         Impl var4 = var3.getOrThrow(RegistryKeys.ENCHANTMENT);
         Builder var5 = new Builder((ItemEnchantmentsComponent)var0.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT));

         for (byte var6 = 0; var6 < var1.length; var6 += 2) {
            RegistryKey var7 = (RegistryKey)var1[var6];
            int var8 = (Integer)var1[var6 + 1];
            Reference var9 = var4.getOrThrow(var7);
            var5.add(var9, var8);
         }

         var0.set(DataComponentTypes.ENCHANTMENTS, var5.build());
      }
   }

   private static void setupItem(ItemStack var0, Text var1, List<Text> var2) {
      var0.set(DataComponentTypes.CUSTOM_NAME, var1);
      NbtCompound var3 = new NbtCompound();
      var3.putInt("HideFlags", 127);
      var3.putBoolean("Unbreakable", true);
      var0.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var3));
      if (!var2.isEmpty()) {
         var0.set(DataComponentTypes.LORE, new LoreComponent(var2));
      }
   }

   private static Text createStyledName(String var0) {
      return Text.literal(var0).formatted(new Formatting[]{Formatting.BOLD, Formatting.DARK_RED});
   }
}
