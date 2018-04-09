package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Bias extends Modifier {

    private final float bias;

    public Bias(Module source, float bias) {
        super(source);
        this.bias = bias;
    }

    @Override
    public float minValue() {
        return super.minValue() + bias;
    }

    @Override
    public float maxValue() {
        return super.maxValue() + bias;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return noiseValue + bias;
    }
}
