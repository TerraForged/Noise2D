package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class Modulate extends Modifier {

    private final Module direction;
    private final Module power;

    public Modulate(Module source, Module direction, Module power) {
        super(source);
        this.direction = direction;
        this.power = power;
    }

    @Override
    public float getValue(float x, float y) {
        float angle = direction.getValue(x, y) * NoiseUtil.PI2;
        float strength = power.getValue(x, y);
        float dx = NoiseUtil.sin(angle) * strength;
        float dy = NoiseUtil.cos(angle) * strength;
        return source.getValue(x + dx, y + dy);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return 0;
    }
}
