package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Map extends Modifier {

    private final Module min;
    private final Module max;
    private final float sourceRange;

    public Map(Module source, Module min, Module max) {
        super(source);
        this.min = min;
        this.max = max;
        this.sourceRange = source.maxValue() - source.minValue();
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
        float range = max - min;

        if (source.maxValue() != source.minValue()) {
            noiseValue = Math.min(source.maxValue(), Math.max(source.minValue(), noiseValue));
            noiseValue = (noiseValue - source.minValue()) / sourceRange;
            return (noiseValue - min) / range;
        } else {
            return Math.min(max, Math.max(min, noiseValue));
        }
    }
}
