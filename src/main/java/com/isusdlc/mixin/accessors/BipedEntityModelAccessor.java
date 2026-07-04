package com.isusdlc.mixin.accessors;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BipedEntityModel.class})
public interface BipedEntityModelAccessor {
   @Accessor("head")
   ModelPart elegant$getHead();

   @Accessor("hat")
   ModelPart elegant$getHat();
}
