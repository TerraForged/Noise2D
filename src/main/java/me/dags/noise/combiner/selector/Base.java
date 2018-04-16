package me.dags.noise.combiner.selector;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Base extends Selector {

    private final Module lower;
    private final Module upper;
    protected final float min;
    protected final float max;
    protected final float maxValue;
    protected final float falloff;

    public Base(Module lower, Module upper, float falloff, Interpolation interpolation) {
        super(upper, new Module[]{lower, upper}, interpolation);
        this.lower = lower;
        this.upper = upper;
        this.min = lower.maxValue();
        this.max = lower.maxValue() + falloff;
        this.falloff = falloff;
        this.maxValue = Math.max(lower.maxValue(), upper.maxValue());
    }

    @Override
    protected float selectValue(float x, float y, float upperValue) {
        if (upperValue < max) {
            float lowerValue = lower.getValue(x, y);
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
    protected List<?> selectTags(float x, float y, float upperValue) {
        if (upperValue < max) {
            if (falloff > 0) {
                return getTags();
            }
            return lower.getTags(x, y);
        }
        return upper.getTags(x, y);
    }

    @Override
    public float minValue() {
        return lower.minValue();
    }

    @Override
    public float maxValue() {
        return maxValue;
    }
}
