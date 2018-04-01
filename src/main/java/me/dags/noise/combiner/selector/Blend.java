package me.dags.noise.combiner.selector;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Blend extends Selector {

    private final Module source0;
    private final Module source1;
    private final float midpoint;
    private final float blendLower;
    private final float blendUpper;
    private final float blendRange;

    public Blend(Module selector, Module source0, Module source1, float midPoint, float blendRange, Interpolation interpolation) {
        super(selector, new Module[]{source0, source1}, Cache.NONE, interpolation);
        float mid = selector.minValue() + ((selector.maxValue() - selector.minValue()) * midPoint);
        this.source0 = source0;
        this.source1 = source1;
        this.midpoint = blendRange;
        this.blendLower = Math.max(selector.minValue(), mid - (blendRange / 2));
        this.blendUpper = Math.min(selector.maxValue(), mid + (blendRange / 2));
        this.blendRange = blendUpper - blendLower;
    }

    @Override
    public String getName() {
        return "blend";
    }

    @Override
    public float selectValue(float x, float y, float select) {
        if (select < blendLower) {
            return selectOne(source0, 0, x, y);
        }
        if (select > blendUpper) {
            return selectOne(source1, 1, x, y);
        }
        float alpha = (select - blendLower) / blendRange;
        return selectTwo(source0, source1, 0, 1, x, y, alpha);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("blend", Util.round5(blendRange));
        node.set("center", Util.round5(midpoint));
    }
}
