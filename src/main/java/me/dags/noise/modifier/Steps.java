package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final float steps;
    private final int maxIndex;

    public Steps(Module source, int steps) {
        super(source.map(0, 1));
        if (steps < 1) {
            throw new IllegalArgumentException("steps cannot less than 1");
        }
        this.steps = steps;
        this.maxIndex = steps - 1;
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
        value = (int) (value * steps);
        value = Math.min(maxIndex, value);
        return value / steps;
    }
}
