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
import com.terraforged.n2d.func.CurveFunc;
import com.terraforged.n2d.func.MidPointCurve;

public class Curve extends Modifier {

    private final CurveFunc func;

    public Curve(Module source, CurveFunc func) {
        super(source);
        this.func = func;
    }

    public Curve(Module source, float mid, float steepness) {
        this(source, new MidPointCurve(mid, steepness));
    }

    @Override
    public String getSpecName() {
        return "Curve";
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return func.apply(noiseValue);
    }

    private static final DataFactory<Curve> factory = (data, spec, context) -> new Curve(
            spec.get("source", data, Module.class, context),
            spec.get("curve", data, CurveFunc.class, context)
    );

    public static DataSpec<Curve> spec() {
        return sourceBuilder(Curve.class, factory).addObj("curve", c -> c.func).build();
    }
}
