package me.dags.noise.source;

import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastPerlin extends FastSource {

    private static final float[] signals = {1F, 0.900F, 0.83F, 0.75F, 0.64F, 0.62F, 0.61F};
    protected final float min;
    protected final float max;
    protected final float range;

    public FastPerlin(Builder builder) {
        super(builder);
        min = min(builder.getOctaves(), builder.getGain());
        max = max(builder.getOctaves(), builder.getGain());
        range = Math.abs(max - min);
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        float sum = 0;
        float amp = gain;

        for (int i = 0; i < octaves; i++) {
            sum += Noise.singlePerlin(x, y, seed + i, interpolation) * amp;
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
        }

        return NoiseUtil.map(sum, min, max, range);
    }

    protected float min(int octaves, float gain) {
        return -max(octaves, gain);
    }

    protected float max(int octaves, float gain) {
        float signal = signal(octaves);
        float sum = 0;
        float amp = gain;
        for (int i = 0; i < octaves; i++) {
            sum += signal * amp;
            amp *= gain;
        }
        return sum;
    }

    private static float signal(int octaves) {
        int index = Math.min(octaves, signals.length - 1);
        return signals[index];
    }
}
