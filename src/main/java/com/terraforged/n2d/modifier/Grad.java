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
import com.terraforged.n2d.util.NoiseUtil;

public class Grad extends Modifier {

    private final Module lower;
    private final Module upper;
    private final Module strength;

    public Grad(Module source, Module lower, Module upper, Module strength) {
        super(source);
        this.lower = lower;
        this.upper = upper;
        this.strength = strength;
    }

    @Override
    public String getSpecName() {
        return "Grad";
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float upperBound = upper.getValue(x, y);
        if (noiseValue > upperBound) {
            return noiseValue;
        }

        float amount = strength.getValue(x, y);
        float lowerBound = lower.getValue(x, y);
        if (noiseValue < lowerBound) {
            return NoiseUtil.pow(noiseValue, 1 - amount);
        }

        float alpha = 1 - ((noiseValue - lowerBound) / (upperBound - lowerBound));
        float power = 1 - (amount * alpha);
        return NoiseUtil.pow(noiseValue, power);
    }

    private static DataFactory<Grad> factory = (data, spec, context) -> new Grad(
            spec.get("source", data, Module.class, context),
            spec.get("lower", data, Module.class, context),
            spec.get("upper", data, Module.class, context),
            spec.get("strength", data, Module.class, context)
    );

    public static DataSpec<Grad> spec() {
        return Modifier.sourceBuilder(Grad.class, factory)
                .addObj("lower", g -> g.lower)
                .addObj("upper", g -> g.upper)
                .addObj("strength", g -> g.strength)
                .build();
    }
}
