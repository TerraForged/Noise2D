package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.Source;

/**
 * @author dags <dags@dags.me>
 */
public class Clamp extends Modifier {

    private final Module min;
    private final Module max;

    public Clamp(Module source, float min, float max) {
        this(source, Source.constant(min), Source.constant(max));
    }

    public Clamp(Module source, Module min, Module max) {
        super(source);
        this.min = min;
        this.max = max;
    }

    @Override
    public float minValue() {
        return min.minValue();
    }

    @Override
    public float maxValue() {
        return max.maxValue();
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float min = this.min.getValue(x, y);
        float max = this.max.getValue(x, y);
        return Math.min(max, Math.max(min, noiseValue));
    }
}
