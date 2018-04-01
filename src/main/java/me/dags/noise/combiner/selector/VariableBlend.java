package me.dags.noise.combiner.selector;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

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
        super(control, new Module[]{source0, source1}, Cache.NONE, interpolation);
        this.source0 = source0;
        this.source1 = source1;
        this.midpoint = midpoint;
        this.maxBlend = maxBlend;
        this.minBlend = minBlend;
        this.variator = variator;
    }

    @Override
    public String getName() {
        return "variable_blend";
    }

    @Override
    protected float selectValue(float x, float y, float selector) {
        float radius = minBlend + variator.getValue(x, y) * maxBlend;

        float min = Math.max(0, midpoint - radius);
        if (selector < min) {
            return selectOne(source0, 0, x, y);
        }

        float max = Math.min(1, midpoint + radius);
        if (selector > max) {
            return selectOne(source1, 1, x, y);
        }

        float alpha = (selector - min) / (max - min);
        return selectTwo(source0, source1, 0, 1, x, y, alpha);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        variator.toNode(node.node("variator"));
        node.set("mid", Util.round5(midpoint));
        node.set("min", Util.round5(minBlend));
        node.set("max", Util.round5(minBlend));
    }
}
