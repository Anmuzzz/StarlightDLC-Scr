package com.isusdlc.modules.rotation;

public class MathAngle {
    public static Turns calculateDelta(Turns current, Turns target) {
        float yawDelta = target.getYaw() - current.getYaw();
        float pitchDelta = target.getPitch() - current.getPitch();
        return new Turns(yawDelta, pitchDelta);
    }
}
