package com.isusdlc.systems.modules.modules.combat.AuraDev;

import lombok.Value;

@Value
public class SituationKey {
    String targetType;
    String distanceBucket;
    String movementState;
    String critState;
    String healthState;

    @Override
    public String toString() {
        return targetType + "_" + distanceBucket + "_" + movementState + "_" + critState + "_" + healthState;
    }
}
