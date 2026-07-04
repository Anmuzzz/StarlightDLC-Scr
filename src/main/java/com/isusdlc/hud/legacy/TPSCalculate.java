package com.isusdlc.hud.legacy;

public final class TPSCalculate {
    private static TPSCalculate instance;
    private float tps = 20.0f;

    public static TPSCalculate getInstance() {
        if (instance == null) instance = new TPSCalculate();
        return instance;
    }

    public float getTPS() {
        return tps;
    }

    public void setTPS(float tps) {
        this.tps = tps;
    }

    private TPSCalculate() {
    }
}
