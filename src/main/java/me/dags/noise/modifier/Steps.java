package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.source.fast.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final float steps;

    public Steps(Module source, int steps) {
        super(source.norm());
        if (steps == 0) {
            throw new IllegalArgumentException("steps cannot be 0");
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
    public float modify(float x, float y, float noiseValue) {
        int round = Util.FastRound(noiseValue * steps);
        return round / steps;
    }
}
