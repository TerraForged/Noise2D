package me.dags.noise.source;

import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

public class FastSimplex extends FastSource {

    private final float min;
    private final float max;
    private final float range;

    public FastSimplex(Builder builder) {
        super(builder);
        this.min = -max(builder.getOctaves(), builder.getGain());
        this.max = max(builder.getOctaves(), builder.getGain());
        this.range = max - min;
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        float sum = 0;
        float amp = 1;

        for (int i = 0; i < octaves; i++) {
            sum += Noise.singleSimplex(x, y, seed + i, interpolation) * amp;
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
        }

        return NoiseUtil.map(sum, min, max, range);
    }

    private static float max(int octaves, float gain) {
        float signal = signal(octaves);

        float sum = 0;
        float amp = 1;
        for (int i = 0; i < octaves; i++) {
            sum += amp * signal;
            amp *= gain;
        }
        return sum;
    }

    private static float signal(int octaves) {
        int index = Math.min(octaves, signals.length - 1);
        return signals[index];
    }

    private static final float[] signals = {1.00F, 0.989F, 0.810F, 0.781F, 0.708F, 0.702F, 0.696F};
}
