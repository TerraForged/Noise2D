package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Cache implements Module {

    private final Module source;
    private float x = 0;
    private float y = 0;
    private float value = 0;

    public Cache(Module source) {
        this.source = source;
        getValue(1, 1);
    }

    @Override
    public float minValue() {
        return source.minValue();
    }

    @Override
    public float maxValue() {
        return source.maxValue();
    }

    @Override
    public float getValue(float x, float y) {
        if (x != this.x || y != this.y) {
            this.x = x;
            this.y = y;
            this.value = source.getValue(x, y);
        }
        return value;
    }
}
