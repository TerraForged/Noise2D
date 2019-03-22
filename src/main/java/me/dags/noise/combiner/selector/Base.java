package me.dags.noise.combiner.selector;

import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Base extends Selector {

    private final Module base;
    protected final float min;
    protected final float max;
    protected final float maxValue;
    protected final float falloff;

    public Base(Module base, Module source, float falloff, Interpolation interpolation) {
        super(source, new Module[]{base, source}, interpolation);
        this.base = base;
        this.min = base.maxValue();
        this.max = base.maxValue() + falloff;
        this.falloff = falloff;
        this.maxValue = Math.max(base.maxValue(), source.maxValue());
    }

    @Override
    protected float selectValue(float x, float y, float upperValue) {
        if (upperValue < max) {
            float lowerValue = base.getValue(x, y);
            if (falloff > 0) {
                float clamp = Math.max(min, upperValue);
                float alpha = (max - clamp) / falloff;
                return blendValues(upperValue, lowerValue, alpha);
            }
            return lowerValue;
        }
        return upperValue;
    }

    @Override
    public float minValue() {
        return base.minValue();
    }

    @Override
    public float maxValue() {
        return maxValue;
    }
}
