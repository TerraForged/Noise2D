package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Sub extends Combiner {

    public Sub(Module... modules) {
        super(modules);
    }

    @Override
    protected float minTotal(float total, Module next) {
        return total - next.maxValue();
    }

    @Override
    protected float maxTotal(float total, Module next) {
        return total - next.minValue();
    }

    @Override
    protected float combine(float total, float value) {
        return total - value;
    }
}
