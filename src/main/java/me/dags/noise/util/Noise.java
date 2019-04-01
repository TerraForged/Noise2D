package me.dags.noise.util;

import me.dags.noise.Module;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.func.Interpolation;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class Noise {

    private final static float F2 = (float) (1.0 / 2.0);
    private final static float G2 = (float) (1.0 / 4.0);

    public static float singlePerlin(float x, float y, int seed, Interpolation interp) {
        int x0 = NoiseUtil.floor(x);
        int y0 = NoiseUtil.floor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs = interp.apply(x - x0);
        float ys = interp.apply(y - y0);

        float xd0 = x - x0;
        float yd0 = y - y0;
        float xd1 = xd0 - 1;
        float yd1 = yd0 - 1;

        float xf0 = NoiseUtil.lerp(NoiseUtil.gradCoord2D(seed, x0, y0, xd0, yd0), NoiseUtil.gradCoord2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = NoiseUtil.lerp(NoiseUtil.gradCoord2D(seed, x0, y1, xd0, yd1), NoiseUtil.gradCoord2D(seed, x1, y1, xd1, yd1), xs);

        return NoiseUtil.lerp(xf0, xf1, ys);
    }

    public static float singleSimplex(float x, float y, int seed, Interpolation interpolation) {
        float t = (x + y) * F2;
        int i = NoiseUtil.floor(x + t);
        int j = NoiseUtil.floor(y + t);

        t = (i + j) * G2;
        float X0 = i - t;
        float Y0 = j - t;

        float x0 = x - X0;
        float y0 = y - Y0;

        int i1, j1;
        if (x0 > y0) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }

        float x1 = x0 - i1 + G2;
        float y1 = y0 - j1 + G2;
        float x2 = x0 - 1 + F2;
        float y2 = y0 - 1 + F2;

        float n0, n1, n2;

        t = 0.5F - x0 * x0 - y0 * y0;
        if (t < 0) {
            n0 = 0;
        } else {
            t *= t;
            n0 = t * t * NoiseUtil.gradCoord2D(seed, i, j, x0, y0);
        }

        t = 0.5F - x1 * x1 - y1 * y1;
        if (t < 0) {
            n1 = 0;
        } else {
            t *= t;
            n1 = t * t * NoiseUtil.gradCoord2D(seed, i + i1, j + j1, x1, y1);
        }

        t = 0.5F - x2 * x2 - y2 * y2;
        if (t < 0) {
            n2 = 0;
        } else {
            t *= t;
            n2 = t * t * NoiseUtil.gradCoord2D(seed, i + 1, j + 1, x2, y2);
        }

        return 50 * (n0 + n1 + n2);
    }

    public static float singleCubic(float x, float y, int seed) {
        int x1 = NoiseUtil.floor(x);
        int y1 = NoiseUtil.floor(y);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;

        float xs = x - (float) x1;
        float ys = y - (float) y1;

        return NoiseUtil.cubicLerp(
                NoiseUtil.cubicLerp(NoiseUtil.valCoord2D(seed, x0, y0), NoiseUtil.valCoord2D(seed, x1, y0), NoiseUtil.valCoord2D(seed, x2, y0), NoiseUtil.valCoord2D(seed, x3, y0), xs),
                NoiseUtil.cubicLerp(NoiseUtil.valCoord2D(seed, x0, y1), NoiseUtil.valCoord2D(seed, x1, y1), NoiseUtil.valCoord2D(seed, x2, y1), NoiseUtil.valCoord2D(seed, x3, y1), xs),
                NoiseUtil.cubicLerp(NoiseUtil.valCoord2D(seed, x0, y2), NoiseUtil.valCoord2D(seed, x1, y2), NoiseUtil.valCoord2D(seed, x2, y2), NoiseUtil.valCoord2D(seed, x3, y2), xs),
                NoiseUtil.cubicLerp(NoiseUtil.valCoord2D(seed, x0, y3), NoiseUtil.valCoord2D(seed, x1, y3), NoiseUtil.valCoord2D(seed, x2, y3), NoiseUtil.valCoord2D(seed, x3, y3), xs), ys)
                * NoiseUtil.CUBIC_2D_BOUNDING;
    }

    public static float cell(float x, float y, int seed, CellFunc cellFunc, DistanceFunc distanceFunc, Module lookup) {
        int xr = NoiseUtil.round(x);
        int yr = NoiseUtil.round(y);

        int cellX = 0;
        int cellY = 0;
        float distance = Float.MAX_VALUE;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int xi = xr + dx;
                int yi = yr + dy;
                Vec2f vec = NoiseUtil.CELL_2D[NoiseUtil.hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;
                float newDistance = distanceFunc.apply(vecX, vecY);
                if (newDistance < distance) {
                    distance = newDistance;
                    cellX = xi;
                    cellY = yi;
                }
            }
        }

        return cellFunc.apply(cellX, cellY, distance, seed, lookup);
    }

    public static float singleCellular(float x, float y, int seed, CellFunc cellFunc, DistanceFunc distanceFunc, Module lookup) {
        int xr = NoiseUtil.round(x);
        int yr = NoiseUtil.round(y);

        int cellX = 0;
        int cellY = 0;
        float distance = Float.MAX_VALUE;

        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int xi = xr + dx;
                int yi = yr + dy;
                Vec2f vec = NoiseUtil.CELL_2D[NoiseUtil.hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;
                float newDistance = distanceFunc.apply(vecX, vecY);
                if (newDistance < distance) {
                    distance = newDistance;
                    cellX = xi;
                    cellY = yi;
                }
            }
        }

        return cellFunc.apply(cellX, cellY, distance, seed, lookup);
    }

    public static float singleCellular2Edge(float x, float y, int seed, EdgeFunc edgeFunc, DistanceFunc distanceFunc) {
        int xr = NoiseUtil.round(x);
        int yr = NoiseUtil.round(y);

        float distance = 999999;
        float distance2 = 999999;

        for (int xi = xr - 1; xi <= xr + 1; xi++) {
            for (int yi = yr - 1; yi <= yr + 1; yi++) {
                Vec2f vec = NoiseUtil.CELL_2D[NoiseUtil.hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;
                float newDistance = distanceFunc.apply(vecX, vecY);

                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                distance = Math.min(distance, newDistance);
            }
        }

        return edgeFunc.apply(distance, distance2);
    }
}
