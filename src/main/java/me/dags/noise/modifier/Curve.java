package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.func.CurveFunc;
import me.dags.noise.util.NoiseUtil;

public class Curve extends Modifier {

    private final CurveFunc func;

    public Curve(Module source, CurveFunc func) {
        super(source);
        this.func = func;
    }

    public Curve(Module source, float mid, float steepness) {
        this(source, value -> NoiseUtil.curve(value, mid, steepness));
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return func.apply(noiseValue);
    }
}
