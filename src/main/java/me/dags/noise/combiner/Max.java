package me.dags.noise.combiner;

import me.dags.noise.Module;
import me.dags.noise.cache.Cache;

/**
 * @author dags <dags@dags.me>
 */
public class Max extends Combiner {

    public Max(Module... modules) {
        this(Cache.NONE, modules);
    }

    public Max(Cache cache, Module... modules) {
        super(cache, modules);
    }

    @Override
    public String getName() {
        return "max";
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
