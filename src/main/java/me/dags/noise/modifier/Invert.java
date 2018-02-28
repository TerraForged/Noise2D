package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Invert extends Modifier {

    public Invert(Module source) {
        super(source);
    }

    @Override
    public float minValue() {
        return -super.maxValue();
    }

    @Override
    public float maxValue() {
        return -super.minValue();
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return -noiseValue;
    }
}
