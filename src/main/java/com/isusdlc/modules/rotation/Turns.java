package com.isusdlc.modules.rotation;

public class Turns {
    private final float yaw;
    private final float pitch;

    public Turns(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }
}
