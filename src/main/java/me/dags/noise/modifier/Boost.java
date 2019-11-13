package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class Boost extends Modifier {

    private final int iterations;

    public Boost(Module source, int iterations) {
        super(source.map(0, 1));
        this.iterations = Math.max(1, iterations);
    }

    @Override
    public float modify(float x, float y, float value) {
        for (int i = 0; i < iterations; i++) {
            value = NoiseUtil.pow(value, 1 - value);
        }
        return value;
    }
}
