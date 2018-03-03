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
            int seed = (this.seed + curOctave);

            signal = Noise.singlePerlin(x, y, seed, interpolation);
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

        return value * bounding;
    }

    @Override
    public float minValue() {
        return 0;
    }

    @Override
    public float maxValue() {
        return 1F;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        Util.setNonDefault(node, "interpolation", interpolation, Builder.INTERP);
    }
}
