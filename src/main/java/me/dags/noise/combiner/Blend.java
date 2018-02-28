package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Blend extends Combiner {

    private final Module control;
    private final Module source0;
    private final Module source1;

    public Blend(Module control, Module source0, Module source1) {
        super(source0, source1);
        this.source0 = source0;
        this.source1 = source1;
        this.control = control.norm();
    }

    @Override
    public float getValue(float x, float y) {
        float alpha = control.getValue(x, y);
        float v0 = source0.getValue(x, y);
        float v1 = source1.getValue(x, y);
        return (v0 * (1 - alpha)) + (v1 * alpha);
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
        return 0F;
    }
}
