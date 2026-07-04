package com.isusdlc.hud.legacy;

import com.isusdlc.utility.animation.base.Direction;

public class Animation {
    protected long duration = 300;
    protected float range = 1.0f;
    protected Direction direction = Direction.BACKWARDS;
    protected long startTime = System.currentTimeMillis();
    protected boolean finished = true;

    public Animation setMs(long ms) {
        this.duration = ms;
        return this;
    }

    public Animation setValue(float value) {
        this.range = value;
        this.startTime = System.currentTimeMillis();
        this.finished = false;
        return this;
    }

    public void setDirection(Direction dir) {
        if (this.direction != dir) {
            this.direction = dir;
            this.startTime = System.currentTimeMillis();
            this.finished = false;
        }
    }

    public Number getOutput() {
        if (finished) {
            return direction == Direction.FORWARDS ? range : 0.0f;
        }
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= duration) {
            finished = true;
            return direction == Direction.FORWARDS ? range : 0.0f;
        }
        float progress = (float) elapsed / (float) duration;
        float eased = ease(progress);
        float value = direction == Direction.FORWARDS ? eased * range : (1.0f - eased) * range;
        return value;
    }

    public boolean isFinished(Direction dir) {
        if (direction != dir) return false;
        if (finished) return true;
        float val = getOutput().floatValue();
        if (dir == Direction.FORWARDS) return val >= range;
        return val <= 0.0f;
    }

    protected float ease(float t) {
        return t;
    }
}
