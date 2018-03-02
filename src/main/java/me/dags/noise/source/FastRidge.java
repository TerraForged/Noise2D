package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.func.Noise;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastRidge extends FastPerlin {

    public FastRidge(Builder builder) {
        super(builder);
    }

    @Override
    public String getName() {
        return "ridge";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        int seed = this.seed;
        float sum = 1 - Math.abs(Noise.singlePerlin(x, y, seed, interpolation));
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum -= (1 - Math.abs(Noise.singlePerlin(x, y, ++seed, interpolation))) * amp;
        }

        return sum;
    }
}
