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
        float fade = getFade(x, y);
        float dist2 = getDistance2(x, y);
        float radius2 = radius.getValue(x, y) * fade;
        if (dist2 > radius2) {
            return 0;
        }
        float feather = featherBias + (fade * featherScale);
        float value = dist2 / radius2;
        return (1 - value) * feather;
    }

    private float getDistance2(float x, float y) {
        float t = ((x - x1) * dx) + ((y - y1) * dy);
        float s = NoiseUtil.clamp(t / length2, 0, 1);
        float ix = x1 + s * dx;
        float iy = y1 + s * dy;
        return dist2(x, y, ix, iy);
    }

    private float getFade(float x, float y) {
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

    private static float dist2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }
}
