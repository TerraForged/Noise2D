package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Warp extends Modifier {

    private final Module warpX;
    private final Module warpY;
    private final Module power;

    public Warp(Module source, Module x, Module y, Module power) {
        super(source);
        this.power = power;
        this.warpX = map(x);
        this.warpY = map(y);
    }

    @Override
    public float getValue(float x, float y) {
        float distance = power.getValue(x, y);
        float offsetX = warpX.getValue(x, y) * distance;
        float offsetY = warpY.getValue(x, y) * distance;
        return source.getValue(x + offsetX, y + offsetY);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        // not used
        return 0;
    }

    // map the module to the range -0.5 to 0.5
    // this ensures warping occurs in the positive and negative directions
    private static Module map(Module in) {
        if (in.minValue() == -0.5F && in.maxValue() == 0.5F) {
            return in;
        }

        float range = in.maxValue() - in.minValue();

        // if range is 1 then the module just needs biasing by some value so that it's min becomes -0.5
        if (range == 1F) {
            float bias = -0.5F - in.minValue();
            return in.bias(bias);
        }

        // if range is not 1 then use map to squash/stretch it to correct range
        return in.map(-0.5, 0.5);
    }
}
