package me.dags.noise.combiner.selector;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.combiner.Combiner;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public abstract class Selector extends Combiner {

    private final Module selector;
    private final Interpolation interpolation;

    public Selector(Module control, Module[] sources, Interpolation interpolation) {
        super(sources);
        this.selector = control;
        this.interpolation = interpolation;
    }

    @Override
    public float getValue(float x, float y) {
        float select = selector.getValue(x, y);
        return selectValue(x, y, select);
    }

    @Override
    public List<?> getTags(float x, float y) {
        float select = selector.getValue(x, y);
        return selectTags(x, y, select);
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

    protected float blendValues(float lower, float upper, float alpha) {
        alpha = interpolation.apply(alpha);
        return NoiseUtil.interp(lower, upper, alpha);
    }

    protected abstract float selectValue(float x, float y, float selector);

    protected abstract List<?> selectTags(float x, float y, float selector);
}
