package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Clamp extends Modifier {

    private final float min;
    private final float max;

    public Clamp(Module source, float min, float max) {
        super(source);
        this.min = min;
        this.max = max;
    }

    @Override
    public float minValue() {
        return min;
    }

    @Override
    public float maxValue() {
        return max;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return Math.min(max, Math.max(min, noiseValue));
    }

    @Override
    public String toString() {
        return "Clamp{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
