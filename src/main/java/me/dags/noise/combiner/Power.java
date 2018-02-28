package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Power extends Combiner {

    public Power(Module... modules) {
        super(modules);
    }

    @Override
    protected float minTotal(float result, Module next) {
        return (float) Math.pow(result, next.minValue());
    }

    @Override
    protected float maxTotal(float result, Module next) {
        return (float) Math.pow(result, next.maxValue());
    }

    @Override
    protected float combine(float result, float value) {
        return (float) Math.pow(result, value);
    }
}
