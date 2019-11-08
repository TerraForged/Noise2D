package me.dags.noise.domain;

import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.util.NoiseUtil;

public class DirectionWarp implements Domain {

    private final Module direction;
    private final Module strength;

    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private float ox = 0;
    private float oy = 0;

    public DirectionWarp(Module direction, Module strength) {
        this.direction = direction;
        this.strength = strength;
    }

    @Override
    public float getX(float x, float y) {
        return x + update(x, y).ox;
    }

    @Override
    public float getY(float x, float y) {
        return y + update(x, y).oy;
    }

    private DirectionWarp update(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            float angle = direction.getValue(x, y) * 2 * NoiseUtil.PI2;
            ox = NoiseUtil.sin(angle) * strength.getValue(x, y);
            oy = NoiseUtil.cos(angle) * strength.getValue(x, y);
        }
        return this;
    }

    public static Domain of(Source type, int seed, int scale, int octaves, double strength) {
        Module direction = Source.build(seed, scale, octaves).build(type);
        Module power = Source.constant(strength);
        return new DirectionWarp(direction, power);
    }
}
