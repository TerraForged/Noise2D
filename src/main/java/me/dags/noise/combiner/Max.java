package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Max extends Combiner {

    public Max(Module... modules) {
        super(modules);
    }

    @Override
    protected float minTotal(float total, Module next) {
        return maxTotal(total, next);
    }

    @Override
    protected float maxTotal(float total, Module next) {
        return Math.max(total, next.maxValue());
    }

    @Override
    protected float combine(float total, float value) {
        return Math.max(total, value);
    }
}
