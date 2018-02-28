package me.dags.noise.combiner;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 *
 * Combiners output values that may be the result of one or more input Modules
 */
public abstract class Combiner implements Module {

    private final float min;
    private final float max;
    private final Module[] modules;

    public Combiner(Module... modules) {
        this.modules = modules;
        float min = 0F;
        float max = 0F;
        if (modules.length > 0) {
            min = modules[0].minValue();
            max = modules[0].maxValue();
            for (int i = 1; i < modules.length; i++) {
                Module next = modules[i];
                min = minTotal(min, next);
                max = maxTotal(max, next);
            }
        }
        this.min = min;
        this.max = max;
    }

    @Override
    public float minValue() {
        return min;
    }

    @Override
    public float maxValue() {
        return max;
    }

    @Override
    public float getValue(float x, float y) {
        float result = 0F;
        if (modules.length > 0) {
            result = modules[0].getValue(x, y);
            for (int i = 1; i < modules.length; i++) {
                Module module = modules[i];
                float value = module.getValue(x, y);
                result = combine(result, value);
            }
        }
        return result;
    }

    protected abstract float minTotal(float result, Module next);

    protected abstract float maxTotal(float result, Module next);

    protected abstract float combine(float result, float value);
}
