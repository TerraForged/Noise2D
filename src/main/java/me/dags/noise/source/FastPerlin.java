package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Builder;
import me.dags.noise.func.Interpolation;
import me.dags.noise.func.Noise;
import me.dags.noise.util.Util;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastPerlin extends FastSource {

    protected final Interpolation interpolation;

    public FastPerlin(Builder builder) {
        super(builder);
        interpolation = builder.interp();
    }

    @Override
    public String getName() {
        return "perlin";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        int seed = this.seed;
        float sum = Noise.singlePerlin(x, y, seed, interpolation);
        float amp = 1;

        for (int i = 1; i < octaves; i++) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += Noise.singlePerlin(x, y, ++seed, interpolation) * amp;
        }

        return sum * bounding;
    }

    @Override
    public Builder toBuilder() {
        return super.toBuilder()
                .interp(interpolation);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        Util.setNonDefault(node, "interpolation", interpolation, Builder.INTERP);
    }

    @Override
    public String toString() {
        return getName() + "{"
                + properties()
                + ", interpolation=" + interpolation
                + "}";
    }
}
