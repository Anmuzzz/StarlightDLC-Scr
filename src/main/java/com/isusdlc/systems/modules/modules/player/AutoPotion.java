package com.isusdlc.systems.modules.modules.player;

import com.isusdlc.systems.event.EventListener;
import com.isusdlc.systems.event.impl.game.AttackEvent;
import com.isusdlc.systems.event.impl.player.ClientPlayerTickEvent;
import com.isusdlc.systems.modules.api.ModuleCategory;
import com.isusdlc.systems.modules.api.ModuleInfo;
import com.isusdlc.systems.modules.impl.BaseModule;
import com.isusdlc.systems.setting.settings.BooleanSetting;
import com.isusdlc.systems.setting.settings.SliderSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;

@ModuleInfo(
   name = "Auto Potion",
   category = ModuleCategory.COMBAT,
   desc = "Автоматически бросает зелья при отсутствии эффектов"
)
public class AutoPotion extends BaseModule {

   private final BooleanSetting strength = new BooleanSetting(this, "Strength").enable();
   private final BooleanSetting speed = new BooleanSetting(this, "Speed").enable();
   private final BooleanSetting fireResist = new BooleanSetting(this, "Fire Resistance").enable();
   private final BooleanSetting onlyInPvP = new BooleanSetting(this, "Only in PvP");
   private final SliderSetting health = new SliderSetting(this, "Health")
      .min(0.0F).max(20.0F).step(1.0F).currentValue(10.0F);

   private long lastThrow;
   private boolean inPvP;

   private final EventListener<AttackEvent> onAttack = event -> inPvP = true;

   private final EventListener<ClientPlayerTickEvent> onTick = event -> {
      if (mc.player == null || mc.world == null) return;
      if (mc.player.isUsingItem() && mc.player.isBlocking()) return;

      if (inPvP && mc.player.getRecentDamageSource() == null && mc.player.getAttacking() == null) {
         inPvP = false;
      }

      if (onlyInPvP.isEnabled() && !inPvP) return;

      if (System.currentTimeMillis() - lastThrow < 500) return;
      if (mc.player.getHealth() < health.getCurrentValue()) return;

      for (int i = 0; i < 9; i++) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (stack.getItem() != Items.SPLASH_POTION) continue;

         PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
         if (contents == null) continue;

         for (StatusEffectInstance effect : contents.getEffects()) {
            RegistryEntry<StatusEffect> type = effect.getEffectType();
            if (shouldThrow(type) && !mc.player.hasStatusEffect(type)) {
               int prevSlot = mc.player.getInventory().selectedSlot;
               mc.player.getInventory().selectedSlot = i;
               mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
               mc.player.swingHand(Hand.MAIN_HAND);
               mc.player.getInventory().selectedSlot = prevSlot;
               lastThrow = System.currentTimeMillis();
               return;
            }
         }
      }
   };

   private boolean shouldThrow(RegistryEntry<StatusEffect> type) {
      if (type == StatusEffects.STRENGTH && strength.isEnabled()) return true;
      if (type == StatusEffects.SPEED && speed.isEnabled()) return true;
      return type == StatusEffects.FIRE_RESISTANCE && fireResist.isEnabled();
   }
}
