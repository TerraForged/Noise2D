package me.dags.noise.source;

import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastPerlin extends FastSource {

    protected final Interpolation interpolation;
    protected final float min;
    protected final float max;
    protected final float range;

    public FastPerlin(Builder builder) {
        super(builder);
        interpolation = builder.getInterp();
        min = calculateBound(minSignal(), builder.getOctaves(), builder.getGain());
        max = calculateBound(maxSignal(), builder.getOctaves(), builder.getGain());
        range = Math.abs(max - min);
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        float amp = 1;
        float sum = Noise.singlePerlin(x, y, seed, interpolation);

        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            x *= lacunarity;
            y *= lacunarity;
            sum += Noise.singlePerlin(x, y, seed + i, interpolation) * amp;
        }

        return NoiseUtil.map(sum, min, max, range);
    }

    @Override
    public Builder toBuilder() {
        return super.toBuilder()
                .interp(interpolation);
    }

    protected float minSignal() {
        return -0.775F;
    }

    protected float maxSignal() {
        return 0.775F;
    }

    protected float calculateBound(float signal, int octaves, float gain) {
        float amp = 1F;
        float value = signal;
        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            value += signal * amp;
        }
        return value;
    }
}
