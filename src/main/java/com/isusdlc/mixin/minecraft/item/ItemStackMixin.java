package com.isusdlc.mixin.minecraft.item;

import com.isusdlc.elegant;
import com.isusdlc.systems.event.impl.game.FinishEatEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ItemStack.class})
public abstract class ItemStackMixin {
   @Inject(
      method = {"finishUsing(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;"},
      at = {@At("TAIL")}
   )
   private void onFinishUsing(World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
      if (user instanceof PlayerEntity player) {
         elegant.getInstance().getEventManager().triggerEvent(new FinishEatEvent(player, (ItemStack)(Object)this));
      }
   }
}
