package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final Module steps;

    public Steps(Module source, Module steps) {
        super(source.map(0, 1));
        if (steps.minValue() < 1) {
            throw new IllegalArgumentException("steps cannot less than 1");
        }
        this.steps = steps;
    }

    @Override
    public float minValue() {
        return 0F;
    }

    public float maxValue() {
        return 1F;
    }

    @Override
    public float modify(float x, float y, float value) {
        float steps = this.steps.getValue(x, y);
        value = NoiseUtil.round(value * steps);
        return value / steps;
    }
}
