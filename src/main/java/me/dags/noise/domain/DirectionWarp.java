package me.dags.noise.domain;

import me.dags.noise.Module;
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
    public float getOffsetX(float x, float y) {
        return update(x, y).ox;
    }

    @Override
    public float getOffsetY(float x, float y) {
        return update(x, y).oy;
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
}
