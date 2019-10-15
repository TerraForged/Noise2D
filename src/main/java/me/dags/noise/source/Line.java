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
    private final Module fadeIn;
    private final Module fadeOut;
    private final Module radius;

    public Line(float x1, float y1, float x2, float y2, Module radius2, Module fadeIn, Module fadeOut) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.dx = x2 - x1;
        this.dy = y2 - y1;
        this.length2 = dx * dx + dy * dy;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.radius = radius2;
    }

    @Override
    public float getValue(float x, float y) {
        float dist2 = getDistance2(x, y);
        float radius2 = radius.getValue(x, y);
        if (dist2 > radius2) {
            return 0;
        }
        float value = dist2 / radius2;
        float fade = getFade(x, y);
        return (1 - value) * fade;
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
        float d2 = dist2(x, y, x2, y2);

        final float dist;
        final float fade;
        if (d1 < d2) {
            dist = d1;
            fade = fadeIn.getValue(x, y);
        } else if (d2 < d1) {
            dist = d2;
            fade = fadeOut.getValue(x, y);
        } else {
            return 1F;
        }

        float fadeLength = length2 * fade;
        if (dist > fadeLength) {
            return 1;
        }

        return dist / fadeLength;
    }

    private static float dist2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }
}
