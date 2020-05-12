package me.dags.noise.func;

import me.dags.noise.util.NoiseUtil;

public class SCurve implements CurveFunc {

    private final float lower;
    private final float upper;

    public SCurve(float lower, float upper) {
        this.lower = lower;
        this.upper = upper < 0 ? Math.max(-lower, upper) : upper;
    }

    @Override
    public float apply(float value) {
        return NoiseUtil.pow(value, lower + (upper * value));
    }
}
