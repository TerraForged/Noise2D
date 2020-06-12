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
import com.terraforged.n2d.Source;

/**
 * @author dags <dags@dags.me>
 */
public class Clamp extends Modifier {

    private final Module min;
    private final Module max;

    public Clamp(Module source, float min, float max) {
        this(source, Source.constant(min), Source.constant(max));
    }

    public Clamp(Module source, Module min, Module max) {
        super(source);
        this.min = min;
        this.max = max;
    }

    @Override
    public String getSpecName() {
        return "Clamp";
    }

    @Override
    public float minValue() {
        return min.minValue();
    }

    @Override
    public float maxValue() {
        return max.maxValue();
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float min = this.min.getValue(x, y);
        float max = this.max.getValue(x, y);
        if (noiseValue < min) {
            return min;
        }
        if (noiseValue > max) {
            return max;
        }
        return noiseValue;
    }

    private static final DataFactory<Clamp> factory = (data, spec, context) -> new Clamp(
            spec.get("source", data, Modifier.class, context),
            spec.get("min", data, Modifier.class, context),
            spec.get("max", data, Modifier.class, context)
    );

    public static DataSpec<Clamp> spec() {
        return Modifier.sourceBuilder(Clamp.class, factory)
                .addObj("min", Module.class, c -> c.min)
                .addObj("max", Module.class, c -> c.max)
                .build();
    }
}
