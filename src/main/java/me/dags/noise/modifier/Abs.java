package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Abs extends Modifier {

    public Abs(Module source) {
        super(source);
    }

    @Override
    public String getName() {
        return "abs";
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return Math.abs(noiseValue);
    }
}
