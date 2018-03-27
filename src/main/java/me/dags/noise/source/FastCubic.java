package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.func.Noise;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCubic extends FastSource {

    public FastCubic(Builder builder) {
        super(builder);
    }

    @Override
    public String getName() {
        return "cubic";
    }

    @Override
    public float minValue() {
        return -1F;
    }

    @Override
    public float maxValue() {
        return 1F;
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        int seed = this.seed;
        float sum = Noise.singleCubic(x, y, seed);
        float amp = 1;
        int i = 0;

        while (++i < octaves) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += Noise.singleCubic(x, y, ++seed) * amp;
        }

        return sum;
    }
}
