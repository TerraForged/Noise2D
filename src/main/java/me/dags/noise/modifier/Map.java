package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Map extends Modifier {

    private final float sourceRange;
    private final float outMin;
    private final float outMax;
    private final float outRange;

    public Map(Module source, float min1, float max1) {
        super(source);
        this.outMin = min1;
        this.outMax = max1;
        this.outRange = max1 - min1;
        this.sourceRange = source.maxValue() - source.minValue();
    }

    @Override
    public float minValue() {
        return outMin;
    }

    @Override
    public float maxValue() {
        return outMax;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        if (source.maxValue() != source.minValue()) {
            noiseValue = Math.min(source.maxValue(), Math.max(source.minValue(), noiseValue));
            noiseValue = (noiseValue - source.minValue()) / sourceRange;
            return (noiseValue - outMin) / outRange;
        } else {
            return Math.min(outMax, Math.max(outMin, noiseValue));
        }
    }
}
