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

    public Selector(Module control, Module source0, Module source1, Cache cache, Interpolation interpolation) {
        super(cache, source0, source1);
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

    protected float selectOne(Module source, float x, float y) {
        float value = source.getValue(x, y);
        select(source, x, y);
        return value;
    }

    protected float selectTwo(Module source0, Module source1, float x, float y, float alpha) {
        alpha = interpolation.apply(alpha);
        float value = NoiseUtil.interp(source0.getValue(x, y), source1.getValue(x, y), alpha);
        select(source0, source1, x, y);
        return value;
    }

    protected float selectTwo(Module source0, Module source1, float value0, float value1, float x, float y, float alpha) {
        alpha = interpolation.apply(alpha);
        float value = NoiseUtil.interp(value0, value1, alpha);
        select(source0, source1, x, y);
        return value;
    }

    protected void select(Module module, float x, float y) {}

    protected void select(Module module0, Module module1, float x, float y) {}

    protected abstract float selectValue(float x, float y, float selector);
}
