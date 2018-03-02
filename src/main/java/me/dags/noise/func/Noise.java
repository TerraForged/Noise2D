package me.dags.noise.func;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class Noise {

    public static float calculateFractalBounding(int octaves, float gain) {
        float amp = gain;
        float ampFractal = 1;
        for (int i = 1; i < octaves; i++) {
            ampFractal += amp;
            amp *= gain;
        }
        return 1 / ampFractal;
    }

    public static float singlePerlin(float x, float y, int seed, Interpolation interp) {
        int x0 = NoiseUtil.FastFloor(x);
        int y0 = NoiseUtil.FastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs = interp.apply(x - x0);
        float ys = interp.apply(y - y0);

        float xd0 = x - x0;
        float yd0 = y - y0;
        float xd1 = xd0 - 1;
        float yd1 = yd0 - 1;

        float xf0 = NoiseUtil.Lerp(NoiseUtil.GradCoord2D(seed, x0, y0, xd0, yd0), NoiseUtil.GradCoord2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = NoiseUtil.Lerp(NoiseUtil.GradCoord2D(seed, x0, y1, xd0, yd1), NoiseUtil.GradCoord2D(seed, x1, y1, xd1, yd1), xs);

        return NoiseUtil.Lerp(xf0, xf1, ys);
    }

    public static  float singleCubic(float x, float y, int seed) {
        int x1 = NoiseUtil.FastFloor(x);
        int y1 = NoiseUtil.FastFloor(y);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;

        float xs = x - (float) x1;
        float ys = y - (float) y1;

        return NoiseUtil.CubicLerp(
                NoiseUtil.CubicLerp(NoiseUtil.ValCoord2D(seed, x0, y0), NoiseUtil.ValCoord2D(seed, x1, y0), NoiseUtil.ValCoord2D(seed, x2, y0), NoiseUtil.ValCoord2D(seed, x3, y0), xs),
                NoiseUtil.CubicLerp(NoiseUtil.ValCoord2D(seed, x0, y1), NoiseUtil.ValCoord2D(seed, x1, y1), NoiseUtil.ValCoord2D(seed, x2, y1), NoiseUtil.ValCoord2D(seed, x3, y1), xs),
                NoiseUtil.CubicLerp(NoiseUtil.ValCoord2D(seed, x0, y2), NoiseUtil.ValCoord2D(seed, x1, y2), NoiseUtil.ValCoord2D(seed, x2, y2), NoiseUtil.ValCoord2D(seed, x3, y2), xs),
                NoiseUtil.CubicLerp(NoiseUtil.ValCoord2D(seed, x0, y3), NoiseUtil.ValCoord2D(seed, x1, y3), NoiseUtil.ValCoord2D(seed, x2, y3), NoiseUtil.ValCoord2D(seed, x3, y3), xs), ys)
                * NoiseUtil.CUBIC_2D_BOUNDING;
    }

    public static float singleCellular(float x, float y, int seed, CellFunc cellFunc, DistanceFunc distanceFunc, Module lookup) {
        int xr = NoiseUtil.FastRound(x);
        int yr = NoiseUtil.FastRound(y);

        float distance = 999999;
        int xc = 0, yc = 0;

        for (int xi = xr - 1; xi <= xr + 1; xi++) {
            for (int yi = yr - 1; yi <= yr + 1; yi++) {
                NoiseUtil.Float2 vec = NoiseUtil.CELL_2D[NoiseUtil.Hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;
                float newDistance = distanceFunc.apply(vecX, vecY);

                if (newDistance < distance) {
                    distance = newDistance;
                    xc = xi;
                    yc = yi;
                }
            }
        }

        return cellFunc.apply(xc, yc, distance, seed, lookup);
    }

    public static float singleCellular2Edge(float x, float y, int seed, EdgeFunc edgeFunc, DistanceFunc distanceFunc) {
        int xr = NoiseUtil.FastRound(x);
        int yr = NoiseUtil.FastRound(y);

        float distance = 999999;
        float distance2 = 999999;

        for (int xi = xr - 1; xi <= xr + 1; xi++) {
            for (int yi = yr - 1; yi <= yr + 1; yi++) {
                NoiseUtil.Float2 vec = NoiseUtil.CELL_2D[NoiseUtil.Hash2D(seed, xi, yi) & 255];

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
