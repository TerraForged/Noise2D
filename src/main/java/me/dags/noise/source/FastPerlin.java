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
    protected final float min;
    protected final float max;
    protected final float range;

    public FastPerlin(Builder builder) {
        super(builder);
        interpolation = builder.interp();
        min = calculateBound(minSignal(), builder.octaves(), builder.gain());
        max = calculateBound(maxSignal(), builder.octaves(), builder.gain());
        range = Math.abs(max - min);
        System.out.println(min + ":" + max);
    }

    @Override
    public String getName() {
        return "perlin";
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

        return Noise.map(sum, min, max, range);
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

    protected float minSignal() {
        return -0.5F;
    }

    protected float maxSignal() {
        return 0.5F;
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
