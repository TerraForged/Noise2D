package me.dags.noise.modifier;

import me.dags.noise.Module;

public class Alpha extends Modifier {

    private final Module alpha;

    public Alpha(Module source, Module alpha) {
        super(source);
        this.alpha = alpha;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float a = alpha.getValue(x, y);
        return (noiseValue * a) + (1 - a);
    }
}
