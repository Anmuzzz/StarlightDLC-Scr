package com.isusdlc.features.autobuy.util.krushprovider;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import com.isusdlc.features.autobuy.items.list.KrushItems;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Items;

public class KrushProvider {
   public static List<AutoBuyableItem> getKrush() {
      ArrayList var0 = new ArrayList();
      var0.add(new KrushItem("Шлем Крушителя", Items.NETHERITE_HELMET, KrushItems.getHelmet(), Defaultpricec.getPrice("Шлем крушителя")));
      var0.add(new KrushItem("Нагрудник Крушителя", Items.NETHERITE_CHESTPLATE, KrushItems.getChestplate(), Defaultpricec.getPrice("Нагрудник крушителя")));
      var0.add(new KrushItem("Поножи Крушителя", Items.NETHERITE_LEGGINGS, KrushItems.getLeggings(), Defaultpricec.getPrice("Поножи крушителя")));
      var0.add(new KrushItem("Ботинки Крушителя", Items.NETHERITE_BOOTS, KrushItems.getBoots(), Defaultpricec.getPrice("Ботинки крушителя")));
      var0.add(new KrushItem("Меч Крушителя", Items.NETHERITE_SWORD, KrushItems.getSword(), Defaultpricec.getPrice("Меч крушителя")));
      var0.add(new KrushItem("Кирка Крушителя", Items.NETHERITE_PICKAXE, KrushItems.getPickaxe(), Defaultpricec.getPrice("Кирка крушителя")));
      var0.add(new KrushItem("Арбалет Крушителя", Items.CROSSBOW, KrushItems.getCrossbow(), Defaultpricec.getPrice("Арбалет крушителя")));
      var0.add(new KrushItem("Трезубец Крушителя", Items.TRIDENT, KrushItems.getTrident(), Defaultpricec.getPrice("Трезубец крушителя")));
      var0.add(new KrushItem("Булава Крушителя", Items.MACE, KrushItems.getMace(), Defaultpricec.getPrice("Булава крушителя")));
      return var0;
   }
}
