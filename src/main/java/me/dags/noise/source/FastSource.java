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

package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 * <p>
 * FastSource modules are based on the work of FastNoise_Java https://github.com/Auburns/FastNoise_Java
 */
public abstract class FastSource implements Module {

    protected final int seed;
    protected final int octaves;
    protected final float gain;
    protected final float frequency;
    protected final float lacunarity;
    protected final Interpolation interpolation;

    public FastSource(Builder builder) {
        this.seed = builder.getSeed();
        this.octaves = builder.getOctaves();
        this.lacunarity = builder.getLacunarity();
        this.gain = builder.getGain();
        this.frequency = builder.getFrequency();
        this.interpolation = builder.getInterp();
    }

    @Override
    public float getValue(float x, float y) {
        return getValue(x, y, seed);
    }

    public abstract float getValue(float x, float y, int seed);
}
