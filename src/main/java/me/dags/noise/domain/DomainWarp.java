package me.dags.noise.domain;

import me.dags.noise.Module;

public class DomainWarp implements Domain {

    private final Module x;
    private final Module y;
    private final Module distance;

    public DomainWarp(Module x, Module y, Module distance) {
        this.x = map(x);
        this.y = map(y);
        this.distance = distance;
    }

    @Override
    public float getX(float x, float y) {
        float ox = this.x.getValue(x, y) * this.distance.getValue(x, y);
        return x + ox;
    }

    @Override
    public float getY(float x, float y) {
        float oy = this.y.getValue(x, y) * this.distance.getValue(x, y);
        return y + oy;
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
