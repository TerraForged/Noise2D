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

package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class Line implements Module {

    private final float x1;
    private final float y1;
    private final float x2;
    private final float y2;
    private final float dx;
    private final float dy;
    private final float orthX1;
    private final float orthY1;
    private final float orthX2;
    private final float orthY2;
    private final float length2;
    private final float featherBias;
    private final float featherScale;
    private final Module fadeIn;
    private final Module fadeOut;
    private final Module radius;

    public Line(float x1, float y1, float x2, float y2, Module radius2, Module fadeIn, Module fadeOut, float feather) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.orthX1 = x1 + (y2 - y1);
        this.orthY1 = y1 + (x1 - x2);
        this.orthX2 = x2 + (y2 - y1);
        this.orthY2 = y2 + (x1 - x2);
        this.dx = x2 - x1;
        this.dy = y2 - y1;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.radius = radius2;
        this.featherScale = feather;
        this.featherBias = 1 - feather;
        this.length2 = dx * dx + dy * dy;
    }

    @Override
    public float getValue(float x, float y) {
        float widthMod = getWidthModifier(x, y);
        return getValue(x, y, widthMod);
    }

    public float getValue(float x, float y, float widthModifier) {
        float dist2 = getDistance2(x, y);
        float radius2 = radius.getValue(x, y) * widthModifier;
        if (dist2 > radius2) {
            return 0;
        }
        float value = dist2 / radius2;
        if (featherScale == 0) {
            return 1 - value;
        }
        float feather = featherBias + (widthModifier * featherScale);
        return (1 - value) * feather;
    }

    /**
     * Check if the position x,y is 'before' the start of this line
     */
    public boolean clipStart(float x, float y) {
        return sign(x, y, x1, y1, orthX1, orthY1) > 0;
    }

    /**
     * Check if the position x,y is past the end of this line
     */
    public boolean clipEnd(float x, float y) {
        return sign(x, y, x2, y2, orthX2, orthY2) < 0;
    }

    public float getWidthModifier(float x, float y) {
        float d1 = dist2(x, y, x1, y1);
        if (d1 == 0) {
            return 0;
        }

        float d2 = dist2(x, y, x2, y2);
        if (d2 == 0) {
            return 0;
        }

        float fade = 1F;
        float in = fadeIn.getValue(x, y);
        float out = fadeOut.getValue(x, y);
        if (in > 0) {
            float dist = in * length2;
            if (d1 < dist) {
                fade *= (d1 / dist);
            }
        }

        if (out > 0) {
            float dist = out * length2;
            if (d2 < dist) {
                fade *= (d2 / dist);
            }
        }
        return fade;
    }

    private float getDistance2(float x, float y) {
        float t = ((x - x1) * dx) + ((y - y1) * dy);
        float s = NoiseUtil.clamp(t / length2, 0, 1);
        float ix = x1 + s * dx;
        float iy = y1 + s * dy;
        return dist2(x, y, ix, iy);
    }

    public static float dist2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }

    public static int sign(float x, float y, float x1, float y1, float x2, float y2) {
        float value = (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1);
        if (value == 0) {
            return 0;
        }
        if (value < 0) {
            return -1;
        }
        return 1;
    }
}
