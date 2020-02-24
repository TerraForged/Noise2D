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

package me.dags.noise.domain;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

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
    public float getOffsetX(float x, float y) {
        return update(x, y).ox;
    }

    @Override
    public float getOffsetY(float x, float y) {
        return update(x, y).oy;
    }

    private DirectionWarp update(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            float angle = direction.getValue(x, y) * 2 * NoiseUtil.PI2;
            ox = NoiseUtil.sin(angle) * strength.getValue(x, y);
            oy = NoiseUtil.cos(angle) * strength.getValue(x, y);
        }
        return this;
    }
}
