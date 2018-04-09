package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Multiply extends Combiner {

    public Multiply(Module... modules) {
        super(modules);
    }

    @Override
    protected float minTotal(float total, Module next) {
        return total * next.minValue();
    }

    @Override
    protected float maxTotal(float total, Module next) {
        return total * next.maxValue();
    }

    @Override
    protected float combine(float total, float value) {
        return total * value;
    }
}
