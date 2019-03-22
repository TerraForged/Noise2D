package me.dags.noise.combiner.selector;

import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class VariableBlend extends Selector {

    private final Module source0;
    private final Module source1;
    private final Module variator;
    private final float midpoint;
    private final float maxBlend;
    private final float minBlend;

    public VariableBlend(Module control, Module variator, Module source0, Module source1, float midpoint, float minBlend, float maxBlend, Interpolation interpolation) {
        super(control, new Module[]{source0, source1}, interpolation);
        this.source0 = source0;
        this.source1 = source1;
        this.midpoint = midpoint;
        this.maxBlend = maxBlend;
        this.minBlend = minBlend;
        this.variator = variator;
    }

    @Override
    protected float selectValue(float x, float y, float selector) {
        float radius = minBlend + variator.getValue(x, y) * maxBlend;

        float min = Math.max(0, midpoint - radius);
        if (selector < min) {
            return source0.getValue(x, y);
        }

        float max = Math.min(1, midpoint + radius);
        if (selector > max) {
            return source1.getValue(x, y);
        }

        float alpha = (selector - min) / (max - min);
        return blendValues(source0.getValue(x, y), source1.getValue(x, y), alpha);
    }
}
