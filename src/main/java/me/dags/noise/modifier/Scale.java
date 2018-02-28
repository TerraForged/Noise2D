package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Scale extends Modifier {

    private final float scale;
    private final float min;
    private final float max;

    public Scale(Module source, float scale) {
        super(source);
        this.scale = scale;
        this.min = source.minValue() * scale;
        this.max = source.maxValue() * scale;
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
        return noiseValue * scale;
    }
}
