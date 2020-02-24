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
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCell extends FastSource {

    private final Module lookup;
    private final CellFunc cellFunc;
    private final DistanceFunc distFunc;
    private final float min;
    private final float max;
    private final float range;

    public FastCell(Builder builder) {
        super(builder);
        lookup = builder.getSource();
        cellFunc = builder.getCellFunc();
        distFunc = builder.getDistFunc();
        min = min(cellFunc, lookup);
        max = max(cellFunc, lookup);
        range = max - min;
    }

    @Override
    public float getValue(float x, float y, int seed) {
        x *= frequency;
        y *= frequency;
        float value = Noise.cell(x, y, seed, cellFunc, distFunc, lookup);
        if (cellFunc != CellFunc.NOISE_LOOKUP) {
            return NoiseUtil.map(value, min, max, range);
        }
        return value;
    }

    static float min(CellFunc func, Module lookup) {
        if (func == CellFunc.NOISE_LOOKUP) {
            return lookup.minValue();
        }
        if (func == CellFunc.DISTANCE) {
            return -1;
        }
        return -1;
    }

    static float max(CellFunc func, Module lookup) {
        if (func == CellFunc.NOISE_LOOKUP) {
            return lookup.maxValue();
        }
        if (func == CellFunc.DISTANCE) {
            return 0.25F;
        }
        return 1;
    }
}
