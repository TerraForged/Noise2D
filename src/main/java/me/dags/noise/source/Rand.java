package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

public class Rand implements Module {

    private final int seed;
    private final float frequency;

    public Rand(Builder builder) {
        seed = builder.getSeed();
        frequency = builder.getFrequency();
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        // -1 to 1
        float value = Noise.white(x, y, seed);
        return Math.abs(value);
    }

    public float getValue(float x, float y, int childSeed) {
        return Noise.white(x, y, NoiseUtil.hash(seed, childSeed));
    }

    public int nextInt(float x, float y, int range) {
        float noise = getValue(x, y);
        return NoiseUtil.round((range * noise) / (range + range));
    }

    public int nextInt(float x, float y, int childSeed, int range) {
        float noise = getValue(x, y, childSeed);
        return NoiseUtil.round((range * noise) / (range + range));
    }
}
