package me.dags.noise.modifier;

import me.dags.noise.Module;

public class Freq extends Modifier {

    private final Module x;
    private final Module y;

    public Freq(Module source, Module x, Module y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    @Override
    public float getValue(float x, float y) {
        float fx = this.x.getValue(x, y);
        float fy = this.y.getValue(x, y);
        return source.getValue(x * fx, y * fy);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return 0;
    }
}
