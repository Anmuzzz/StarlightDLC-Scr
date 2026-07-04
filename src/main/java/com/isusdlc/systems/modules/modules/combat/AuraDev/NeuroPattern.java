package com.isusdlc.systems.modules.modules.combat.AuraDev;

import lombok.Data;
import java.io.Serializable;

@Data
public class NeuroPattern implements Serializable {
    private static final long serialVersionUID = 1L;
    private final float yaw;
    private final float pitch;
    private final double distance;
    private final long timestamp;
    private final boolean isCritical;
    private final double targetSpeed;
    private final String targetType;

    public NeuroPattern(float yaw, float pitch, double distance, boolean isCritical,
                        double targetSpeed, String targetType) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.distance = distance;
        this.timestamp = System.currentTimeMillis();
        this.isCritical = isCritical;
        this.targetSpeed = targetSpeed;
        this.targetType = targetType;
    }
}
