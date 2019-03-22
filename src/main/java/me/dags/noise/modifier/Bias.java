package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.Source;

/**
 * @author dags <dags@dags.me>
 */
public class Bias extends Modifier {

    private final Module bias;

    public Bias(Module source, float bias) {
        this(source, Source.constant(bias));
    }

    public Bias(Module source, Module bias) {
        super(source);
        this.bias = bias;
    }

    @Override
    public float minValue() {
        return super.minValue() + bias.minValue();
    }

    @Override
    public float maxValue() {
        return super.maxValue() + bias.maxValue();
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return noiseValue + bias.getValue(x, y);
    }
}
