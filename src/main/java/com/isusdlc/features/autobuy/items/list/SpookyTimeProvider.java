package com.isusdlc.features.autobuy.items.list;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.settings.AutoBuyItemSettings;
import com.isusdlc.features.autobuy.settings.AutoBuySettingsManager;
import java.util.ArrayList;
import java.util.Arrays;
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

public class SpookyTimeProvider {
   private static List<AutoBuyableItem> items = null;

   public static List<AutoBuyableItem> getItems() {
      if (items == null) {
         items = new ArrayList<>();
         addItems();
      }

      return items;
   }

   public static void reload() {
      items = null;
   }

   private static void addItems() {
      List<SpookyTimeProvider.EnchantmentData> var0 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.AQUA_AFFINITY, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.RESPIRATION, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var1 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var2 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var3 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.BLAST_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.DEPTH_STRIDER, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FEATHER_FALLING, 4),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROJECTILE_PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PROTECTION, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.SOUL_SPEED, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var4 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.BANE_OF_ARTHROPODS, 7),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_ASPECT, 2),
         new SpookyTimeProvider.EnchantmentData(Enchantments.LOOTING, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.SHARPNESS, 7),
         new SpookyTimeProvider.EnchantmentData(Enchantments.SMITE, 7),
         new SpookyTimeProvider.EnchantmentData(Enchantments.SWEEPING_EDGE, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var5 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.CHANNELING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FIRE_ASPECT, 2),
         new SpookyTimeProvider.EnchantmentData(Enchantments.IMPALING, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.LOYALTY, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.SHARPNESS, 7),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var6 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MULTISHOT, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.PIERCING, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.QUICK_CHARGE, 3),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 3)
      );
      List<SpookyTimeProvider.EnchantmentData> var7 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.EFFICIENCY, 10),
         new SpookyTimeProvider.EnchantmentData(Enchantments.FORTUNE, 5),
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1),
         new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var8 = Arrays.asList(
         new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, -1), new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 5)
      );
      List<SpookyTimeProvider.EnchantmentData> var9 = Arrays.asList(new SpookyTimeProvider.EnchantmentData(Enchantments.MENDING, 1));
      items.add(
         new SpookyTimeProvider.SpookyTimeItem("Шлем Крушителя", Items.NETHERITE_HELMET, 0, var0.toArray(new SpookyTimeProvider.EnchantmentData[0]), null)
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeItem(
            "Нагрудник Крушителя", Items.NETHERITE_CHESTPLATE, 0, var1.toArray(new SpookyTimeProvider.EnchantmentData[0]), null
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeItem("Поножи Крушителя", Items.NETHERITE_LEGGINGS, 0, var2.toArray(new SpookyTimeProvider.EnchantmentData[0]), null)
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeItem("Ботинки Крушителя", Items.NETHERITE_BOOTS, 0, var3.toArray(new SpookyTimeProvider.EnchantmentData[0]), null)
      );
      items.add(new SpookyTimeProvider.SpookyTimeItem("Меч Крушителя", Items.NETHERITE_SWORD, 0, var4.toArray(new SpookyTimeProvider.EnchantmentData[0]), null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Трезубец Крушителя", Items.TRIDENT, 0, var5.toArray(new SpookyTimeProvider.EnchantmentData[0]), null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Арбалет Крушителя", Items.CROSSBOW, 0, var6.toArray(new SpookyTimeProvider.EnchantmentData[0]), null));
      items.add(
         new SpookyTimeProvider.SpookyTimeItem("Кирка Крушителя", Items.NETHERITE_PICKAXE, 0, var7.toArray(new SpookyTimeProvider.EnchantmentData[0]), null)
      );
      items.add(new SpookyTimeProvider.SpookyTimeItem("Элитры Крушителя", Items.ELYTRA, 0, var8.toArray(new SpookyTimeProvider.EnchantmentData[0]), null));
      SpookyTimeProvider.EnchantmentData[] var10 = new SpookyTimeProvider.EnchantmentData[]{new SpookyTimeProvider.EnchantmentData(Enchantments.UNBREAKING, 1)};
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Карателя",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
               createAttributes("minecraft:generic.max_health", -4.0, 0, "offhand"),
               createAttributes("minecraft:generic.attack_damage", 7.0, 0, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Крушителя",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.armor", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.armor_toughness", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
               createAttributes("minecraft:generic.max_health", 4.0, 0, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Раздора",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.attack_damage", 4.0, 0, "offhand"),
               createAttributes("minecraft:generic.armor", -3.0, 0, "offhand"),
               createAttributes("minecraft:generic.max_health", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
               createAttributes("minecraft:generic.attack_speed", 0.1, 1, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Тирана",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.armor", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.max_health", -4.0, 0, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Ярости",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.attack_damage", 5.0, 0, "offhand"), createAttributes("minecraft:generic.max_health", -4.0, 0, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Вихря",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.max_health", 2.0, 0, "offhand"),
               createAttributes("minecraft:generic.attack_speed", 0.15, 1, "offhand"),
               createAttributes("minecraft:generic.movement_speed", 0.15, 1, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Мрака",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.max_health", 1.5, 0, "offhand"), createAttributes("minecraft:generic.armor", 1.5, 0, "offhand")
            },
            var10
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeTalismanItem(
            "Талисман Демона",
            Items.TOTEM_OF_UNDYING,
            0,
            new SpookyTimeProvider.AttributeData[]{
               createAttributes("minecraft:generic.attack_speed", 0.1, 1, "offhand"), createAttributes("minecraft:generic.attack_damage", 2.5, 0, "offhand")
            },
            var10
         )
      );
      items.add(new SpookyTimeProvider.SpookyTimeTalismanItem("Тотем бессмертия", Items.TOTEM_OF_UNDYING, 0, null, null));
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Афины",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJTcGhlcmVBdGhlbmEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTNmOWVlZGEzYmEyM2ZlMTQyM2M0MDM2ZTdkZDBhNzQ0NjFkZmY5NmJhZGM1YjJmMmI5ZmFhN2NjMTZmMzgyZiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            createAttributes("minecraft:generic.attack_speed", 0.15, 1, "offhand"),
            createAttributes("minecraft:generic.movement_speed", 0.15, 1, "offhand"),
            createAttributes("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", -2.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Титана",
            Items.PLAYER_HEAD,
            0,
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFlOTY5ODQ1OGI3ODQxYzk2YWU0ZjI0ZWM4NGFlMDE3MjQxMDA2NDFjNTY0ZTJhN2IxODVmNDA2ZThlZDIzIn19fQ==",
            createAttributes("minecraft:generic.armor", 3.0, 0, "offhand"),
            createAttributes("minecraft:generic.armor_toughness", 3.0, 0, "offhand"),
            createAttributes("minecraft:generic.movement_speed", -0.15, 1, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Хаоса",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODY0MTkwMCwKICAicHJvZmlsZUlkIiA6ICIxNzRjZmRiNGEzY2I0M2I1YmZjZGU0MjRjM2JiMmM2ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJtYXJhZWwxOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lN2E3YWU3Y2RjZjYxNmU4YjdhNDIyMWE2MjFiMjQzNTc1M2M2MGVkNmEyNThlYTA2MGRhZTMwMDJmZmU5ZTI4IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
            createAttributes("minecraft:generic.attack_damage", 3.0, 0, "offhand"),
            createAttributes("minecraft:generic.movement_speed", 0.07, 1, "offhand"),
            createAttributes("minecraft:generic.attack_speed", 0.13, 1, "offhand"),
            createAttributes("minecraft:generic.armor", 2.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", -4.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Сатира",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODYwODUyOCwKICAicHJvZmlsZUlkIiA6ICJkMTQ4NjFiM2UwZmM0Njk5OTFlMTcyNTllMzdiZjZhZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJyYXhpdG9jbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83NzFhOWE0OThiNGZhNWVjNDkzNjJmOWJjODhlZGE0ZjUyYjA0ZGU0OWQ3NWFhM2NhMzMyYTFmZWExYWEwZTU3IiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=",
            createAttributes("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
            createAttributes("minecraft:generic.attack_speed", 0.15, 1, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Бестии",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0MzgzNDkzMCwKICAicHJvZmlsZUlkIiA6ICI1MzUzNWIxN2M0ZDY0NWQ0YWUwY2U2ZjM4Zjk0NTFjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJVYml2aXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQxMWFjMTczODFiOWZjZTliYWIzYzcyYWZkYjdmMTk4NTcwZGFmNDczMmJkODExZDMxYzIyN2Q4MGZhMzliMSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            createAttributes("minecraft:generic.armor", 1.0, 0, "offhand"),
            createAttributes("minecraft:generic.movement_speed", 0.1, 1, "offhand"),
            createAttributes("minecraft:generic.attack_speed", 0.1, 1, "offhand"),
            createAttributes("minecraft:generic.max_health", 4.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Ареса",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzc3NDI1NSwKICAicHJvZmlsZUlkIiA6ICJhYWMxYjA2OWNkMjE0NWE2ODNlNzQxNzE4MDcxMGU4MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJqdXNhbXUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE2YWRjNmJhZmNiNTdmZDcwN2RlZTdkZDZhNzM2ZmUxMjY3MTFkNTNhMWZkNmNlNzg5ZGE0MWIzYmUxM2YyYSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            createAttributes("minecraft:generic.attack_damage", 6.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", -2.0, 0, "offhand"),
            createAttributes("minecraft:generic.armor", -2.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Гидры",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODUzMjE4MywKICAicHJvZmlsZUlkIiA6ICI1OGZmZWI5NTMxNGQ0ODcwYTQwYjVjYjQyZDRlYTU5OCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTa2luREJuZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2UzYzExOGQ2OTZkOTEwZTU0ZGUwMmNhNGQ4MDc1NDNmOWIxOGMwMDhjOTgzOGQyZmY2OTM3NzYyMmZiMWQzMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
            createAttributes("minecraft:generic.armor", 2.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", 4.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Икара",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDI3ODU4MjQ5MSwKICAicHJvZmlsZUlkIiA6ICJhZWNkODIxZTQyYzE0ZDJlOThmNTA1OTg1MWI5OWMzNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJSb2RyaVgyMDc1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2M2ODAzZTZkNTY2N2EyZDYxMDYyOGJjM2IzMmY4NjNjZGE0OTVjNDY1NjE2ZGU2NTVjYjMyOTkzM2I2MWFmNzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
            createAttributes("minecraft:generic.attack_damage", 2.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", 2.0, 0, "offhand")
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimeSphereItem(
            "Сфера Эрида",
            Items.PLAYER_HEAD,
            0,
            "ewogICJ0aW1lc3RhbXAiIDogMTc1MDM0Mzg2MTE4NywKICAicHJvZmlsZUlkIiA6ICJlZGUyYzdhMGFjNjM0MTNiYjA5ZDNmMGJlZTllYzhlYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ0aGVEZXZKYWRlIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzZlNGUyZjEwNDdmM2VjNmU5ZTQ1OTE4NDczOWUzM2I3YzFmYzYzYWQ4MjAyYmRhYjlmMDI0NTA4YWRkMjNlNWIiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
            createAttributes("minecraft:generic.luck", 1.0, 0, "offhand"),
            createAttributes("minecraft:generic.max_health", 2.0, 0, "offhand")
         )
      );
      items.add(new SpookyTimeProvider.SpookyTimeItem("Пузырёк опыта", Items.EXPERIENCE_BOTTLE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Чарка", Items.ENCHANTED_GOLDEN_APPLE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Золотое яблоко", Items.GOLDEN_APPLE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Яблоко", Items.APPLE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Перка", Items.ENDER_PEARL, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Незеритовый слиток", Items.NETHERITE_INGOT, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Незеритовый лом", Items.NETHERITE_SCRAP, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Алмаз", Items.DIAMOND, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Изумруд", Items.EMERALD, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Золотой слиток", Items.GOLD_INGOT, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Алмазный блок", Items.DIAMOND_BLOCK, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Изумрудный блок", Items.EMERALD_BLOCK, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Золотой блок", Items.GOLD_BLOCK, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Обсидиан", Items.OBSIDIAN, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Голова дракона", Items.DRAGON_HEAD, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Голова визер-скелета", Items.WITHER_SKELETON_SKULL, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Древние обломки", Items.ANCIENT_DEBRIS, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Яйцо призыва крестьянина", Items.VILLAGER_SPAWN_EGG, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Яйцо зомби-крестьянина", Items.ZOMBIE_VILLAGER_SPAWN_EGG, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Элитры", Items.ELYTRA, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Золотая морковь", Items.GOLDEN_CARROT, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Шалкер", Items.SHULKER_BOX, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Маяк", Items.BEACON, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Алмазная руда", Items.DIAMOND_ORE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Изумрудная руда", Items.EMERALD_ORE, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Спавнер", Items.SPAWNER, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Порох", Items.GUNPOWDER, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Проклятая душа", Items.SOUL_LANTERN, 0, "soul-currency"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Трапка", Items.NETHERITE_SCRAP, 0, "schematic-item-trap"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Дезориентация", Items.ENDER_EYE, 0, "effect-item-diz"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Явная пыль", Items.SUGAR, 0, "effect-item-dust"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Пласт", Items.DRIED_KELP, 0, "schematic-item-plast"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Божья аура", Items.PHANTOM_MEMBRANE, 0, "effect-item-god"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Снежок заморозка", Items.SNOWBALL, 0, "effect-item-snowball"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Молот Тора", Items.NETHERITE_PICKAXE, 0, "radius-item-mega-buldozer"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Божье касание", Items.GOLDEN_PICKAXE, 0, "spawner-item-spawner-break"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Мощный удар", Items.GOLDEN_PICKAXE, 0, "bedrock-item-bedrock-break"));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Книга починка", Items.ENCHANTED_BOOK, 0, var9.toArray(new SpookyTimeProvider.EnchantmentData[0]), null));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Отмычка к сферам", Items.TRIPWIRE_HOOK, 0, "spheres"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Отмычка к броне", Items.TRIPWIRE_HOOK, 0, "armors"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Отмычка к оружию", Items.TRIPWIRE_HOOK, 0, "weapons"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Отмычка к инструментам", Items.TRIPWIRE_HOOK, 0, "tools"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Отмычка к ресурсам", Items.TRIPWIRE_HOOK, 0, "resources"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Обычный мист", Items.CAMPFIRE, 0, "MILD"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Богатый мист", Items.CAMPFIRE, 0, "WEAK"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Легендарный мист", Items.CAMPFIRE, 0, "MEDIUM"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Прогрузчик чанков 1x1", Items.STRUCTURE_BLOCK, 0, "executable-block-chunker-1"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Прогрузчик чанков 2x2", Items.STRUCTURE_BLOCK, 0, "executable-block-chunker-2"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Прогрузчик чанков 3x3", Items.STRUCTURE_BLOCK, 0, "executable-block-chunker-2"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Дамагер", Items.JIGSAW, 0, "executable-block-damager"));
      items.add(new SpookyTimeProvider.SpookyTimeItem("Динамит", Items.TNT, 0, null, null));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Таер вайт", Items.TNT, 0, "tnt-item-white"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Таер блэк", Items.TNT, 0, "tnt-item-black"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Неизбежный скин", Items.PAPER, 0, "trap-skin-item-inevitable"));
      items.add(new SpookyTimeProvider.SpookyTimeSpecialItem("Драконий скин", Items.PAPER, 0, "trap-skin-item-dragon"));
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem("Зелье силы", Items.POTION, 0, Arrays.asList(new StatusEffectInstance(StatusEffects.STRENGTH, 3600, 2)))
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem("Зелье скорости", Items.POTION, 0, Arrays.asList(new StatusEffectInstance(StatusEffects.SPEED, 3600, 2)))
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Зелье исцеления", Items.POTION, 0, Arrays.asList(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1))
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Силка + Скорка (автопарсинг не работает)",
            Items.POTION,
            0,
            Arrays.asList(new StatusEffectInstance(StatusEffects.STRENGTH, 3600, 2), new StatusEffectInstance(StatusEffects.SPEED, 3600, 2))
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Хлопушка",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.SLOWNESS, 200, 9),
               new StatusEffectInstance(StatusEffects.SPEED, 400, 4),
               new StatusEffectInstance(StatusEffects.BLINDNESS, 100, 9),
               new StatusEffectInstance(StatusEffects.GLOWING, 3600, 0)
            ),
            16738740
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Зелье Гнева",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 4), new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 3)),
            10040115
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Зелье Палладина",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.RESISTANCE, 12000, 0),
               new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 12000, 0),
               new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 2),
               new StatusEffectInstance(StatusEffects.INVISIBILITY, 18000, 2)
            ),
            65535
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Святая вода",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 2),
               new StatusEffectInstance(StatusEffects.INVISIBILITY, 12000, 1),
               new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1)
            ),
            16777215
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Зелье Ассасина",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 3),
               new StatusEffectInstance(StatusEffects.SPEED, 6000, 2),
               new StatusEffectInstance(StatusEffects.HASTE, 1200, 0),
               new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1)
            ),
            3355443
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Зелье Радиации",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.POISON, 400, 0),
               new StatusEffectInstance(StatusEffects.WITHER, 400, 0),
               new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 2),
               new StatusEffectInstance(StatusEffects.HUNGER, 400, 4),
               new StatusEffectInstance(StatusEffects.GLOWING, 400, 0)
            ),
            3329330
         )
      );
      items.add(
         new SpookyTimeProvider.SpookyTimePotionItem(
            "Снотворное",
            Items.SPLASH_POTION,
            0,
            Arrays.asList(
               new StatusEffectInstance(StatusEffects.WEAKNESS, 1800, 1),
               new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 1),
               new StatusEffectInstance(StatusEffects.WITHER, 1800, 2),
               new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0)
            ),
            4737096
         )
      );
   }

   private static SpookyTimeProvider.AttributeData createAttributes(String var0, double var1, int var3, String var4) {
      return new SpookyTimeProvider.AttributeData(var0, var1, var3, var4);
   }

   private static void addEnchantments(ItemStack var0, SpookyTimeProvider.EnchantmentData[] var1) {
      MinecraftClient var2 = MinecraftClient.getInstance();
      if (var2.world != null) {
         DynamicRegistryManager var3 = var2.world.getRegistryManager();
         Impl var4 = var3.getOrThrow(RegistryKeys.ENCHANTMENT);
         Builder var5 = new Builder((ItemEnchantmentsComponent)var0.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT));

         for (SpookyTimeProvider.EnchantmentData var9 : var1) {
            if (var9.level > 0) {
               Optional var10 = var4.getOptional(var9.enchantment);
               if (var10.isPresent()) {
                  var5.add((RegistryEntry)var10.get(), var9.level);
               }
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

   public static class SpookyTimeItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final SpookyTimeProvider.EnchantmentData[] requiredEnchantments;
      private final List<Text> loreTexts;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public SpookyTimeItem(String var1, Item var2, int var3, SpookyTimeProvider.EnchantmentData[] var4, List<Text> var5) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.requiredEnchantments = var4;
         this.loreTexts = var5;
         this.enabled = true;
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
         if (this.requiredEnchantments != null && this.requiredEnchantments.length > 0) {
            SpookyTimeProvider.addEnchantments(var1, this.requiredEnchantments);
         }

         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.GREEN}));
         if (this.loreTexts != null && !this.loreTexts.isEmpty()) {
            var1.set(DataComponentTypes.LORE, new LoreComponent(this.loreTexts));
         }

         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("SpookyTimeItem", true);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.requiredEnchantments != null && this.requiredEnchantments.length > 0) {
            NbtList var3 = new NbtList();

            for (SpookyTimeProvider.EnchantmentData var7 : this.requiredEnchantments) {
               NbtCompound var8 = new NbtCompound();
               var8.putString("id", var7.enchantment.getValue().toString());
               var8.putShort("lvl", (short)var7.level);
               var3.add(var8);
            }

            var2.put("RequiredEnchantments", var3);
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

   public static class SpookyTimePotionItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final List<StatusEffectInstance> effects;
      private final Integer customColor;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public SpookyTimePotionItem(String var1, Item var2, int var3, List<StatusEffectInstance> var4) {
         this(var1, var2, var3, var4, null);
      }

      public SpookyTimePotionItem(String var1, Item var2, int var3, List<StatusEffectInstance> var4, Integer var5) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.effects = var4;
         this.customColor = var5;
         this.enabled = true;
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
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.LIGHT_PURPLE}));
         if (this.effects != null && !this.effects.isEmpty()) {
            PotionContentsComponent var2 = new PotionContentsComponent(
               Optional.empty(), this.customColor != null ? Optional.of(this.customColor) : Optional.empty(), this.effects, Optional.empty()
            );
            var1.set(DataComponentTypes.POTION_CONTENTS, var2);
         }

         NbtCompound var7 = new NbtCompound();
         var7.putBoolean("SpookyTimeItem", true);
         var7.putBoolean("SpookyTimePotion", true);
         var7.putInt("HideFlags", 127);
         var7.putBoolean("Unbreakable", true);
         if (this.effects != null && !this.effects.isEmpty()) {
            NbtList var3 = new NbtList();

            for (StatusEffectInstance var5 : this.effects) {
               NbtCompound var6 = new NbtCompound();
               var6.putString("effectId", var5.getEffectType().getIdAsString());
               var6.putInt("amplifier", var5.getAmplifier());
               var6.putInt("duration", var5.getDuration());
               var3.add(var6);
            }

            var7.put("effects", var3);
         }

         var1.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(var7));
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

   public static class SpookyTimeSpecialItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final String spookyItemType;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public SpookyTimeSpecialItem(String var1, Item var2, int var3, String var4) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.spookyItemType = var4;
         this.enabled = true;
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
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.YELLOW}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("SpookyTimeItem", true);
         var2.putBoolean("SpookyTimeSpecial", true);
         var2.putString("spookyItemType", this.spookyItemType);
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

      public String getSpookyItemType() {
         return this.spookyItemType;
      }
   }

   public static class SpookyTimeSphereItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final String texture;
      private final SpookyTimeProvider.AttributeData[] attributes;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public SpookyTimeSphereItem(String var1, Item var2, int var3, String var4, SpookyTimeProvider.AttributeData... var5) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.texture = var4;
         this.attributes = var5;
         this.enabled = true;
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
         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.RED}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("SpookyTimeItem", true);
         var2.putBoolean("SpookyTimeSphere", true);
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
            GameProfile var8 = new GameProfile(var3, "");
            var8.getProperties().put("textures", new Property("textures", this.texture));
            var1.set(DataComponentTypes.PROFILE, new ProfileComponent(var8));
         }

         if (this.attributes != null && this.attributes.length > 0) {
            NbtList var9 = new NbtList();

            for (SpookyTimeProvider.AttributeData var13 : this.attributes) {
               NbtCompound var14 = new NbtCompound();
               var14.putString("AttributeName", var13.attributeName);
               var14.putDouble("Amount", var13.amount);
               var14.putInt("Operation", var13.operation);
               var14.putString("Slot", var13.slot);
               var14.putString("Name", UUID.randomUUID().toString());
               var14.putIntArray(
                  "UUID",
                  new int[]{
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9),
                     (int)(Math.random() * 2.147483647E9)
                  }
               );
               var9.add(var14);
            }

            var2.put("AttributeModifiers", var9);
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

   public static class SpookyTimeTalismanItem implements AutoBuyableItem {
      private final String displayName;
      private final Item material;
      private final int price;
      private final SpookyTimeProvider.AttributeData[] attributes;
      private final SpookyTimeProvider.EnchantmentData[] enchantments;
      private final AutoBuyItemSettings settings;
      private boolean enabled;

      public SpookyTimeTalismanItem(String var1, Item var2, int var3, SpookyTimeProvider.AttributeData... var4) {
         this(var1, var2, var3, var4, null);
      }

      public SpookyTimeTalismanItem(String var1, Item var2, int var3, SpookyTimeProvider.AttributeData[] var4, SpookyTimeProvider.EnchantmentData[] var5) {
         this.displayName = var1;
         this.material = var2;
         this.price = var3;
         this.attributes = var4;
         this.enchantments = var5;
         this.enabled = true;
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
            SpookyTimeProvider.addEnchantments(var1, this.enchantments);
         }

         var1.set(DataComponentTypes.CUSTOM_NAME, Text.literal(this.displayName).formatted(new Formatting[]{Formatting.BOLD, Formatting.YELLOW}));
         NbtCompound var2 = new NbtCompound();
         var2.putBoolean("SpookyTimeItem", true);
         var2.putBoolean("SpookyTimeTalik", true);
         var2.putInt("HideFlags", 127);
         var2.putBoolean("Unbreakable", true);
         if (this.attributes != null && this.attributes.length > 0) {
            NbtList var3 = new NbtList();

            for (SpookyTimeProvider.AttributeData var7 : this.attributes) {
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
