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

package com.terraforged.noise.source;

import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.noise.Module;
import com.terraforged.noise.Source;
import com.terraforged.noise.util.N2DUtil;
import com.terraforged.noise.util.Noise;
import com.terraforged.noise.util.NoiseUtil;

import java.awt.*;
import java.util.Arrays;

public class SimplexRidgeNoise extends NoiseSource {

    private static final int RIDGED_MAX_OCTAVE = 30;

    private final float[] spectralWeights;
    private final float min;
    private final float max;
    private final float range;

    public SimplexRidgeNoise(Builder builder) {
        super(builder);
        this.spectralWeights = new float[RIDGED_MAX_OCTAVE];

        float h = 1.0F;
        float frequency = 1.0F;
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            spectralWeights[i] = NoiseUtil.pow(frequency, -h);
            frequency *= lacunarity;
        }

        min = 0;
        max = SimplexNoise2.max(builder.getOctaves(), builder.getGain());
        range = Math.abs(max - min);
    }

    @Override
    public String getSpecName() {
        return "SimplexRidge";
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
            signal = Noise.singleSimplex(x, y, seed + octave);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplexRidgeNoise)) return false;
        if (!super.equals(o)) return false;

        SimplexRidgeNoise that = (SimplexRidgeNoise) o;

        if (Float.compare(that.min, min) != 0) return false;
        if (Float.compare(that.max, max) != 0) return false;
        if (Float.compare(that.range, range) != 0) return false;
        return interpolation == that.interpolation;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(spectralWeights);
        result = 31 * result + (min != +0.0f ? Float.floatToIntBits(min) : 0);
        result = 31 * result + (max != +0.0f ? Float.floatToIntBits(max) : 0);
        result = 31 * result + (range != +0.0f ? Float.floatToIntBits(range) : 0);
        return result;
    }

    public static DataSpec<SimplexRidgeNoise> ridgeSpec() {
        return specBuilder("SimplexRidge", SimplexRidgeNoise.class, SimplexRidgeNoise::new).build();
    }
}