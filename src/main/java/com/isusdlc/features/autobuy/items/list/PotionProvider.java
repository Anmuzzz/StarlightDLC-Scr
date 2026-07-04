package com.isusdlc.features.autobuy.items.list;

import com.isusdlc.features.autobuy.items.AutoBuyableItem;
import com.isusdlc.features.autobuy.items.customitem.CustomItem;
import com.isusdlc.features.autobuy.items.defaultsetpricec.Defaultpricec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

public class PotionProvider {
   public static List<AutoBuyableItem> getPotions() {
      ArrayList var0 = new ArrayList();
      var0.add(
         new CustomItem(
            "[★] Святая вода",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Святая вода"),
            createPotionContents(
               16777215,
               List.of(
                  new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 2),
                  new StatusEffectInstance(StatusEffects.INVISIBILITY, 12000, 1),
                  new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1)
               )
            ),
            null
         )
      );
      var0.add(
         new CustomItem(
            "[★] Зелье Гнева",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Зелье Гнева"),
            createPotionContents(
               10040115, List.of(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 4), new StatusEffectInstance(StatusEffects.SLOWNESS, 600, 3))
            ),
            null
         )
      );
      var0.add(
         new CustomItem(
            "[★] Зелье Палладина",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Зелье Палладина"),
            createPotionContents(
               65535,
               List.of(
                  new StatusEffectInstance(StatusEffects.RESISTANCE, 12000, 0),
                  new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 12000, 0),
                  new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 1200, 2),
                  new StatusEffectInstance(StatusEffects.INVISIBILITY, 18000, 2)
               )
            ),
            null
         )
      );
      var0.add(
         new CustomItem(
            "[★] Зелье Ассасина",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Зелье Ассасина"),
            createPotionContents(
               3355443,
               List.of(
                  new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 3),
                  new StatusEffectInstance(StatusEffects.SPEED, 6000, 2),
                  new StatusEffectInstance(StatusEffects.HASTE, 1200, 0),
                  new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1)
               )
            ),
            null
         )
      );
      var0.add(
         new CustomItem(
            "[★] Зелье Радиации",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Зелье Радиации"),
            createPotionContents(
               4737096,
               List.of(
                  new StatusEffectInstance(StatusEffects.WEAKNESS, 1800, 1),
                  new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 200, 1),
                  new StatusEffectInstance(StatusEffects.WITHER, 1800, 2),
                  new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 0)
               )
            ),
            null
         )
      );
      var0.add(
         new CustomItem(
            "[★] Снотворное",
            null,
            Items.SPLASH_POTION,
            Defaultpricec.getPrice("Снотворное"),
            createPotionContents(
               3329330,
               List.of(
                  new StatusEffectInstance(StatusEffects.POISON, 400, 1),
                  new StatusEffectInstance(StatusEffects.WITHER, 400, 1),
                  new StatusEffectInstance(StatusEffects.SLOWNESS, 400, 2),
                  new StatusEffectInstance(StatusEffects.HUNGER, 400, 4),
                  new StatusEffectInstance(StatusEffects.GLOWING, 400, 0)
               )
            ),
            null
         )
      );
      return var0;
   }

   private static PotionContentsComponent createPotionContents(int var0, List<StatusEffectInstance> var1) {
      ArrayList var2 = new ArrayList();

      for (StatusEffectInstance var4 : var1) {
         var2.add(var4.getEffectType());
      }

      return new PotionContentsComponent(Optional.empty(), Optional.of(var0), var1, Optional.empty());
   }
}
