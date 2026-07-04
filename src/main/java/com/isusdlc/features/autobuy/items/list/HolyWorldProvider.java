package com.isusdlc.features.autobuy.items.list;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.settings.AutoBuyItemSettings;
import com.isusdlc.features.autobuy.settings.AutoBuySettingsManager;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent.Builder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.Impl;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class HolyWorldProvider {
   private static List<AutoBuyableItem> items = null;

   public static List<AutoBuyableItem> getItems() {
      if (items == null) {
         items = new ArrayList<>();
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Шлем Infinity",
               Items.NETHERITE_HELMET,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.AQUA_AFFINITY, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.RESPIRATION, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Нагрудник Infinity",
               Items.NETHERITE_CHESTPLATE,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Поножи Infinity",
               Items.NETHERITE_LEGGINGS,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Ботинки Infinity",
               Items.NETHERITE_BOOTS,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.DEPTH_STRIDER, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FEATHER_FALLING, 4),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.SOUL_SPEED, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Шлем Eternity",
               Items.NETHERITE_HELMET,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.AQUA_AFFINITY, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.RESPIRATION, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый I").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Нагрудник Eternity",
               Items.NETHERITE_CHESTPLATE,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый I").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Штаны Eternity",
               Items.NETHERITE_LEGGINGS,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый I").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Ботинки Eternity",
               Items.NETHERITE_BOOTS,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.DEPTH_STRIDER, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FEATHER_FALLING, 4),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.SOUL_SPEED, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(Text.literal("Непробиваемый I").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Шлем солнца",
               Items.GOLDEN_HELMET,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.AQUA_AFFINITY, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PROTECTION, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.RESPIRATION, 3)
               },
               List.of(Text.literal("Непробиваемый II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Броневая элитра",
               Items.ELYTRA,
               0,
               new HolyWorldProvider.EnchantmentData[]{new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 4)},
               new HolyWorldProvider.AttributeData[]{new HolyWorldProvider.AttributeData("minecraft:generic.armor", 8.0, 0, "chest")},
               null
            )
         );
         items.add(new HolyWorldProvider.HolyWorldItem("Элитры", Items.ELYTRA, 0, null, null));
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Меч Eternity",
               Items.NETHERITE_SWORD,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.BANE_OF_ARTHROPODS, 7),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FIRE_ASPECT, 2),
                  new HolyWorldProvider.EnchantmentData(Enchantments.LOOTING, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.MENDING, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.SHARPNESS, 7),
                  new HolyWorldProvider.EnchantmentData(Enchantments.SMITE, 7),
                  new HolyWorldProvider.EnchantmentData(Enchantments.SWEEPING_EDGE, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(
                  Text.literal("Богач I").formatted(Formatting.GRAY),
                  Text.literal("Разрушитель II").formatted(Formatting.GRAY),
                  Text.literal("Критический II").formatted(Formatting.GRAY)
               )
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Кирка Eternity",
               Items.NETHERITE_PICKAXE,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.EFFICIENCY, 10),
                  new HolyWorldProvider.EnchantmentData(Enchantments.FORTUNE, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.MENDING, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               List.of(
                  Text.literal("Магнетизм I").formatted(Formatting.GRAY),
                  Text.literal("Неразрушимость I").formatted(Formatting.GRAY),
                  Text.literal("Автоплавка").formatted(Formatting.GRAY),
                  Text.literal("Опытный III").formatted(Formatting.GRAY),
                  Text.literal("Бур II").formatted(Formatting.GRAY)
               )
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Арбалет Eternity",
               Items.CROSSBOW,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.MULTISHOT, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.PIERCING, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.QUICK_CHARGE, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 3)
               },
               List.of(Text.literal("Оглушение II").formatted(Formatting.GRAY))
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldItem(
               "Громовержец",
               Items.TRIDENT,
               0,
               new HolyWorldProvider.EnchantmentData[]{
                  new HolyWorldProvider.EnchantmentData(Enchantments.IMPALING, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.LOOTING, 5),
                  new HolyWorldProvider.EnchantmentData(Enchantments.LOYALTY, 3),
                  new HolyWorldProvider.EnchantmentData(Enchantments.MENDING, 1),
                  new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
               },
               null
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера Цербера",
               "Сфера Цербера",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA5NWE3ZmQ5MGRhYTFiYmU3MDY5MDg5NzQwZTA1ZDBiZmM2NjI5NmVlM2M0MGVlNzFhNGUwYTY2MTZiMmJiYyJ9fX0=",
               "Cerber",
               "hms-damage:5,hms-rush:1"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера Флеша",
               "Сфера Флеша",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc0MDBlYTE5ZGJkODRmNzVjMzlhZDY4MjNhYzRlZjc4NmYzOWY0OGZjNmY4NDYwMjM2NmFjMjliODM3NDIyIn19fQ==",
               "Flash",
               "hms-speed:3,hms-armor:1"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера ɪᴍᴍᴏʀᴛᴀʟɪᴛʏ",
               "Сфера Имморталити",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODNlZDRjZTIzOTMzZTY2ZTA0ZGYxNjA3MDY0NGY3NTk5ZWViNTUzMDdmN2VhZmU4ZDkyZjQwZmIzNTIwODYzYyJ9fX0=",
               "Immortal",
               "hms-speed:2,hms-damage:3"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера ᴀʀᴍᴏʀᴛᴀʟɪᴛʏ",
               "Сфера Арморталити",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE2MmI5ZGU2YTI2Yjg2ODY5Y2EyMmVhNDBmMWJkZTgwYTA0MzBhNTQ1NDdiZWNjZThmZGE4NzA3Nzc3MjU4ZiJ9fX0=",
               "Armortality",
               "hms-armor:2,hms-damage:2,hms-health:2"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера на Скорость III",
               "Сфера на скорость 3",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM5MzY1NjQyYzZlZGRjZmVkZjViNWUxNGUyYmM3MTI1N2Q5ZTRhMzM2M2QxMjNjNmYzM2M1NWNhZmJmNmQifX19",
               "Speed3"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера Eternity",
               "Сфера Eternity",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM5MzY1NjQyYzZlZGRjZmVkZjViNWUxNGUyYmM3MTI1N2Q5ZTRhMzM2M2QxMjNjNmYzM2M1NWNhZmJmNmQifX19",
               "Eternity",
               "hms-speed:2,hms-damage:2,hms-armor:2"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера Stinger",
               "Сфера Stinger",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGM5MzY1NjQyYzZlZGRjZmVkZjViNWUxNGUyYmM3MTI1N2Q5ZTRhMzM2M2QxMjNjNmYzM2M1NWNhZmJmNmQifX19",
               "Stinger",
               "hms-speed:1,hms-armor:2,hms-damage:2"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера на броня III скорость II",
               "Сфера на броня 3",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmZjJlYjQ5OGU1YzZhMDQ0ODRmMGM5Zjc4NWI0NDg0NzlhYjIxM2RmOTVlYzkxMTc2YTMwOGExMmFkZDcwIn19fQ==",
               "Mythical3",
               "hms-armor:3,hms-speed:2"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера на урон II броня III",
               "Сфера на броня 3",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmZjJlYjQ5OGU1YzZhMDQ0ODRmMGM5Zjc4NWI0NDg0NzlhYjIxM2RmOTVlYzkxMTc2YTMwOGExMmFkZDcwIn19fQ==",
               "Speed",
               "hms-armor:3,hms-damage:2"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldSphereItem(
               "Сфера на броня II урон III",
               "Сфера на броня 3",
               Items.PLAYER_HEAD,
               0,
               null,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmFmZjJlYjQ5OGU1YzZhMDQ0ODRmMGM5Zjc4NWI0NDg0NzlhYjIxM2RmOTVlYzkxMTc2YTMwOGExMmFkZDcwIn19fQ==",
               "Mythical1",
               "hms-armor:2,hms-damage:3"
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldTalikItem(
               "Талисман Stinger",
               Items.TOTEM_OF_UNDYING,
               0,
               new HolyWorldProvider.AttributeData[]{
                  new HolyWorldProvider.AttributeData("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand")
               },
               new HolyWorldProvider.EnchantmentData[]{new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 1)}
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldTalikItem(
               "Талисман Infinity",
               Items.TOTEM_OF_UNDYING,
               0,
               new HolyWorldProvider.AttributeData[]{
                  new HolyWorldProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.movement_speed", 0.2, 1, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.max_health", 2.0, 0, "offhand")
               },
               new HolyWorldProvider.EnchantmentData[]{new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 1)}
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldTalikItem(
               "Талисман Eternity",
               Items.TOTEM_OF_UNDYING,
               0,
               new HolyWorldProvider.AttributeData[]{
                  new HolyWorldProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.movement_speed", 0.2, 1, "offhand")
               },
               new HolyWorldProvider.EnchantmentData[]{new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 1)}
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldTalikItem(
               "Легендарный талисман",
               Items.TOTEM_OF_UNDYING,
               0,
               new HolyWorldProvider.AttributeData[]{
                  new HolyWorldProvider.AttributeData("minecraft:generic.armor", 2.0, 0, "offhand"),
                  new HolyWorldProvider.AttributeData("minecraft:generic.attack_damage", 2.0, 0, "offhand")
               },
               new HolyWorldProvider.EnchantmentData[]{new HolyWorldProvider.EnchantmentData(Enchantments.UNBREAKING, 1)}
            )
         );
         items.add(new HolyWorldProvider.HolyWorldItem("Тотем бессмертия", Items.TOTEM_OF_UNDYING, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldExpBottleItem("Пузырек с 15 уровнем", "15", Items.EXPERIENCE_BOTTLE, 0, 315));
         items.add(new HolyWorldProvider.HolyWorldExpBottleItem("Пузырек с 50 уровнем", "50", Items.EXPERIENCE_BOTTLE, 0, 5345));
         items.add(new HolyWorldProvider.HolyWorldExpBottleItem("Пузырек с 100 уровнем", "100", Items.EXPERIENCE_BOTTLE, 0, 30971));
         items.add(new HolyWorldProvider.HolyWorldExpBottleItem("Обычный пузырек опыта", "опыт", Items.EXPERIENCE_BOTTLE, 0, 0));
         items.add(new HolyWorldProvider.HolyWorldBackpackItem("Рюкзак I уровень", "рюкзак 1 уровень", Items.PINK_SHULKER_BOX, 0, "mini"));
         items.add(new HolyWorldProvider.HolyWorldBackpackItem("Рюкзак II уровень", "рюкзак 2 уровень", Items.LIGHT_BLUE_SHULKER_BOX, 0, "normal"));
         items.add(new HolyWorldProvider.HolyWorldBackpackItem("Рюкзак III уровень", "рюкзак 3 уровень", Items.RED_SHULKER_BOX, 0, "big"));
         items.add(new HolyWorldProvider.HolyWorldBackpackItem("Рюкзак IV уровень", "рюкзак 4 уровень", Items.MAGENTA_SHULKER_BOX, 0, "huge"));
         items.add(new HolyWorldProvider.HolyWorldBackpackItem("Рюкзак Infinity", "рюкзак infinity", Items.LIME_SHULKER_BOX, 0, "infinity"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Трапка", "Трапка", Items.POPPED_CHORUS_FRUIT, 0, "ALTERNATIVE_TRAP"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Взрывная трапка", "Взрывная трапка", Items.PRISMARINE_SHARD, 0, "EXPLOSIVE_TRAP"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Стан", "Стан", Items.NETHER_STAR, 0, "STUN_STAR"));
         items.add(new HolyWorldProvider.HolyWorldKringeItem("Взрывная штучка", "Взрывная штучка", Items.FIRE_CHARGE, 0, "ExplosiveStuff"));
         items.add(new HolyWorldProvider.HolyWorldKringeItem("Ком снега", "Ком снега", Items.SNOWBALL, 0, "SnowBall"));
         items.add(new HolyWorldProvider.HolyWorldRuneItem("Руна «Бессмертие»", "Бессмертие", Items.ORANGE_DYE, 0, "immortality"));
         items.add(
            new HolyWorldProvider.HolyWorldPotionItem(
               "Улучшенное зелье силы", "Улучшенное зелье силы", Items.POTION, 0, StatusEffects.STRENGTH, 2, List.of(3600, 7200)
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldPotionItem(
               "Улучшенное зелье скорости", "Улучшенное зелье скорости", Items.POTION, 0, StatusEffects.SPEED, 2, List.of(3600)
            )
         );
         items.add(new HolyWorldProvider.HolyWorldKringeItem("Зелье победителя", "Зелье победителя", Items.POTION, 0, "win-potion", 33461));
         items.add(
            new HolyWorldProvider.HolyWorldPotionItem(
               "Зелье исцеления", "Зелье исцеление", Items.POTION, 0, StatusEffects.INSTANT_HEALTH, 1, List.of(1), 16711680
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldStandardPotionItem(
               "Зелье черепашьей мощи", "Зелье черепашьей мощи", Items.POTION, 0, "minecraft:long_turtle_master", 8369336
            )
         );
         items.add(
            new HolyWorldProvider.HolyWorldStandardPotionItem(
               "Зелье черепашьей мощи II", "Зелье черепашьей мощи", Items.POTION, 0, "minecraft:strong_turtle_master", 8369336
            )
         );
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Охотник", "Охотник", Items.NETHERITE_SWORD, 0, "EXP_DROPPER"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Снеговик", "Снеговик", Items.SNOW_BLOCK, 0, "BLINDNESS"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Иллюминатор", "Иллюминатор", Items.SEA_LANTERN, 0, "PORTHOLE"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Эндермен", "Эндермен", Items.ENDER_PEARL, 0, "ENDERMAN"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Анти Фантом", "Анти Фантом", Items.PHANTOM_MEMBRANE, 0, "ANTI_PHANTOM"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Телекинез", "Телекинез", Items.HONEY_BLOCK, 0, "TELEKINESIS"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Гравитация", "Гравитация", Items.FEATHER, 0, "GRAVITY"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Вампиризм", "Вампиризм", Items.WITHER_SKELETON_SKULL, 0, "VAMPIRE"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Справедливость", "Справедливость", Items.POTION, 0, "JUSTICE"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Универсальный ключ", "Универсальный ключ", Items.TRIPWIRE_HOOK, 0, "UNIVERSAL_KEY"));
         items.add(new HolyWorldProvider.HolyWorldKringeEffectItem("Фармер", "Фармер", Items.DIAMOND_SWORD, 0, "FARMER"));
         items.add(new HolyWorldProvider.HolyWorldItem("Зачарованное золотое яблоко", Items.ENCHANTED_GOLDEN_APPLE, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Золотое яблоко", Items.GOLDEN_APPLE, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Золотая морковь", Items.GOLDEN_CARROT, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Эндер жемчуг", "Эндер-жемчуг", Items.ENDER_PEARL, 0, null, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Плод хоруса", Items.CHORUS_FRUIT, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldKringeItem("Артефакт", "Артефакт", Items.CONDUIT, 0, "EmptyArtefact"));
         items.add(new HolyWorldProvider.HolyWorldItem("Фейерверк", Items.FIREWORK_ROCKET, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Незеритовый слиток", Items.NETHERITE_INGOT, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Порох", Items.GUNPOWDER, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldItem("Боевой фрагмент", Items.PRISMARINE_CRYSTALS, 0, null, null));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Взрывчатое вещество", "Взрывчатое вещество", Items.CLAY, 0, "EXPLOSIVE_SUBSTANCE"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Динамит А", "Динамит А", Items.TNT, 0, "A"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Динамит B", "динамит б", Items.TNT, 0, "B"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("Динамит B2", "динамит б2", Items.TNT, 0, "B2"));
         items.add(new HolyWorldProvider.HolyWorldPyrotechnicItem("C4 ВзРыВчАтКа", "с4 взрывчатка", Items.TNT, 0, "C4"));
         items.add(new HolyWorldProvider.HolyWorldKringeItem("Золотая кирка Джейка", "Золотая кирка Джейка", Items.GOLDEN_PICKAXE, 0, "jake-pickaxe"));
         items.add(
            new HolyWorldProvider.HolyWorldSphereShardItem(
               "Осколок сферы",
               "Осколок сферы",
               Items.PLAYER_HEAD,
               0,
               "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmY3YmJjZTIzZTgxNjJlNDJkMjA3MDU1YjBjZTkwZjBlZDU3YjAxNWU1MjEyMTM5YWM4ZmM3ZTZkNDVkZGZjYSJ9fX0="
            )
         );
      }

      return items;
   }

   public static void reload() {
      items = null;
   }

   private static void addEnchantments(ItemStack var0, HolyWorldProvider.EnchantmentData[] var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.world != null) {
         DynamicRegistryManager var3 = var2.world.getRegistryManager();
         Impl var4 = var3.getOrThrow(RegistryKeys.ENCHANTMENT);
         Builder var5 = new Builder((ItemEnchantmentsComponent)var0.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT));

         for (HolyWorldProvider.EnchantmentData var9 : var1) {
            Optional var10 = var4.getOptional(var9.enchantment);
            if (var10.isPresent()) {
               var5.add((RegistryEntry)var10.get(), var9.level);
            }
         }

         var0.set(DataComponentTypes.ENCHANTMENTS, var5.build());
      }
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

   public static class EnchantmentData {
      public final RegistryKey<Enchantment> enchantment;
      public final int level;

      public EnchantmentData(RegistryKey<Enchantment> var1, int var2) {
         this.enchantment = var1;
         this.level = var2;
      }
   }

   public static class HolyWorldBackpackItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String backpackType;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldBackpackItem(String var1, String var2, Item var3, int var4, String var5) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.backpackType = var5;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.LIGHT_PURPLE}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldBackpack", true);
         var2.putString("backpackType", this.backpackType);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

      public String getBackpackType() {
         return this.backpackType;
      }
   }

   public static class HolyWorldExpBottleItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final int expValue;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldExpBottleItem(String var1, String var2, Item var3, int var4, int var5) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.expValue = var5;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.GOLD}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldExpBottle", true);
         var2.putInt("holy-exp-bottle-value", this.expValue);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

   public static class HolyWorldItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final HolyWorldProvider.EnchantmentData[] requiredEnchantments;
      private final HolyWorldProvider.AttributeData[] attributes;
      private final List<Text> loreTexts;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldItem(String var1, Item var2, int var3, HolyWorldProvider.EnchantmentData[] var4, List<Text> var5) {
         this(var1, null, var2, var3, var4, null, var5);
      }

      public HolyWorldItem(String var1, Item var2, int var3, HolyWorldProvider.EnchantmentData[] var4, HolyWorldProvider.AttributeData[] var5, List<Text> var6) {
         this(var1, null, var2, var3, var4, var5, var6);
      }

      public HolyWorldItem(
         String var1, String var2, Item var3, int var4, HolyWorldProvider.EnchantmentData[] var5, HolyWorldProvider.AttributeData[] var6, List<Text> var7
      ) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.requiredEnchantments = var5;
         this.attributes = var6;
         this.loreTexts = var7;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         if (this.requiredEnchantments != null && this.requiredEnchantments.length > 0) {
            HolyWorldProvider.addEnchantments(var1, this.requiredEnchantments);
         }

         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.GREEN}));
         if (this.loreTexts != null && !this.loreTexts.isEmpty()) {
            var1.set(DataComponentTypes.LORE, new LoreComponent(this.loreTexts));
         }

         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putInt("RequiredEnchantCount", this.requiredEnchantments != null ? this.requiredEnchantments.length : 0);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.attributes != null && this.attributes.length > 0) {
            NbtList var3 = new NbtList();

            for (HolyWorldProvider.AttributeData var7 : this.attributes) {
               NbtCompound var8 = new NbtCompound();
               var8.putString("AttributeName", var7.attributeName);
               var8.putDouble("Amount", var7.amount);
               var8.putInt("Operation", var7.operation);
               var8.putString("Slot", var7.slot);
               var8.putString("Name", UUID.randomUUID().toString());
               var8.putIntArray(
                  "UUID",
                  new int[]{
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9)
                  }
               );
               var3.add(var8);
            }

            var2.put("AttributeModifiers", var3);
         }

         if (this.requiredEnchantments != null && this.requiredEnchantments.length > 0) {
            NbtList var9 = new NbtList();

            for (HolyWorldProvider.EnchantmentData var13 : this.requiredEnchantments) {
               NbtCompound var14 = new NbtCompound();
               var14.putString("id", var13.enchantment.getValue().toString());
               var14.putShort("lvl", (short)var13.level);
               var9.add(var14);
            }

            var2.put("RequiredEnchantments", var9);
         }

         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

      public HolyWorldProvider.EnchantmentData[] getRequiredEnchantments() {
         return this.requiredEnchantments;
      }
   }

   public static class HolyWorldKringeEffectItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String effectType;
      private final Integer customColor;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldKringeEffectItem(String var1, String var2, Item var3, int var4, String var5) {
         this(var1, var2, var3, var4, var5, null);
      }

      public HolyWorldKringeEffectItem(String var1, String var2, Item var3, int var4, String var5, Integer var6) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.effectType = var5;
         this.customColor = var6;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.GOLD}));
         if (this.customColor != null && this.material == Items.POTION) {
            PotionContentsComponent var2 = new PotionContentsComponent(Optional.empty(), Optional.of(this.customColor), List.of(), Optional.empty());
            var1.set(DataComponentTypes.POTION_CONTENTS, var2);
         }

         NbtCompound var3 = new NbtCompound();
         var3.putBoolean("HolyWorldItem", true);
         var3.putBoolean("HolyWorldKringeEffect", true);
         var3.putString("effectType", this.effectType);
         if (this.customColor != null) {
            var3.putInt("customColor", this.customColor);
         }

         var3.putInt("HideFlags", 127);
         var3.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var3));
         return var1;
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

      public String getEffectType() {
         return this.effectType;
      }
   }

   public static class HolyWorldKringeItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String kringeType;
      private final Integer customColor;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldKringeItem(String var1, String var2, Item var3, int var4, String var5) {
         this(var1, var2, var3, var4, var5, null);
      }

      public HolyWorldKringeItem(String var1, String var2, Item var3, int var4, String var5, Integer var6) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.kringeType = var5;
         this.customColor = var6;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.RED}));
         if (this.customColor != null && this.material == Items.POTION) {
            PotionContentsComponent var2 = new PotionContentsComponent(Optional.empty(), Optional.of(this.customColor), List.of(), Optional.empty());
            var1.set(DataComponentTypes.POTION_CONTENTS, var2);
         }

         NbtCompound var3 = new NbtCompound();
         var3.putBoolean("HolyWorldItem", true);
         var3.putBoolean("HolyWorldKringe", true);
         var3.putString("kringeType", this.kringeType);
         if (this.customColor != null) {
            var3.putInt("customColor", this.customColor);
         }

         var3.putInt("HideFlags", 127);
         var3.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var3));
         return var1;
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

      public String getKringeType() {
         return this.kringeType;
      }
   }

   public static class HolyWorldMultiEffectPotionItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final int customColor;
      private final List<StatusEffectInstance> effects;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldMultiEffectPotionItem(String var1, String var2, Item var3, int var4, int var5, List<StatusEffectInstance> var6) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.customColor = var5;
         this.effects = var6;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.AQUA}));
         PotionContentsComponent var2 = new PotionContentsComponent(Optional.empty(), Optional.of(this.customColor), this.effects, Optional.empty());
         var1.set(DataComponentTypes.POTION_CONTENTS, var2);
         NbtCompound var3 = new NbtCompound();
         var3.putBoolean("HolyWorldItem", true);
         var3.putBoolean("HolyWorldMultiEffectPotion", true);
         var3.putInt("customColor", this.customColor);
         NbtList var4 = new NbtList();

         for (StatusEffectInstance var6 : this.effects) {
            NbtCompound var7 = new NbtCompound();
            var7.putString("effectId", var6.getEffectType().getIdAsString());
            var7.putInt("amplifier", var6.getAmplifier());
            var7.putInt("duration", var6.getDuration());
            var4.add(var7);
         }

         var3.put("effects", var4);
         var3.putInt("HideFlags", 127);
         var3.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var3));
         return var1;
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

      public List<StatusEffectInstance> getEffects() {
         return this.effects;
      }

      public int getCustomColor() {
         return this.customColor;
      }
   }

   public static class HolyWorldPotionItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final RegistryEntry<StatusEffect> effectType;
      private final int amplifier;
      private final List<Integer> allowedDurations;
      private final Integer customColor;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldPotionItem(String var1, String var2, Item var3, int var4, RegistryEntry<StatusEffect> var5, int var6, List<Integer> var7) {
         this(var1, var2, var3, var4, var5, var6, var7, null);
      }

      public HolyWorldPotionItem(String var1, String var2, Item var3, int var4, RegistryEntry<StatusEffect> var5, int var6, List<Integer> var7, Integer var8) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.effectType = var5;
         this.amplifier = var6;
         this.allowedDurations = var7;
         this.customColor = var8;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.RED}));
         StatusEffectInstance var2 = new StatusEffectInstance(
            this.effectType, this.allowedDurations.isEmpty() ? 3600 : this.allowedDurations.get(0), this.amplifier
         );
         PotionContentsComponent var3 = new PotionContentsComponent(
            Optional.empty(), this.customColor != null ? Optional.of(this.customColor) : Optional.empty(), List.of(var2), Optional.empty()
         );
         var1.set(DataComponentTypes.POTION_CONTENTS, var3);
         NbtCompound var4 = new NbtCompound();
         var4.putBoolean("HolyWorldItem", true);
         var4.putBoolean("HolyWorldPotion", true);
         var4.putString("effectId", this.effectType.getIdAsString());
         var4.putInt("amplifier", this.amplifier);
         NbtList var5 = new NbtList();

         for (Integer var7 : this.allowedDurations) {
            NbtCompound var8 = new NbtCompound();
            var8.putInt("duration", var7);
            var5.add(var8);
         }

         var4.put("allowedDurations", var5);
         var4.putInt("HideFlags", 127);
         var4.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var4));
         return var1;
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

      public RegistryEntry<StatusEffect> getEffectType() {
         return this.effectType;
      }

      public int getAmplifier() {
         return this.amplifier;
      }

      public List<Integer> getAllowedDurations() {
         return this.allowedDurations;
      }
   }

   public static class HolyWorldPyrotechnicItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String pyrotechnicType;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldPyrotechnicItem(String var1, String var2, Item var3, int var4, String var5) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.pyrotechnicType = var5;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.LIGHT_PURPLE}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldPyrotechnic", true);
         var2.putString("pyrotechnicType", this.pyrotechnicType);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

      public String getPyrotechnicType() {
         return this.pyrotechnicType;
      }
   }

   public static class HolyWorldRuneItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String runeId;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldRuneItem(String var1, String var2, Item var3, int var4, String var5) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.runeId = var5;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.GOLD}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldRune", true);
         var2.putString("runeId", this.runeId);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         if ("immortality".equals(this.runeId)) {
            var1.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
         }

         return var1;
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

      public String getRuneId() {
         return this.runeId;
      }
   }

   public static class HolyWorldSphereItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String skullUuid;
      private final String texture;
      private final String sphereName;
      private final String requiredEffects;
      private final HolyWorldProvider.AttributeData[] attributes;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldSphereItem(String var1, String var2, Item var3, int var4, String var5, String var6, String var7) {
         this(var1, var2, var3, var4, var5, var6, var7, null);
      }

      public HolyWorldSphereItem(String var1, String var2, Item var3, int var4, String var5, String var6, String var7, String var8) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.skullUuid = var5;
         this.texture = var6;
         this.sphereName = var7;
         this.requiredEffects = var8;
         this.attributes = null;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.RED}));
         NbtCompound var2 = new NbtCompound();
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.texture != null && !this.texture.isEmpty() && this.material == Items.PLAYER_HEAD) {
            UUID var3;
            if (this.skullUuid != null && !this.skullUuid.isEmpty()) {
               try {
                  var3 = UUID.fromString(this.skullUuid);
               } catch (IllegalArgumentException var9) {
                  String var5 = "HolyWorldSphere:" + this.displayName + ":" + this.texture;
                  var3 = UUID.nameUUIDFromBytes(var5.getBytes(StandardCharsets.UTF_8));
               }
            } else {
               String var4 = "HolyWorldSphere:" + this.displayName + ":" + this.texture;
               var3 = UUID.nameUUIDFromBytes(var4.getBytes(StandardCharsets.UTF_8));
            }

            NbtCompound var10 = new NbtCompound();
            var10.putUuid("Id", var3);
            NbtCompound var11 = new NbtCompound();
            NbtList var6 = new NbtList();
            NbtCompound var7 = new NbtCompound();
            var7.putString("Value", this.texture);
            var6.add(var7);
            var11.put("textures", var6);
            var10.put("Properties", var11);
            var2.put("SkullOwner", var10);
            GameProfile var8 = new GameProfile(var3, "");
            var8.getProperties().put("textures", new Property("textures", this.texture));
            var1.set(DataComponentTypes.PROFILE, new ProfileComponent(var8));
         }

         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldSphere", true);
         if (this.sphereName != null && !this.sphereName.isEmpty()) {
            var2.putString("sphereName", this.sphereName);
         }

         if (this.requiredEffects != null && !this.requiredEffects.isEmpty()) {
            var2.putString("requiredEffects", this.requiredEffects);
         }

         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

   public static class HolyWorldSphereShardItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String texture;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldSphereShardItem(String var1, String var2, Item var3, int var4, String var5) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.texture = var5;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.LIGHT_PURPLE}));
         NbtCompound var2 = new NbtCompound();
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.texture != null && !this.texture.isEmpty() && this.material == Items.PLAYER_HEAD) {
            UUID var3 = UUID.fromString("9afca6b1-556f-3cf9-b349-3886d7d2c53b");
            NbtCompound var4 = new NbtCompound();
            var4.putUuid("Id", var3);
            NbtCompound var5 = new NbtCompound();
            NbtList var6 = new NbtList();
            NbtCompound var7 = new NbtCompound();
            var7.putString("Value", this.texture);
            var6.add(var7);
            var5.put("textures", var6);
            var4.put("Properties", var5);
            var2.put("SkullOwner", var4);
            var2.putIntArray("SkullOwnerOrig", new int[]{0, 778770836, 0, 778770836});
            GameProfile var8 = new GameProfile(var3, "");
            var8.getProperties().put("textures", new Property("textures", this.texture));
            var1.set(DataComponentTypes.PROFILE, new ProfileComponent(var8));
         }

         NbtCompound var9 = new NbtCompound();
         var9.putByte("magicspheres:burned-sphere-shard", (byte)1);
         var2.put("PublicBukkitValues", var9);
         var2.putInt("sphereEffect", 1);
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldSphereShard", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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

   public static class HolyWorldStandardPotionItem implements AutoBuyableItem {
      private final String displayName;
      private final String searchName;
      private final Item material;
      private final int price;
      private final String potionType;
      private final Integer customColor;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldStandardPotionItem(String var1, String var2, Item var3, int var4, String var5) {
         this(var1, var2, var3, var4, var5, null);
      }

      public HolyWorldStandardPotionItem(String var1, String var2, Item var3, int var4, String var5, Integer var6) {
         this.displayName = var1;
         this.searchName = var2;
         this.material = var3;
         this.price = var4;
         this.potionType = var5;
         this.customColor = var6;
         this.enabled = true;
         this.settings = new AutoBuyItemSettings(var4, var3, var1);
         AutoBuySettingsManager.getInstance().loadSettings(var1, this.settings);
      }

      @Override
      public String getDisplayName() {
         return this.displayName;
      }

      @Override
      public String getSearchName() {
         return this.searchName != null ? this.searchName : this.displayName;
      }

      @Override
      public ItemStack createItemStack() {
         ItemStack var1 = new ItemStack(this.material);
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.AQUA}));
         if (this.customColor != null) {
            PotionContentsComponent var2 = new PotionContentsComponent(Optional.empty(), Optional.of(this.customColor), List.of(), Optional.empty());
            var1.set(DataComponentTypes.POTION_CONTENTS, var2);
         }

         NbtCompound var3 = new NbtCompound();
         var3.putBoolean("HolyWorldItem", true);
         var3.putBoolean("HolyWorldStandardPotion", true);
         var3.putString("potionType", this.potionType);
         if (this.customColor != null) {
            var3.putInt("customColor", this.customColor);
         }

         var3.putInt("HideFlags", 127);
         var3.putBoolean("Unbreakable", true);
         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var3));
         return var1;
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

      public String getPotionType() {
         return this.potionType;
      }
   }

   public static class HolyWorldTalikItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final HolyWorldProvider.AttributeData[] attributes;
      private final HolyWorldProvider.EnchantmentData[] enchantments;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public HolyWorldTalikItem(String var1, Item var2, int var3, HolyWorldProvider.AttributeData[] var4, HolyWorldProvider.EnchantmentData[] var5) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.attributes = var4;
         this.enchantments = var5;
         this.settings = new AutoBuyItemSettings(var3, var2, var1);
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
         ItemStack var1 = new ItemStack(this.material);
         if (this.enchantments != null && this.enchantments.length > 0) {
            HolyWorldProvider.addEnchantments(var1, this.enchantments);
         }

         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.BLUE}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("HolyWorldItem", true);
         var2.putBoolean("HolyWorldTalik", true);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.attributes != null && this.attributes.length > 0) {
            NbtList var3 = new NbtList();

            for (HolyWorldProvider.AttributeData var7 : this.attributes) {
               NbtCompound var8 = new NbtCompound();
               var8.putString("AttributeName", var7.attributeName);
               var8.putDouble("Amount", var7.amount);
               var8.putInt("Operation", var7.operation);
               var8.putString("Slot", var7.slot);
               var8.putString("Name", UUID.randomUUID().toString());
               var8.putIntArray(
                  "UUID",
                  new int[]{
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9)
                  }
               );
               var3.add(var8);
            }

            var2.put("AttributeModifiers", var3);
         }

         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var2));
         return var1;
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
}
