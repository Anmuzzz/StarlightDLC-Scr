package com.isusdlc.mixin.minecraft.render.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({EntityRenderer.class})
public class EntityRendererMixin<T extends Entity> {
}
