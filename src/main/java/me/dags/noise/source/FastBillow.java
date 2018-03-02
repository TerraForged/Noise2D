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

        int seed = this.seed;
        float sum = Math.abs(Noise.singlePerlin(x, y, seed, interpolation)) * 2 - 1;
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
            sum += (Math.abs(Noise.singlePerlin(x, y, ++seed, interpolation)) * 2 - 1) * amp;
        }

        return sum * bounding;
    }
}
