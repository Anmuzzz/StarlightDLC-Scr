package com.isusdlc.mixin.minecraft.client.gui.screen;

import com.isusdlc.utility.interfaces.IMinecraft;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({InventoryScreen.class})
public abstract class InventoryScreenMixin extends RecipeBookScreen<PlayerScreenHandler> implements IMinecraft {
   public InventoryScreenMixin(PlayerScreenHandler handler, RecipeBookWidget<?> recipeBook, PlayerInventory inventory, Text title) {
      super(handler, recipeBook, inventory, title);
   }
}
