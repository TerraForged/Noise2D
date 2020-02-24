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

public class DomainWarp implements Domain {

    private final Module x;
    private final Module y;
    private final Module distance;

    public DomainWarp(Module x, Module y, Module distance) {
        this.x = map(x);
        this.y = map(y);
        this.distance = distance;
    }

    @Override
    public float getOffsetX(float x, float y) {
        return this.x.getValue(x, y) * this.distance.getValue(x, y);
    }

    @Override
    public float getOffsetY(float x, float y) {
        return this.y.getValue(x, y) * this.distance.getValue(x, y);
    }

    // map the module to the range -0.5 to 0.5
    // this ensures warping occurs in the positive and negative directions
    private static Module map(Module in) {
        if (in.minValue() == -0.5F && in.maxValue() == 0.5F) {
            return in;
        }

        float range = in.maxValue() - in.minValue();

        // if range is 1 then the module just needs biasing by some value so that it's min becomes -0.5
        if (range == 1F) {
            float bias = -0.5F - in.minValue();
            return in.bias(bias);
        }

        // if range is not 1 then use map to squash/stretch it to correct range
        return in.map(-0.5, 0.5);
    }
}
