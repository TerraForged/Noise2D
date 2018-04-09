package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 *
 * Modifiers alter the output of one or more Modules
 */
public abstract class Modifier implements Module {

    protected final Module source;

    public Modifier(Module source) {
        this.source = source;
    }

    @Override
    public float getValue(float x, float y) {
        float value = source.getValue(x, y);
        return modify(x, y, value);
    }

    @Override
    public float minValue() {
        return source.minValue();
    }

    @Override
    public float maxValue() {
        return source.maxValue();
    }

    public abstract float modify(float x, float y, float noiseValue);
}
