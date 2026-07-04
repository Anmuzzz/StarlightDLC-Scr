package com.isusdlc.mixin.accessors;

import java.util.List;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.Selectable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Screen.class})
public interface ScreenAccessor {
   @Accessor("children")
   List<Element> getChildren();

   @Invoker("addDrawableChild")
   <T extends Element & Drawable & Selectable> T invokeAddDrawableChild(T var1);
}
