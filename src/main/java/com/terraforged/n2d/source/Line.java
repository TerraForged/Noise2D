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

package com.terraforged.n2d.source;

import com.terraforged.cereal.spec.Context;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.cereal.value.DataObject;
import com.terraforged.cereal.value.DataValue;
import com.terraforged.n2d.Module;
import com.terraforged.n2d.util.NoiseUtil;

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
    public String getSpecName() {
        return "Line";
    }

    @Override
    public float getValue(float x, float y) {
        float widthMod = getWidthModifier(x, y);
        return getValue(x, y, widthMod);
    }

    public float getValue(float x, float y, float widthModifier) {
        return getValue(x, y, 0, widthModifier);
    }

    public float getValue(float x, float y, float minWidth2, float widthModifier) {
        float dist2 = getDistance2(x, y);
        float radius2 = minWidth2 + radius.getValue(x, y) * widthModifier;
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

    public static boolean intersect(float ax1, float ay1, float ax2, float ay2, float bx1, float by1, float bx2, float by2) {
        return ((relativeCCW(ax1, ay1, ax2, ay2, bx1, by1) * relativeCCW(ax1, ay1, ax2, ay2, bx2, by2) <= 0)
                && (relativeCCW(bx1, by1, bx2, by2, ax1, ay1) * relativeCCW(bx1, by1, bx2, by2, ax2, ay2) <= 0));
    }

    private static int relativeCCW(float x1, float y1, float x2, float y2, float px, float py) {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        double ccw = px * y2 - py * x2;
        if (ccw == 0F) {
            // The point is colinear, classify based on which side of
            // the segment the point falls on.  We can calculate a
            // relative value using the projection of px,py onto the
            // segment - a negative value indicates the point projects
            // outside of the segment in the direction of the particular
            // endpoint used as the origin for the projection.
            ccw = px * x2 + py * y2;
            if (ccw > 0.0) {
                // Reverse the projection to be relative to the original x2,y2
                // x2 and y2 are simply negated.
                // px and py need to have (x2 - x1) or (y2 - y1) subtracted
                //    from them (based on the original values)
                // Since we really want to get a positive answer when the
                //    point is "beyond (x2,y2)", then we want to calculate
                //    the inverse anyway - thus we leave x2 & y2 negated.
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if (ccw < 0.0) {
                    ccw = 0.0;
                }
            }
        }
        return (ccw < 0F) ? -1 : ((ccw > 0F) ? 1 : 0);
    }

    private static Line create(DataObject data, DataSpec<Line> spec, Context context) {
        return new Line(
                spec.get("x1", data, DataValue::asFloat),
                spec.get("y1", data, DataValue::asFloat),
                spec.get("x2", data, DataValue::asFloat),
                spec.get("y2", data, DataValue::asFloat),
                spec.get("radius", data, Module.class),
                spec.get("fade_in", data, Module.class),
                spec.get("fade_out", data, Module.class),
                spec.get("feather", data, DataValue::asFloat)
        );
    }

    public static DataSpec<Line> spec() {
        return DataSpec.builder("Line", Line.class, Line::create)
                .add("x1", 0F, l -> l.x1)
                .add("y1", 0F, l -> l.y1)
                .add("x2", 0F, l -> l.x2)
                .add("y2", 0F, l -> l.y2)
                .add("feather", 0F, l -> l.featherScale)
                .addObj("radius", l -> l.radius)
                .addObj("fade_in", l -> l.fadeIn)
                .addObj("fade_out", l -> l.fadeOut)
                .build();
    }
}
