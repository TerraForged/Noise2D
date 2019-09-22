package me.dags.noise.source;

import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCubic extends FastSource {

    private final float min;
    private final float max;
    private final float range;

    public FastCubic(Builder builder) {
        super(builder);
        min = calculateBound(-0.75F, builder.getOctaves(), builder.getGain());
        max = calculateBound(0.75F, builder.getOctaves(), builder.getGain());
        range = max - min;
    }

    @Override
    public float minValue() {
        return min;
    }

    @Override
    public float maxValue() {
        return max;
    }

    @Override
    public float getValue(float x, float y, int seed) {
        x *= frequency;
        y *= frequency;

        float sum = Noise.singleCubic(x, y, seed);
        float amp = 1;
        int i = 0;

        while (++i < octaves) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += Noise.singleCubic(x, y, ++seed) * amp;
        }

        return NoiseUtil.map(sum, min, max, range);
    }

    private float calculateBound(float signal, int octaves, float gain) {
        float amp = 1F;
        float value = signal;
        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            value += signal * amp;
        }
        return value;
    }
}
