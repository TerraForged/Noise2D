package me.dags.noise.source;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Constant implements Module {

    private final float value;

    public Constant(float value) {
        this.value = value;
    }

    @Override
    public float getValue(float x, float y) {
        return value;
    }

    @Override
    public float minValue() {
        return value;
    }

    @Override
    public float maxValue() {
        return value;
    }
}
