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

import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Invert extends Modifier {

    public Invert(Module source) {
        super(source);
    }

    @Override
    public String getSpecName() {
        return "Invert";
    }

    @Override
    public float modify(int seed, float x, float y, float noiseValue) {
        if (noiseValue > source.maxValue()) {
            return source.minValue();
        }
        if (noiseValue < source.minValue()) {
            return source.maxValue();
        }
        return source.maxValue() - noiseValue;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static DataSpec<Invert> spec() {
        return Modifier.spec(Invert.class, Invert::new);
    }
}
