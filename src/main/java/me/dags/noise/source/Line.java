package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;
import me.dags.noise.util.Vec2i;

public class Line implements Module {

    private final int x1;
    private final int y1;
    private final int dx;
    private final int dy;
    private final float length2;
    private final Module maxDistance;

    public Line(Vec2i pos1, Vec2i pos2, Module maxDistance) {
        this(pos1.x, pos1.y, pos2.x, pos2.y, maxDistance);
    }

    public Line(int x1, int y1, int x2, int y2, Module maxDistance) {
        this.x1 = x1;
        this.y1 = y1;
        this.dx = x2 - x1;
        this.dy = y2 - y1;
        this.length2 = dx * dx + dy * dy;
        this.maxDistance = maxDistance;
    }

    @Override
    public float getValue(float x, float y) {
        float dist2 = getDistance2(x, y);
        float max = maxDistance.getValue(x, y);
        float max2 = max * max;
        if (dist2 > max2) {
            dist2 = max2;
        }
        return 1 - (dist2 / max2);
    }

    public float getDistance2(float x, float y) {
        float t = ((x - x1) * dx) + ((y - y1) * dy);
        float s = NoiseUtil.clamp(t / length2, 0, 1);
        float ix = x1 + s * dx;
        float iy = y1 + s * dy;
        return dist2(x, y, ix, iy);
    }

    private static float dist2(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return dx * dx + dy * dy;
    }
}
