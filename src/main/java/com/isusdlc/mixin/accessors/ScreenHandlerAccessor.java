package com.isusdlc.mixin.accessors;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ScreenHandler.class})
public interface ScreenHandlerAccessor {
   @Accessor("trackedStacks")
   DefaultedList<ItemStack> getTrackedStacks();
}
