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

package com.terraforged.noise.modifier;

import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Scale extends Modifier {

    private final Module scale;
    private final float min;
    private final float max;

    public Scale(Module source, Module scale) {
        super(source);
        this.scale = scale;
        this.min = source.minValue() * scale.minValue();
        this.max = source.maxValue() * scale.maxValue();
    }

    @Override
    public String getSpecName() {
        return "Scale";
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
    public float modify(int seed, float x, float y, float noiseValue) {
        return noiseValue * scale.getValue(seed, x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Scale scale1 = (Scale) o;

        if (Float.compare(scale1.min, min) != 0) return false;
        if (Float.compare(scale1.max, max) != 0) return false;
        return scale.equals(scale1.scale);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + scale.hashCode();
        result = 31 * result + (min != 0.0f ? Float.floatToIntBits(min) : 0);
        result = 31 * result + (max != 0.0f ? Float.floatToIntBits(max) : 0);
        return result;
    }

    private static final DataFactory<Scale> factory = (data, spec, context) -> new Scale(
            spec.get("source", data, Module.class, context),
            spec.get("scale", data, Module.class, context)
    );

    public static DataSpec<Scale> spec() {
        return Modifier.sourceBuilder(Scale.class, factory).addObj("scale", Module.class, s -> s.scale).build();
    }
}
