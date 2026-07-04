package com.isusdlc.modules.rotation.impl;

import com.isusdlc.modules.rotation.MathAngle;
import com.isusdlc.modules.rotation.RotateConstructor;
import com.isusdlc.modules.rotation.Turns;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ThreadLocalRandom;

public class FTAngle extends RotateConstructor {

    private long smoothbackShakeStartMs = -1L;
    private float speed = 130.0F;
    private float smooth = 0.85F;

    public FTAngle() {
        super("FunTime");
    }

    public FTAngle withSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public FTAngle withSmooth(float smooth) {
        this.smooth = smooth;
        return this;
    }

    @Override
    public Turns limitAngleChange(Turns currentTurns, Turns targetTurns, Vec3d vec3d, Entity entity) {
        if (mc.player == null) {
            return currentTurns;
        }

        if (entity != null && !entity.isRemoved()) {
            this.smoothbackShakeStartMs = -1L;

            Turns deltaTurns = MathAngle.calculateDelta(currentTurns, targetTurns);
            float yawDelta = deltaTurns.getYaw();
            float pitchDelta = deltaTurns.getPitch();
            float totalDelta = (float) Math.hypot(yawDelta, pitchDelta);

            float yawLimit   = Math.abs(yawDelta   / totalDelta) * this.speed;
            float pitchLimit = Math.abs(pitchDelta / totalDelta) * this.speed;

            return new Turns(
                    MathHelper.lerp(this.smooth, currentTurns.getYaw(),
                            currentTurns.getYaw() + MathHelper.clamp(yawDelta, -yawLimit, yawLimit)),
                    MathHelper.lerp(this.smooth, currentTurns.getPitch(),
                            currentTurns.getPitch() + MathHelper.clamp(pitchDelta, -pitchLimit, pitchLimit))
            );
        }

        Turns playerTurns = new Turns(mc.player.getYaw(), mc.player.getPitch());
        Turns returnDelta = MathAngle.calculateDelta(currentTurns, playerTurns);

        float retYaw   = returnDelta.getYaw();
        float retPitch = returnDelta.getPitch();
        float retTotal = (float) Math.hypot(retYaw, retPitch);

        float shakeYaw   = (float) (randomBetween(18.0F, 28.0F)
                * Math.sin((double) System.currentTimeMillis() / 60.0));
        float shakePitch = (float) (randomBetween(6.0F, 16.0F)
                * Math.cos((double) System.currentTimeMillis() / 60.0));

        if (entity != null && !entity.isRemoved()) {
            this.smoothbackShakeStartMs = -1L;
        } else {
            if (this.smoothbackShakeStartMs < 0L) {
                this.smoothbackShakeStartMs = System.currentTimeMillis();
            }

            float fadeRatio = 1.0F - MathHelper.clamp(
                    (float)(System.currentTimeMillis() - this.smoothbackShakeStartMs) / 1000.0F,
                    0.0F, 1.0F
            );
            shakeYaw   *= fadeRatio;
            shakePitch *= fadeRatio;
        }

        float returnSpeed = this.speed * 0.35F;
        float yawLimit   = Math.abs(retYaw   / retTotal) * returnSpeed;
        float pitchLimit = Math.abs(retPitch / retTotal) * returnSpeed;

        return new Turns(
                MathHelper.lerp(this.smooth, currentTurns.getYaw(),
                        currentTurns.getYaw() + MathHelper.clamp(retYaw, -yawLimit, yawLimit) + shakeYaw),
                MathHelper.lerp(this.smooth, currentTurns.getPitch(),
                        currentTurns.getPitch() + MathHelper.clamp(retPitch, -pitchLimit, pitchLimit) + shakePitch)
        );
    }

    @Override
    public Vec3d randomValue() {
        return Vec3d.ZERO;
    }

    private float randomBetween(float min, float max) {
        if (min == max) return min;
        if (min > max) {
            float tmp = min;
            min = max;
            max = tmp;
        }
        return (float) ThreadLocalRandom.current().nextDouble(min, max);
    }
}
