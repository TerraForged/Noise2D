package me.dags.noise.combiner;

import me.dags.noise.Module;
import me.dags.noise.cache.Cache;

/**
 * @author dags <dags@dags.me>
 */
public class Add extends Combiner {

    public Add(Module... modules) {
        this(Cache.NONE, modules);
    }

    public Add(Cache cache, Module... modules) {
        super(cache, modules);
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    protected float minTotal(float total, Module next) {
        return total + next.minValue();
    }

    @Override
    protected float maxTotal(float total, Module next) {
        return total + next.maxValue();
    }

    @Override
    protected float combine(float total, float value) {
        return total + value;
    }
}
