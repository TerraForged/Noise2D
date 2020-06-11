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

package com.terraforged.n2d.modifier;

import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.n2d.Module;

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
    public float modify(float x, float y, float noiseValue) {
        return noiseValue * scale.getValue(x, y);
    }

    private static final DataFactory<Scale> factory = (data, spec, context) -> new Scale(
            spec.get("source", data, Module.class, context),
            spec.get("scale", data, Module.class, context)
    );

    public static DataSpec<Scale> spec() {
        return Modifier.sourceBuilder(Scale.class, factory).addObj("scale", s -> s.scale).build();
    }
}
