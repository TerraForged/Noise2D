package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.domain.Domain;

/**
 * @author dags <dags@dags.me>
 */
public class Warp extends Modifier {

    private final Domain domain;

    public Warp(Module source, Domain domain) {
        super(source);
        this.domain = domain;
    }

    @Override
    public float getValue(float x, float y) {
        return source.getValue(domain.getX(x, y), domain.getY(x, y));
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        // not used
        return 0;
    }
}
