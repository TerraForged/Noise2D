package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Power extends Modifier {

    private final Module n;

    public Power(Module source, Module n) {
        super(source);
        this.n = n;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return (float) Math.pow(noiseValue, n.getValue(x, y));
    }
}
