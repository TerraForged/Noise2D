package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Min extends Combiner {

    public Min(Module... modules) {
        super(modules);
    }

    @Override
    public String getName() {
        return "min";
    }

    @Override
    protected float minTotal(float total, Module next) {
        return Math.min(total, next.minValue());
    }

    @Override
    protected float maxTotal(float total, Module next) {
        return minTotal(total, next);
    }

    @Override
    protected float combine(float total, float value) {
        return Math.min(total, value);
    }
}
