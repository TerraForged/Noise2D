package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Builder;
import me.dags.noise.func.Interpolation;
import me.dags.noise.func.Noise;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class FlowRidge extends FastSource {

    private static final int RIDGED_MAX_OCTAVE = 30;

    private final Interpolation interpolation;
    private final float[] spectralWeights;
    private final float min;
    private final float max;
    private final float range;

    public FlowRidge(Builder builder) {
        super(builder);
        this.interpolation = builder.interp();
        this.spectralWeights = new float[RIDGED_MAX_OCTAVE];

        double h = 1.0;
        double frequency = 1.0;
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            spectralWeights[i] = (float) Math.pow(frequency, -h);
            frequency *= lacunarity;
        }

        min = calculateBound(0.5F, builder.octaves(), builder.gain());
        max = calculateBound(0.0F, builder.octaves(), builder.gain());
        range = Math.abs(max - min);
    }

    @Override
    public String getName() {
        return "ridge2";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;

        float signal;
        float value = 0.0F;
        float weight = 1.0F;

        float offset = 1.0F;
        float gain = 2.0F;

        for (int curOctave = 0; curOctave < octaves; curOctave++) {
            signal = Noise.singlePerlin(x, y, seed + curOctave, interpolation);
            signal = Math.abs(signal);
            signal = offset - signal;
            signal *= signal;
            signal *= weight;

            weight = signal * gain;
            weight = Math.min(1F, Math.max(0F, weight));

            value += (signal * spectralWeights[curOctave]);

            x *= lacunarity;
            y *= lacunarity;
        }

        return Noise.map(value, min, max, range);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        Util.setNonDefault(node, "interpolation", interpolation, Builder.INTERP);
    }

    private float calculateBound(float signal, int octaves, float gain) {
        float value = 0.0F;
        float weight = 1.0F;

        float amp = 2.0F;
        float offset = 1.0F;

        for (int curOctave = 0; curOctave < octaves; curOctave++) {
            float noise = signal;
            noise = Math.abs(noise);
            noise = offset - noise;
            noise *= noise;
            noise *= weight;

            weight = noise * amp;
            weight = Math.min(1F, Math.max(0F, weight));

            value += (noise * spectralWeights[curOctave]);
        }

        return value;
    }
}
