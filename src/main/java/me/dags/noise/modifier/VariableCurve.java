package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class VariableCurve extends Modifier {

    private final Module midpoint;
    private final Module gradient;

    public VariableCurve(Module source, Module mid, Module gradient) {
        super(source);
        this.midpoint = mid;
        this.gradient = gradient;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float mid = midpoint.getValue(x, y);
        float curve = gradient.getValue(x, y);
        return NoiseUtil.curve(noiseValue, mid, curve);
    }
}
