package com.isusdlc.hud.legacy;

public final class ColorUtil {
    public static int rgba(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private ColorUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
}
