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
import com.terraforged.cereal.value.DataValue;
import com.terraforged.n2d.Module;
import com.terraforged.n2d.util.NoiseUtil;

public class AdvancedTerrace extends Modifier {

    private final int steps;
    private final int octaves;
    private final float modRange;
    private final float blendMin;
    private final float blendMax;
    private final float blendRange;
    private final Module slope;
    private final Module mask;
    private final Module modulation;

    public AdvancedTerrace(Module source, Module modulation, Module mask, Module slope, float blendMin, float blendMax, int steps, int octaves) {
        super(source);
        this.mask = mask;
        this.steps = steps;
        this.octaves = octaves;
        this.slope = slope;
        this.modulation = modulation;
        this.blendMin = blendMin;
        this.blendMax = blendMax;
        this.blendRange = this.blendMax - this.blendMin;
        this.modRange = source.maxValue() + modulation.maxValue();
    }

    @Override
    public String getSpecName() {
        return "AdvTerrace";
    }

    @Override
    public float modify(float x, float y, float value) {
        if (value <= blendMin) {
            return value;
        }

        float mask = this.mask.getValue(x, y);
        if (mask == 0) {
            return value;
        }

        float result = value;
        float slope = this.slope.getValue(x, y);
        float modulation = this.modulation.getValue(x, y);
        for (int i = 1; i <= octaves; i++) {
            result = getStepped(result, steps * i);
            result = getSloped(value, result, slope);
        }

        result = getModulated(result, modulation);

        float alpha = getAlpha(value);
        if (mask != 1) {
            alpha *= mask;
        }

        return NoiseUtil.lerp(value, result, alpha);
    }

    private float getModulated(float value, float modulation) {
        return (value + modulation) / modRange;
    }

    private float getStepped(float value, int steps) {
        value = NoiseUtil.round(value * steps);
        return value / steps;
    }

    private float getSloped(float value, float stepped, float slope) {
        float delta = (value - stepped);
        float amount = delta * slope;
        return stepped + amount;
    }

    private float getAlpha(float value) {
        if (value > blendMax) {
            return 1;
        }
        return (value - blendMin) / blendRange;
    }

    private static final DataFactory<AdvancedTerrace> factory = (data, spec, context) -> new AdvancedTerrace(
            spec.get("source", data, Module.class, context),
            spec.get("modulation", data, Module.class, context),
            spec.get("mask", data, Module.class, context),
            spec.get("slope", data, Module.class, context),
            spec.get("blend_min", data, DataValue::asFloat),
            spec.get("blend_max", data, DataValue::asFloat),
            spec.get("steps", data, DataValue::asInt),
            spec.get("octaves", data, DataValue::asInt)
    );

    public static DataSpec<AdvancedTerrace> spec() {
        return DataSpec.builder("AdvTerrace", AdvancedTerrace.class, factory)
                .add("steps", 1F, a -> a.steps)
                .add("octaves", 1F, a -> a.octaves)
                .add("blend_min", 0F, a -> a.blendMin)
                .add("blend_max", 1F, a -> a.blendMax)
                .addObj("source", Module.class, a -> a.source)
                .addObj("modulation", Module.class, a -> a.modulation)
                .addObj("slope", Module.class, a -> a.slope)
                .addObj("mask", Module.class, a -> a.mask)
                .build();
    }
}
