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
import me.dags.noise.func.CurveFunc;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final Module steps;
    private final Module slopeMin;
    private final Module slopeMax;
    private final CurveFunc curve;

    public Steps(Module source, Module steps, Module slopeMin, Module slopeMax, CurveFunc slopeCurve) {
        super(source);
        this.steps = steps;
        this.curve = slopeCurve;
        this.slopeMin = slopeMin;
        this.slopeMax = slopeMax;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        // round the noise down to the nearest step height
        float stepCount = steps.getValue(x, y);
        float value = ((int) (noiseValue * stepCount)) / stepCount;

        // the distance between steps where blending starts to occur
        float min = slopeMin.getValue(x, y);

        // the distance between steps where blending is at 100%
        float max = slopeMax.getValue(x, y);

        // blend range
        float range = max - min;
        if (range <= 0) {
            // no blending so we can just return the step value
            return value;
        }

        // derive an alpha value from the difference between noise & step heights
        float delta = noiseValue - value;

        // alpha is equal to the delta divided by the size of one step (ie delta / (1F / stepCount))
        // this can be simplified to delta * stepCount
        // map this to the defined blend range
        float alpha = NoiseUtil.map(delta * stepCount, min, max, range);

        // lerp from step to noise value with curve applied to alpha
        return NoiseUtil.lerp(value, noiseValue, curve.apply(alpha));
    }
}
