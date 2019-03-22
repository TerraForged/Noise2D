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
        this.warpX = x;
        this.warpY = y;
        this.power = power;
    }

    @Override
    public float getValue(float x, float y) {
        float power = this.power.getValue(x, y);
        float half = power / 2;
        x += ((warpX.getValue(x, y) * power) - half);
        y += ((warpY.getValue(x, y) * power) - half);
        return source.getValue(x, y);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return 0;
    }
}
