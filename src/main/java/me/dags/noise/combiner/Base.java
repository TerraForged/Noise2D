package me.dags.noise.combiner;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;
import me.dags.noise.combiner.selector.Selector;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

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
        super(upper, lower, upper, Cache.NONE, interpolation);
        this.lower = lower;
        this.upper = upper;
        this.min = lower.maxValue();
        this.max = min + falloff;
        this.falloff = falloff == 0 ? 1F : falloff;
        this.maxValue = Math.max(lower.maxValue(), upper.maxValue());
    }

    @Override
    public String getName() {
        return "floor";
    }

    @Override
    protected float selectValue(float x, float y, float upperValue) {
        if (upperValue < max) {
            float lowerValue = lower.getValue(x, y);

            if (falloff > 0) {
                float clamp = Math.min(max, Math.max(min, upperValue));
                float alpha = (max - clamp) / falloff;
                return selectTwo(lower, upper, lowerValue, upperValue, x, y, alpha);
            }

            select(lower, x, y);
            return lowerValue;
        }
        select(upper, x, y);
        return upperValue;
    }

    @Override
    public float minValue() {
        return lower.minValue();
    }

    @Override
    public float maxValue() {
        return maxValue;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("falloff", Util.round5(falloff));
    }
}
