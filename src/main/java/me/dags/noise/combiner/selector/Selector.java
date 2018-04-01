package me.dags.noise.combiner.selector;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;
import me.dags.noise.combiner.Combiner;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public abstract class Selector extends Combiner {

    private final Module selector;
    private final Interpolation interpolation;

    public Selector(Module control, Module[] sources, Cache cache, Interpolation interpolation) {
        super(cache, sources);
        this.selector = control;
        this.interpolation = interpolation;
    }

    @Override
    public float getValue(float x, float y) {
        if (!getCache().isCached(x, y)) {
            float select = selector.getValue(x, y);
            float value = selectValue(x, y, select);
            return getCache().cacheValue(x, y, value);
        }
        return getCache().getValue();
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        selector.toNode(node.node("selector"));
        node.set("interp", interpolation);
    }

    @Override
    protected float minTotal(float result, Module next) {
        return Math.min(result, next.minValue());
    }

    @Override
    protected float maxTotal(float result, Module next) {
        return Math.max(result, next.maxValue());
    }

    @Override
    protected float combine(float result, float value) {
        return 0;
    }

    protected float selectOne(Module source, int index, float x, float y) {
        float value = source.getValue(x, y);
        select(index, x, y);
        return value;
    }

    protected float selectTwo(Module source0, Module source1, int index0, int index1, float x, float y, float alpha) {
        alpha = interpolation.apply(alpha);
        float value = NoiseUtil.interp(source0.getValue(x, y), source1.getValue(x, y), alpha);
        select(index0, index1, x, y);
        return value;
    }

    protected float selectTwo(Module source0, Module source1, int index0, int index1, float value0, float value1, float x, float y, float alpha) {
        alpha = interpolation.apply(alpha);
        float value = NoiseUtil.interp(value0, value1, alpha);
        select(index0, index1, x, y);
        return value;
    }

    protected void select(int index, float x, float y) {}

    protected void select(int index0, int index1, float x, float y) {}

    protected abstract float selectValue(float x, float y, float selector);
}
