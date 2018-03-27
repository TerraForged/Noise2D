package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.func.Noise;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastBillow extends FastPerlin {

    public FastBillow(Builder builder) {
        super(builder);
    }

    @Override
    public String getName() {
        return "billow";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        float amp = 1;
        float sum = Math.abs(Noise.singlePerlin(x, y, seed, interpolation)) * 2 - 1;

        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            x *= lacunarity;
            y *= lacunarity;
            sum += (Math.abs(Noise.singlePerlin(x, y, seed + i, interpolation)) * 2 - 1) * amp;
        }

        return Noise.map(sum, min, max, range);
    }

    protected float minSignal() {
        return 0F;
    }

    protected float maxSignal() {
        return 0.5F;
    }

    @Override
    protected float calculateBound(float signal, int octaves, float gain) {
        // min output when signal = 0.5 -> 0.5 * 2 - 1 = -1
        // max output when signal = 0.0 -> 0.0 * 2 - 1 =  0
        float amp = 1F;
        float value = Math.abs(signal) * 2 - 1F;
        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            value += (Math.abs(signal) * 2 - 1F) * amp;
        }
        return value;
    }
}
