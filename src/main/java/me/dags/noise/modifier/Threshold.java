package me.dags.noise.modifier;

import me.dags.noise.Module;

public class Threshold extends Modifier {

    private final Module threshold;

    public Threshold(Module source, Module threshold) {
        super(source);
        this.threshold = threshold;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float limit = threshold.getValue(x, y);
        if (noiseValue < limit) {
            return 0F;
        }
        return 1;
    }
}
