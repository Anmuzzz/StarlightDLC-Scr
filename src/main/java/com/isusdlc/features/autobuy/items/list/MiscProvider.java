package com.isusdlc.features.autobuy.items.list;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import com.isusdlc.features.autobuy.util.krushprovider.KrushItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MiscProvider {
   public static List<AutoBuyableItem> getMisc() {
      ArrayList var0 = new ArrayList();
      var0.add(new KrushItem("Золотое яблоко", Items.GOLDEN_APPLE, new ItemStack(Items.GOLDEN_APPLE), Defaultpricec.getPrice("Золотое яблоко")));
      var0.add(
         new KrushItem(
            "Зачарованное золотое яблоко",
            Items.ENCHANTED_GOLDEN_APPLE,
            new ItemStack(Items.ENCHANTED_GOLDEN_APPLE),
            Defaultpricec.getPrice("Зачарованное золотое яблоко")
         )
      );
      var0.add(new KrushItem("Порох", Items.GUNPOWDER, new ItemStack(Items.GUNPOWDER), Defaultpricec.getPrice("Порох")));
      var0.add(new KrushItem("Бирка", Items.NAME_TAG, new ItemStack(Items.NAME_TAG), Defaultpricec.getPrice("Бирка")));
      var0.add(new KrushItem("Трезубец", Items.TRIDENT, new ItemStack(Items.TRIDENT), Defaultpricec.getPrice("Трезубец")));
      var0.add(new KrushItem("Незеритовый слиток", Items.NETHERITE_INGOT, new ItemStack(Items.NETHERITE_INGOT), Defaultpricec.getPrice("Незеритовый слиток")));
      var0.add(new KrushItem("Алмаз", Items.DIAMOND, new ItemStack(Items.DIAMOND), Defaultpricec.getPrice("Алмаз")));
      var0.add(new KrushItem("Алмазный блок", Items.DIAMOND_BLOCK, new ItemStack(Items.DIAMOND_BLOCK), Defaultpricec.getPrice("Алмазный блок")));
      var0.add(new KrushItem("Золотой слиток", Items.GOLD_INGOT, new ItemStack(Items.GOLD_INGOT), Defaultpricec.getPrice("Золотой слиток")));
      var0.add(new KrushItem("Блок золота", Items.GOLD_BLOCK, new ItemStack(Items.GOLD_BLOCK), Defaultpricec.getPrice("Блок золота")));
      var0.add(new KrushItem("Маяк", Items.BEACON, new ItemStack(Items.BEACON), Defaultpricec.getPrice("Маяк")));
      var0.add(new KrushItem("Пузырёк опыта", Items.EXPERIENCE_BOTTLE, new ItemStack(Items.EXPERIENCE_BOTTLE), Defaultpricec.getPrice("Пузырёк опыта")));
      var0.add(new KrushItem("Звезда Незера", Items.NETHER_STAR, new ItemStack(Items.NETHER_STAR), Defaultpricec.getPrice("Звезда Незера")));
      var0.add(new KrushItem("Шалкеровый ящик", Items.SHULKER_BOX, new ItemStack(Items.SHULKER_BOX), Defaultpricec.getPrice("Шалкеровый ящик")));
      var0.add(new KrushItem("Железный слиток", Items.IRON_INGOT, new ItemStack(Items.IRON_INGOT), Defaultpricec.getPrice("Железный слиток")));
      var0.add(new KrushItem("Железный блок", Items.IRON_BLOCK, new ItemStack(Items.IRON_BLOCK), Defaultpricec.getPrice("Железный блок")));
      var0.add(new KrushItem("Незеритовый блок", Items.NETHERITE_BLOCK, new ItemStack(Items.NETHERITE_BLOCK), Defaultpricec.getPrice("Незеритовый блок")));
      var0.add(new KrushItem("Спавнер", Items.SPAWNER, new ItemStack(Items.SPAWNER), Defaultpricec.getPrice("Спавнер")));
      var0.add(new KrushItem("Элитры", Items.ELYTRA, new ItemStack(Items.ELYTRA), Defaultpricec.getPrice("Элитры")));
      var0.add(new KrushItem("Эндер жемчуг", Items.ENDER_PEARL, new ItemStack(Items.ENDER_PEARL), Defaultpricec.getPrice("Эндер жемчуг")));
      var0.add(new KrushItem("Обсидиан", Items.OBSIDIAN, new ItemStack(Items.OBSIDIAN), Defaultpricec.getPrice("Обсидиан")));
      var0.add(new KrushItem("Тотем бессмертия", Items.TOTEM_OF_UNDYING, new ItemStack(Items.TOTEM_OF_UNDYING), Defaultpricec.getPrice("Тотем бессмертия")));
      var0.add(new KrushItem("Палка ифрита", Items.BLAZE_ROD, new ItemStack(Items.BLAZE_ROD), Defaultpricec.getPrice("Палка ифрита")));
      var0.add(new KrushItem("Динамит", Items.TNT, new ItemStack(Items.TNT), Defaultpricec.getPrice("Динамит")));
      var0.add(
         new KrushItem(
            "Яйцо зомби-жителя", Items.ZOMBIE_VILLAGER_SPAWN_EGG, new ItemStack(Items.ZOMBIE_VILLAGER_SPAWN_EGG), Defaultpricec.getPrice("Яйцо зомби-жителя")
         )
      );
      var0.add(new KrushItem("Голова дракона", Items.DRAGON_HEAD, new ItemStack(Items.DRAGON_HEAD), Defaultpricec.getPrice("Голова дракона")));
      var0.add(new KrushItem("Алмазная руда", Items.DIAMOND_ORE, new ItemStack(Items.DIAMOND_ORE), Defaultpricec.getPrice("Алмазная руда")));
      var0.add(new KrushItem("Изумрудная руда", Items.EMERALD_ORE, new ItemStack(Items.EMERALD_ORE), Defaultpricec.getPrice("Изумрудная руда")));
      var0.add(new KrushItem("Яблоко", Items.APPLE, new ItemStack(Items.APPLE), Defaultpricec.getPrice("Яблоко")));
      var0.add(new KrushItem("Торт", Items.CAKE, new ItemStack(Items.CAKE), Defaultpricec.getPrice("Торт")));
      var0.add(new KrushItem("Фейерверк", Items.FIREWORK_ROCKET, new ItemStack(Items.FIREWORK_ROCKET), Defaultpricec.getPrice("Фейерверк")));
      var0.add(new KrushItem("Яйцо жителя", Items.VILLAGER_SPAWN_EGG, new ItemStack(Items.VILLAGER_SPAWN_EGG), Defaultpricec.getPrice("Яйцо жителя")));
      var0.add(new KrushItem("Яйцо вихря", Items.PHANTOM_SPAWN_EGG, new ItemStack(Items.PHANTOM_SPAWN_EGG), Defaultpricec.getPrice("Яйцо вихря")));
      var0.add(new KrushItem("Мешок", Items.BUNDLE, new ItemStack(Items.BUNDLE), Defaultpricec.getPrice("Мешок")));
      var0.add(new KrushItem("Булава", Items.MACE, new ItemStack(Items.MACE), Defaultpricec.getPrice("Булава")));
      return var0;
   }
}
