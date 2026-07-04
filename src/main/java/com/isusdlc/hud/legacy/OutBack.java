package com.isusdlc.hud.legacy;

public class OutBack extends Animation {
    @Override
    protected float ease(float t) {
        float overshoot = 1.70158f;
        float x = t - 1.0f;
        return x * x * ((overshoot + 1.0f) * x + overshoot) + 1.0f;
    }
}
