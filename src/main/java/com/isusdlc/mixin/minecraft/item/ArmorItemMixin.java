package com.isusdlc.mixin.minecraft.item;

import com.isusdlc.utility.mixins.ArmorItemAddition;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.Item.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ArmorItem.class})
public abstract class ArmorItemMixin implements ArmorItemAddition {
   @Unique
   private EquipmentType elegant$type;
   @Unique
   private ArmorMaterial elegant$material;

   @Inject(
      method = {"<init>(Lnet/minecraft/item/equipment/ArmorMaterial;Lnet/minecraft/item/equipment/EquipmentType;Lnet/minecraft/item/Item$Settings;)V"},
      at = {@At("TAIL")}
   )
   public void saveArgs(ArmorMaterial material, EquipmentType type, Settings settings, CallbackInfo ci) {
      this.elegant$type = type;
      this.elegant$material = material;
   }

   @Override
   public ArmorMaterial elegant$getMaterial() {
      return this.elegant$material;
   }

   @Override
   public EquipmentType elegant$getType() {
      return this.elegant$type;
   }
}
