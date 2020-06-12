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

import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.n2d.Module;
import com.terraforged.n2d.util.Noise;
import com.terraforged.n2d.util.NoiseUtil;

public class Rand implements Module {

    private final int seed;
    private final float frequency;

    public Rand(Builder builder) {
        seed = builder.getSeed();
        frequency = builder.getFrequency();
    }

    @Override
    public String getSpecName() {
        return "Rand";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        // -1 to 1
        float value = Noise.white(x, y, seed);
        return Math.abs(value);
    }

    public float getValue(float x, float y, int childSeed) {
        return Noise.white(x, y, NoiseUtil.hash(seed, childSeed));
    }

    public int nextInt(float x, float y, int range) {
        float noise = getValue(x, y);
        return NoiseUtil.round((range * noise) / (range + range));
    }

    public int nextInt(float x, float y, int childSeed, int range) {
        float noise = getValue(x, y, childSeed);
        return NoiseUtil.round((range * noise) / (range + range));
    }

    private static final DataFactory<Rand> factory = (data, spec, context) -> new Rand(
            FastSource.readData(data, spec, context)
    );

    public static DataSpec<Rand> spec() {
        return DataSpec.builder("Rand", Rand.class, factory)
                .add("seed", 0, r -> r.seed)
                .add("frequency", 1F, r -> r.frequency)
                .build();
    }
}
