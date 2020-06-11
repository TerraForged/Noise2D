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

package com.terraforged.n2d.domain;

import com.terraforged.cereal.spec.Context;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.cereal.value.DataObject;
import com.terraforged.n2d.Module;
import com.terraforged.n2d.util.NoiseUtil;

public class DirectionWarp implements Domain {

    private final Module direction;
    private final Module strength;

    private float x = Float.MAX_VALUE;
    private float y = Float.MAX_VALUE;
    private float ox = 0;
    private float oy = 0;

    public DirectionWarp(Module direction, Module strength) {
        this.direction = direction;
        this.strength = strength;
    }

    @Override
    public String getSpecName() {
        return "DirectionWarp";
    }

    @Override
    public float getOffsetX(float x, float y) {
        float angle = direction.getValue(x, y) * NoiseUtil.PI2;
        return NoiseUtil.sin(angle) * strength.getValue(x, y);
    }

    @Override
    public float getOffsetY(float x, float y) {
        float angle = direction.getValue(x, y) * NoiseUtil.PI2;
        return NoiseUtil.cos(angle) * strength.getValue(x, y);
    }

    private static DirectionWarp create(DataObject data, DataSpec<?> spec, Context context) {
        return new DirectionWarp(
                spec.get("direction", data, Module.class, context),
                spec.get("strength", data, Module.class, context)
        );
    }

    public static DataSpec<? extends Domain> spec() {
        return DataSpec.builder("DirectionWarp", DirectionWarp.class, DirectionWarp::create)
                .addObj("direction", w -> w.direction)
                .addObj("strength", w -> w.strength)
                .build();
    }
}
