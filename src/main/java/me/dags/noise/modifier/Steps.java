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

package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final Module steps;

    public Steps(Module source, Module steps) {
        super(source.map(0, 1));
        if (steps.minValue() < 1) {
            throw new IllegalArgumentException("steps cannot less than 1");
        }
        this.steps = steps;
    }

    @Override
    public float minValue() {
        return 0F;
    }

    public float maxValue() {
        return 1F;
    }

    @Override
    public float modify(float x, float y, float value) {
        float steps = this.steps.getValue(x, y);
        value = NoiseUtil.round(value * steps);
        return value / steps;
    }
}
