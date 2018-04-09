package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.Source;

/**
 * @author dags <dags@dags.me>
 * <p>
 * FastSource modules are based on the work of FastNoise_Java https://github.com/Auburns/FastNoise_Java
 */
public abstract class FastSource implements Source {

    protected final int seed;
    protected final int octaves;
    protected final float lacunarity;
    protected final float gain;
    protected final float frequency;

    public FastSource(Builder builder) {
        this.seed = builder.getSeed();
        this.octaves = builder.getOctaves();
        this.lacunarity = builder.getLacunarity();
        this.gain = builder.getGain();
        this.frequency = builder.getFrequency();
    }

    @Override
    public float minValue() {
        return 0;
    }

    @Override
    public float maxValue() {
        return 1;
    }

    @Override
    public Builder toBuilder() {
        return Module.builder()
                .seed(seed)
                .octaves(octaves)
                .lacunarity(lacunarity)
                .gain(gain)
                .frequency(frequency);
    }
}
