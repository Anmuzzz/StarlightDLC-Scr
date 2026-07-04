package com.isusdlc.hud.legacy;

public class Decelerate extends Animation {
    @Override
    protected float ease(float t) {
        return 1.0f - (1.0f - t) * (1.0f - t);
    }
}
