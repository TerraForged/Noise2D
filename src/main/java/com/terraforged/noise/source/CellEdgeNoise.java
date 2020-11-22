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
import com.terraforged.noise.func.DistanceFunc;
import com.terraforged.noise.func.EdgeFunc;
import com.terraforged.noise.util.Noise;
import com.terraforged.noise.util.NoiseUtil;

public class CellEdgeNoise extends NoiseSource {

    private final EdgeFunc edgeFunc;
    private final DistanceFunc distFunc;

    public CellEdgeNoise(Builder builder) {
        super(builder);
        this.edgeFunc = builder.getEdgeFunc();
        this.distFunc = builder.getDistFunc();
    }

    @Override
    public String getSpecName() {
        return "CellEdge";
    }

    @Override
    public float getValue(float x, float y, int seed) {
        x *= frequency;
        y *= frequency;
        float value = Noise.cellEdge(x, y, seed, edgeFunc, distFunc);
        return NoiseUtil.map(value, edgeFunc.min(), edgeFunc.max(), edgeFunc.range());
    }

    public static DataSpec<CellEdgeNoise> spec() {
        return specBuilder("CellEdge", CellEdgeNoise.class, CellEdgeNoise::new)
                .add("edge_func", Builder.DEFAULT_EDGE_FUNC, f -> f.edgeFunc.name())
                .add("dist_func", Builder.DEFAULT_DIST_FUNC, f -> f.distFunc.name())
                .build();
    }
}
