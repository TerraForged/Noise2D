package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.source.fast.Noise;

/**
 * @author dags <dags@dags.me>
 *
 * FastSource modules are based on the work of FastNoise_Java https://github.com/Auburns/FastNoise_Java
 */
public abstract class FastSource implements Source {

    protected final int seed;
    protected final int octaves;
    protected final float lacunarity;
    protected final float gain;
    protected final float frequency;
    protected final float bounding;

    public FastSource(Builder builder) {
        this(builder.seed(), builder.octaves(), builder.lacunarity(), builder.gain(), builder.frequency());
    }

    public FastSource(int seed, int octaves, float lacunarity, float gain, float frequency) {
        this.seed = seed;
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.gain = gain;
        this.frequency = frequency;
        this.bounding = Noise.calculateFractalBounding(octaves, gain);
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
