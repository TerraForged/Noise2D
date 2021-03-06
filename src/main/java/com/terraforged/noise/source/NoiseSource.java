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

import com.terraforged.cereal.spec.Context;
import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.cereal.value.DataObject;
import com.terraforged.cereal.value.DataValue;
import com.terraforged.noise.Module;
import com.terraforged.noise.func.CellFunc;
import com.terraforged.noise.func.DistanceFunc;
import com.terraforged.noise.func.EdgeFunc;
import com.terraforged.noise.func.Interpolation;
import com.terraforged.noise.util.NoiseSpec;

import java.util.function.Function;

public abstract class NoiseSource implements Module {

    protected final int seed;
    protected final int octaves;
    protected final float gain;
    protected final float frequency;
    protected final float lacunarity;
    protected final Interpolation interpolation;

    public NoiseSource(Builder builder) {
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

    public static Builder readData(DataObject data, DataSpec<?> spec, Context context) {
        Builder builder = new Builder();
        builder.seed(NoiseSpec.seed(data, spec, context));
        builder.gain(spec.get("gain", data, DataValue::asDouble));
        builder.octaves(spec.get("octaves", data, DataValue::asInt));
        builder.frequency(spec.get("frequency", data, DataValue::asDouble));
        builder.lacunarity(spec.get("lacunarity", data, DataValue::asDouble));
        builder.interp(spec.get("interp", data, v -> v.asEnum(Interpolation.class)));
        if (data.has("cell_func")) {
            builder.cellFunc(spec.get("cell_func", data, v -> v.asEnum(CellFunc.class)));
        }
        if (data.has("edge_func")) {
            builder.edgeFunc(spec.get("edge_func", data, v -> v.asEnum(EdgeFunc.class)));
        }
        if (data.has("dist_func")) {
            builder.distFunc(spec.get("dist_func", data, v -> v.asEnum(DistanceFunc.class)));
        }
        if (data.has("source")) {
            builder.source(spec.get("source", data, Module.class, context));
        }
        return builder;
    }

    private static <S extends NoiseSource> DataFactory<S> constructor(Function<Builder, S> constructor) {
        return (data, spec, context) -> constructor.apply(readData(data, spec, context));
    }

    public static <S extends NoiseSource> DataSpec.Builder<S> specBuilder(String name, Class<S> type, Function<Builder, S> constructor) {
        return specBuilder(name, type, constructor(constructor));
    }

    public static <S extends NoiseSource> DataSpec.Builder<S> specBuilder(String name, Class<S> type, DataFactory<S> constructor) {
        return DataSpec.builder(name, type, constructor)
                .add("seed", Builder.DEFAULT_SEED, NoiseSpec.seed(f -> f.seed))
                .add("gain", Builder.DEFAULT_GAIN, f -> f.gain)
                .add("octaves", Builder.DEFAULT_OCTAVES, f -> f.octaves)
                .add("frequency", Builder.DEFAULT_FREQUENCY, f -> f.frequency)
                .add("lacunarity", Builder.DEFAULT_LACUNARITY, f -> f.lacunarity)
                .add("interp", Builder.DEFAULT_INTERPOLATION, f -> f.interpolation);
    }
}
