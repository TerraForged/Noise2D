package me.dags.noise.combiner.selector;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

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
        super(selector, new Module[]{source0, source1}, interpolation);
        float mid = selector.minValue() + ((selector.maxValue() - selector.minValue()) * midPoint);
        this.source0 = source0;
        this.source1 = source1;
        this.midpoint = blendRange;
        this.blendLower = Math.max(selector.minValue(), mid - (blendRange / 2));
        this.blendUpper = Math.min(selector.maxValue(), mid + (blendRange / 2));
        this.blendRange = blendUpper - blendLower;
    }

    @Override
    public float selectValue(float x, float y, float select) {
        if (select < blendLower) {
            return source0.getValue(x, y);
        }
        if (select > blendUpper) {
            return source1.getValue(x, y);
        }
        float alpha = (select - blendLower) / blendRange;
        return blendValues(source0.getValue(x, y), source1.getValue(x, y), alpha);
    }

    @Override
    protected List<?> selectTags(float x, float y, float select) {
        if (select < blendLower) {
            return source0.getTags(x, y);
        }
        if (select > blendUpper) {
            return source1.getTags(x, y);
        }
        return getTags();
    }
}
