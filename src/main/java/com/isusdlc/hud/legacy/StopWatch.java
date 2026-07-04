package com.isusdlc.hud.legacy;

public class StopWatch {
    private long ms = 0;

    public void reset() {
        ms = System.currentTimeMillis();
    }

    public boolean finished(long delay) {
        return System.currentTimeMillis() - ms >= delay;
    }

    public long elapsedTime() {
        return System.currentTimeMillis() - ms;
    }
}
