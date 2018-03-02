package me.dags.noise.combiner;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Base extends Combiner {

    private final Module lower;
    private final Module upper;
    private final float min;
    private final float max;
    private final float maxValue;
    private final float falloff;

    public Base(Module lower, Module upper, float falloff) {
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
    public float getValue(float x, float y) {
        float value = upper.getValue(x, y);
        if (value < max) {
            float value1 = lower.getValue(x, y);

            if (falloff > 0) {
                float clamp = Math.min(max, Math.max(min, value));
                float alpha = (max - clamp) / falloff;
                return ((1 - alpha) * value) + (alpha * value1);
            }

            return value1;
        }
        return value;
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
    protected float minTotal(float result, Module next) {
        return 0;
    }

    @Override
    protected float maxTotal(float result, Module next) {
        return 0;
    }

    @Override
    protected float combine(float result, float value) {
        return 0;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("falloff", Util.round5(falloff));
        lower.toNode(node.node("lower"));
        upper.toNode(node.node("upper"));
    }
}
