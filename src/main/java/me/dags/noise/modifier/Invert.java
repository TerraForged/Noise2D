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
    public float modify(float x, float y, float noiseValue) {
        if (noiseValue > source.maxValue()) {
            return source.minValue();
        }
        if (noiseValue < source.minValue()) {
            return source.maxValue();
        }
        return source.maxValue() - noiseValue;
    }
}
