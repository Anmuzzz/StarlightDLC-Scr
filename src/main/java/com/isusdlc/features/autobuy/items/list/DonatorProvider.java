package com.isusdlc.features.autobuy.items.list;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.customitem.CustomItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class DonatorProvider {
   public static List<AutoBuyableItem> getDonator() {
      ArrayList var0 = new ArrayList();
      List var1 = List.of(
         Text.literal("Каст: Световая вспышка"),
         Text.literal("Радиус: 10 блоков"),
         Text.literal("Эффекты для противников:"),
         Text.literal(" • Свечение (00:30)"),
         Text.literal(" • Слепота (00:01)"),
         Text.literal("Чем ближе цель, тем дольше длительность эффектов")
      );
      var0.add(new CustomItem("[★] Явная пыль", null, Items.SUGAR, Defaultpricec.getPrice("Явная пыль"), null, var1));
      List var2 = List.of(Text.literal("Чем ближе цель, тем дольше длительность эффектов"));
      var0.add(new CustomItem("[★] Дезориентация", null, Items.ENDER_EYE, Defaultpricec.getPrice("Дезориентация"), null, var2));
      List var3 = List.of(Text.literal("Каст: Нерушимая клетка"), Text.literal("Длительность: 15 секунд"), Text.literal("Используйте скины: /tskins"));
      var0.add(new CustomItem("[★] Трапка", null, Items.NETHERITE_SCRAP, Defaultpricec.getPrice("Трапка"), null, var3));
      List var4 = List.of(Text.literal("Этой отмычкой можно"), Text.literal("Открыть хранилище"), Text.literal("С Сферами"));
      var0.add(new CustomItem("Отмычка к Сферам", null, Items.TRIPWIRE_HOOK, Defaultpricec.getPrice("Отмычка к Сферам"), null, var4));
      List var5 = List.of(
         Text.literal("Каст: Нерушимая стена"),
         Text.literal("Длительность:"),
         Text.literal("Вертикальный: 20 секунд"),
         Text.literal("Горизонтальный: 60 секунд")
      );
      var0.add(new CustomItem("[★] Пласт", null, Items.DRIED_KELP, Defaultpricec.getPrice("Пласт"), null, var5));
      List var6 = List.of(Text.literal("Содержит 15 ур опыта"));
      var0.add(new CustomItem("Пузырек опыта [15 ур]", null, Items.EXPERIENCE_BOTTLE, Defaultpricec.getPrice("Пузырек опыта [15 ур]"), null, var6));
      List var7 = List.of(Text.literal("Содержит: 30 Ур. опыта"));
      var0.add(new CustomItem("Пузырёк опыта [30 Ур.]", null, Items.EXPERIENCE_BOTTLE, Defaultpricec.getPrice("Пузырёк опыта [30 Ур.]"), null, var7));
      List var8 = List.of(Text.literal("Содержит 50 ур опыта"));
      var0.add(new CustomItem("Пузырек опыта [50 ур]", null, Items.EXPERIENCE_BOTTLE, Defaultpricec.getPrice("Пузырек опыта [50 ур]"), null, var8));
      List var9 = List.of(Text.literal("Этот динамит взрывается"), Text.literal("в 10 раз сильнее обычного"));
      var0.add(new CustomItem("[★] TNT - TIER WHITE", "Таер вайт", null, Items.TNT, Defaultpricec.getPrice("TNT - TIER WHITE"), null, var9));
      List var10 = List.of(Text.literal("Этот динамит взрывается"), Text.literal("в 10 раз сильнее обычного"), Text.literal("и способен взорвать обсидиан"));
      var0.add(new CustomItem("[★] TNT - TIER BLACK", "Таер блэк", null, Items.TNT, Defaultpricec.getPrice("TNT - TIER BLACK"), null, var10));
      List var11 = List.of(Text.literal("Уровень лута: Случайный"));
      var0.add(new CustomItem("Сигнальный огонь [Случайный]", null, Items.CAMPFIRE, Defaultpricec.getPrice("Сигнальный огонь [Случайный]"), null, var11));
      List var12 = List.of(Text.literal("Уровень лута: Обычный"));
      var0.add(new CustomItem("Сигнальный огонь [Обычный]", null, Items.CAMPFIRE, Defaultpricec.getPrice("Сигнальный огонь [Обычный]"), null, var12));
      List var13 = List.of(Text.literal("Уровень лута: Богатый"));
      var0.add(new CustomItem("Сигнальный огонь [Богатый]", null, Items.CAMPFIRE, Defaultpricec.getPrice("Сигнальный огонь [Богатый]"), null, var13));
      List var14 = List.of(Text.literal("Уровень лута: Легендарный"));
      var0.add(new CustomItem("Сигнальный огонь [Легендарный]", null, Items.CAMPFIRE, Defaultpricec.getPrice("Сигнальный огонь [Легендарный]"), null, var14));
      List var15 = List.of(Text.literal("● Каст: Нанесение урона"), Text.literal("● Радиус: 1,5 блока"));
      var0.add(new CustomItem("[★] Блок дамагер", null, Items.JIGSAW, Defaultpricec.getPrice("Блок дамагер"), null, var15));
      List var16 = List.of(
         Text.literal("Прогружает чанк, в котором"),
         Text.literal("находится этот прогрузчик."),
         Text.literal("Нажмите на него, чтобы на"),
         Text.literal("30 секунд увидеть границы")
      );
      var0.add(
         new CustomItem("Прогрузчик чанков [1x1]", "Блок чанкер", null, Items.STRUCTURE_BLOCK, Defaultpricec.getPrice("Прогрузчик чанков [1x1]"), null, var16)
      );
      List var17 = List.of(
         Text.literal("Прогружает чанк, в котором"),
         Text.literal("находится этот прогрузчик."),
         Text.literal("Нажмите на него, чтобы на"),
         Text.literal("30 секунд увидеть границы")
      );
      var0.add(
         new CustomItem("Прогрузчик чанков [3x3]", "Блок чанкер", null, Items.STRUCTURE_BLOCK, Defaultpricec.getPrice("Прогрузчик чанков [3x3]"), null, var17)
      );
      List var18 = List.of(
         Text.literal("Прогружает чанк, в котором"),
         Text.literal("находится этот прогрузчик."),
         Text.literal("Нажмите на него, чтобы на"),
         Text.literal("30 секунд увидеть границы")
      );
      var0.add(
         new CustomItem("Прогрузчик чанков [5x5]", "Блок чанкер", null, Items.STRUCTURE_BLOCK, Defaultpricec.getPrice("Прогрузчик чанков [5x5]"), null, var18)
      );
      List var19 = List.of(Text.literal("Маяк установит временный"), Text.literal("ивент, раздающий Монеты"), Text.literal("игрокам поблизости."));
      var0.add(new CustomItem("Загадочный маяк", null, Items.BEACON, Defaultpricec.getPrice("Загадочный маяк"), null, var19));
      List var20 = List.of(Text.literal("Обменяй души на ценные"), Text.literal("ресурсы у Собирателя душ"), Text.literal("/warp soulcollector"));
      var0.add(new CustomItem("[★] Проклятая душа", null, Items.SOUL_LANTERN, Defaultpricec.getPrice("Проклятая душа"), null, var20));
      List var21 = List.of(
         Text.literal("Используя этот предмет"),
         Text.literal("Вы его расходуете"),
         Text.literal("и получаете Драконий скин взамен"),
         Text.literal("[ПКМ] чтобы использовать x1 скин"),
         Text.literal("[SHIFT+ПКМ] чтобы использовать все скины"),
         Text.literal("Предмет нужно держать в руке")
      );
      var0.add(new CustomItem("[★] Драконий скин", null, Items.PAPER, Defaultpricec.getPrice("Драконий скин"), null, var21));
      List var22 = List.of(
         Text.literal("● Каст: Огненная волна"),
         Text.literal("● Радиус: 10 блоков"),
         Text.literal(""),
         Text.literal("● Эффекты для противников:"),
         Text.literal(" - Поджог (00:03)"),
         Text.literal(""),
         Text.literal("Чем ближе цель, тем дольше"),
         Text.literal("длительность эффектов")
      );
      var0.add(new CustomItem("[★] Огненный смерч", null, Items.FIRE_CHARGE, Defaultpricec.getPrice("Огненный смерч"), null, var22));
      List var23 = List.of(
         Text.literal("● Каст: Ледяная сфера"),
         Text.literal("● Радиус: 7 блоков"),
         Text.literal(""),
         Text.literal("● Эффекты для противников:"),
         Text.literal(" - Заморозка (00:01)"),
         Text.literal(" - Слабость (03:00)")
      );
      var0.add(new CustomItem("[★] Снежок заморозка", null, Items.SNOWBALL, Defaultpricec.getPrice("Снежок заморозка"), null, var23));
      List var24 = List.of(
         Text.literal("● Каст: Божественная аура"),
         Text.literal("● Радиус: 2 блока"),
         Text.literal(""),
         Text.literal("● Эффекты для союзников:"),
         Text.literal(" - Снятие всех эффектов"),
         Text.literal(" - Невидимость (04:00)"),
         Text.literal(" - Сила II (03:00)"),
         Text.literal(" - Скорость II (03:00)")
      );
      var0.add(new CustomItem("[★] Божья аура", null, Items.PHANTOM_MEMBRANE, Defaultpricec.getPrice("Божья аура"), null, var24));
      List var25 = List.of(Text.literal("Это валюта для покупки"), Text.literal("отмычек к тайникам"), Text.literal("у Знахаря (/warp stash)"));
      var0.add(new CustomItem("[★] Серебро", null, Items.IRON_NUGGET, Defaultpricec.getPrice("Серебро"), null, var25));
      List var26 = List.of(Text.literal("Божье касание"), Text.literal(""), Text.literal("Может добыть спавнер,"), Text.literal("но только один раз"));
      var0.add(new CustomItem("[★] Божье касание", null, Items.GOLDEN_PICKAXE, Defaultpricec.getPrice("Божье касание"), null, var26));
      List var27 = List.of(Text.literal("Вскапывает территорию"), Text.literal("размером 9x9x5 блоков"));
      var0.add(new CustomItem("[★] Кирка мега-бульдозер", null, Items.NETHERITE_PICKAXE, Defaultpricec.getPrice("Кирка мега-бульдозер"), null, var27));
      return var0;
   }
}
