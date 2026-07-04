package com.isusdlc.features.autobuy.items.customitem;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.settings.AutoBuyItemSettings;
import com.isusdlc.features.autobuy.settings.AutoBuySettingsManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CustomItem implements AutoBuyableItem {
   private final String displayName;
   private final String searchName;
   private final NbtCompound nbt;
   private final Item material;
   private final int price;
   private final PotionContentsComponent potionContents;
   private final List<Text> loreTexts;
   private final AutoBuyItemSettings settings;
   private boolean enabled;

   public CustomItem(String displayName, String searchName, NbtCompound nbt, Item material, int price,
                     PotionContentsComponent potionContents, List<Text> loreTexts) {
      this.displayName = displayName;
      this.searchName = searchName;
      this.nbt = nbt;
      this.material = material;
      this.price = price;
      this.potionContents = potionContents;
      this.loreTexts = loreTexts;
      this.enabled = true;
      this.settings = new AutoBuyItemSettings(price, material, displayName);
      AutoBuySettingsManager.getInstance().loadSettings(displayName, settings);
   }

   public CustomItem(String displayName, NbtCompound nbt, Item material, int price,
                     PotionContentsComponent potionContents, List<Text> loreTexts) {
      this(displayName, null, nbt, material, price, potionContents, loreTexts);
   }

   public CustomItem(String displayName, NbtCompound nbt, Item material, int price) {
      this(displayName, null, nbt, material, price, null, null);
   }

   @Override
   public String getDisplayName() { return displayName; }

   @Override
   public String getSearchName() { return searchName != null ? searchName : displayName; }

   @Override
   public ItemStack createItemStack() {
      ItemStack stack = new ItemStack(material);
      stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(displayName));
      if (material == Items.POTION) {
         int color = switch (displayName) {
            case "Хлопушка" -> 16735488;
            case "Святая вода" -> 49664;
            case "Снотворное" -> 16777215;
            case "Зелье гнева" -> 6092799;
            case "Зелье паладина" -> 65280;
            case "Зелье ассасина" -> 16775936;
            case "Зелье радиации" -> 16711902;
            default -> 3694022;
         };
         stack.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(Optional.empty(), Optional.of(color), List.of(), Optional.empty()));
      } else if (potionContents != null) {
         stack.set(DataComponentTypes.POTION_CONTENTS, potionContents);
      }
      if (loreTexts != null) {
         stack.set(DataComponentTypes.LORE, new LoreComponent(loreTexts));
      }
      if (material == Items.TOTEM_OF_UNDYING) {
         stack.set(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
      }
      if (nbt != null) {
         NbtCompound copy = nbt.copy();
         if (material == Items.PLAYER_HEAD && copy.contains("SkullOwner", 10)) {
            NbtCompound skullOwner = copy.getCompound("SkullOwner");
            UUID uuid = skullOwner.getUuid("Id");
            GameProfile profile = new GameProfile(uuid, "");
            if (skullOwner.contains("Properties", 10)) {
               NbtCompound props = skullOwner.getCompound("Properties");
               if (props.contains("textures", 9)) {
                  NbtList textures = props.getList("textures", 10);
                  if (!textures.isEmpty()) {
                     NbtCompound tex = textures.getCompound(0);
                     profile.getProperties().put("textures", new Property("textures", tex.getString("Value")));
                  }
               }
            }
            stack.set(DataComponentTypes.PROFILE, new ProfileComponent(profile));
            copy.remove("SkullOwner");
         }
         if (!copy.isEmpty()) {
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(copy));
         }
      }
      return stack;
   }

   @Override public int getPrice() { return price; }
   @Override public boolean isEnabled() { return enabled; }
   @Override public void setEnabled(boolean enabled) { this.enabled = enabled; }
   @Override public AutoBuyItemSettings getSettings() { return settings; }
}
