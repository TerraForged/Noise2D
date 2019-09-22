package me.dags.noise.source;

public class FastBillow extends FastRidge {

    public FastBillow(Builder builder) {
        super(builder);
    }

    @Override
    public float getValue(float x, float y, int seed) {
        return 1 - super.getValue(x, y, seed);
    }
}
