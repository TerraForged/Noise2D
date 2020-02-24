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

package me.dags.noise.selector;

import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Blend extends Selector {

    protected final Module source0;
    protected final Module source1;
    protected final float midpoint;
    protected final float blendLower;
    protected final float blendUpper;
    protected final float blendRange;

    public Blend(Module selector, Module source0, Module source1, float midPoint, float blendRange, Interpolation interpolation) {
        super(selector, new Module[]{source0, source1}, interpolation);
        float mid = selector.minValue() + ((selector.maxValue() - selector.minValue()) * midPoint);
        this.source0 = source0;
        this.source1 = source1;
        this.midpoint = midPoint;
        this.blendLower = Math.max(selector.minValue(), mid - (blendRange / 2F));
        this.blendUpper = Math.min(selector.maxValue(), mid + (blendRange / 2F));
        this.blendRange = blendUpper - blendLower;
    }

    @Override
    public float selectValue(float x, float y, float select) {
        if (select < blendLower) {
            return source0.getValue(x, y);
        }
        if (select > blendUpper) {
            return source1.getValue(x, y);
        }
        float alpha = (select - blendLower) / blendRange;
        return blendValues(source0.getValue(x, y), source1.getValue(x, y), alpha);
    }
}
