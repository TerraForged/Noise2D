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
import com.terraforged.noise.util.Noise;
import com.terraforged.noise.util.NoiseUtil;

public class CubicNoise extends NoiseSource {

    private final float min;
    private final float max;
    private final float range;

    public CubicNoise(Builder builder) {
        super(builder);
        min = calculateBound(-0.75F, builder.getOctaves(), builder.getGain());
        max = calculateBound(0.75F, builder.getOctaves(), builder.getGain());
        range = max - min;
    }

    @Override
    public String getSpecName() {
        return "Cubic";
    }

    @Override
    public float minValue() {
        return min;
    }

    @Override
    public float maxValue() {
        return max;
    }

    @Override
    public float getValue(float x, float y, int seed) {
        x *= frequency;
        y *= frequency;

        float sum = Noise.singleCubic(x, y, seed);
        float amp = 1;
        int i = 0;

        while (++i < octaves) {
            x *= lacunarity;
            y *= lacunarity;

            amp *= gain;
            sum += Noise.singleCubic(x, y, ++seed) * amp;
        }

        return NoiseUtil.map(sum, min, max, range);
    }

    private float calculateBound(float signal, int octaves, float gain) {
        float amp = 1F;
        float value = signal;
        for (int i = 1; i < octaves; i++) {
            amp *= gain;
            value += signal * amp;
        }
        return value;
    }

    public static DataSpec<CubicNoise> spec() {
        return specBuilder("Cubic", CubicNoise.class, CubicNoise::new).build();
    }
}
