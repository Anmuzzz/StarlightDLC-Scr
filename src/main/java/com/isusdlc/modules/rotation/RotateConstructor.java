package com.isusdlc.modules.rotation;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public abstract class RotateConstructor {
    private final String name;
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    public RotateConstructor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Turns limitAngleChange(Turns currentTurns, Turns targetTurns, Vec3d vec3d, Entity entity);
    public abstract Vec3d randomValue();
}
