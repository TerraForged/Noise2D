/*
 *
 * MIT License
 *
 * Copyright (c) 2020 TerraForged
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.terraforged.n2d.source;

import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.n2d.func.Interpolation;
import com.terraforged.n2d.util.Noise;
import com.terraforged.n2d.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public class FastRidge extends FastSource {

    private static final int RIDGED_MAX_OCTAVE = 30;

    private final Interpolation interpolation;
    private final float[] spectralWeights;
    private final float min;
    private final float max;
    private final float range;

    public FastRidge(Builder builder) {
        super(builder);
        this.interpolation = builder.getInterp();
        this.spectralWeights = new float[RIDGED_MAX_OCTAVE];

        float h = 1.0F;
        float frequency = 1.0F;
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            spectralWeights[i] = NoiseUtil.pow(frequency, -h);
            frequency *= lacunarity;
        }

        min = 0;
        max = calculateBound(0.0F, builder.getOctaves(), builder.getGain());
        range = Math.abs(max - min);
    }

    @Override
    public String getSpecName() {
        return "Ridge";
    }

    @Override
    public float getValue(float x, float y, int seed) {
        x *= frequency;
        y *= frequency;

        float signal;
        float value = 0.0F;
        float weight = 1.0F;

        float offset = 1.0F;
        float amp = 2.0F;

        for (int octave = 0; octave < octaves; octave++) {
            signal = Noise.singlePerlin(x, y, seed + octave, interpolation);
            signal = Math.abs(signal);
            signal = offset - signal;
            signal *= signal;

            signal *= weight;
            weight = signal * amp;
            weight = NoiseUtil.clamp(weight, 0, 1);
            value += (signal * spectralWeights[octave]);

            x *= lacunarity;
            y *= lacunarity;
            amp *= gain;
        }
        return NoiseUtil.map(value, min, max, range);
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
            amp *= gain;
        }

        return value;
    }

    public static DataSpec<FastSource> spec() {
        return spec("Ridge", FastRidge::new);
    }
}