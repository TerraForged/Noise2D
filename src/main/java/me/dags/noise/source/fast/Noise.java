package me.dags.noise.source.fast;

import me.dags.noise.Module;

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
        int x0 = Util.FastFloor(x);
        int y0 = Util.FastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs = interp.apply(x0, x);
        float ys = interp.apply(y0, y);

        float xd0 = x - x0;
        float yd0 = y - y0;
        float xd1 = xd0 - 1;
        float yd1 = yd0 - 1;

        float xf0 = Util.Lerp(Util.GradCoord2D(seed, x0, y0, xd0, yd0), Util.GradCoord2D(seed, x1, y0, xd1, yd0), xs);
        float xf1 = Util.Lerp(Util.GradCoord2D(seed, x0, y1, xd0, yd1), Util.GradCoord2D(seed, x1, y1, xd1, yd1), xs);

        return Util.Lerp(xf0, xf1, ys);
    }

    public static float singleValue(float x, float y, int seed, Interpolation interpolation) {
        int x0 = Util.FastFloor(x);
        int y0 = Util.FastFloor(y);
        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float xs = interpolation.apply(x0, x);
        float ys = interpolation.apply(y0, y);

        float xf0 = Util.Lerp(Util.ValCoord2D(seed, x0, y0), Util.ValCoord2D(seed, x1, y0), xs);
        float xf1 = Util.Lerp(Util.ValCoord2D(seed, x0, y1), Util.ValCoord2D(seed, x1, y1), xs);

        return Util.Lerp(xf0, xf1, ys);
    }

    public static  float singleCubic(float x, float y, int seed) {
        int x1 = Util.FastFloor(x);
        int y1 = Util.FastFloor(y);

        int x0 = x1 - 1;
        int y0 = y1 - 1;
        int x2 = x1 + 1;
        int y2 = y1 + 1;
        int x3 = x1 + 2;
        int y3 = y1 + 2;

        float xs = x - (float) x1;
        float ys = y - (float) y1;

        return Util.CubicLerp(
                Util.CubicLerp(Util.ValCoord2D(seed, x0, y0), Util.ValCoord2D(seed, x1, y0), Util.ValCoord2D(seed, x2, y0), Util.ValCoord2D(seed, x3, y0), xs),
                Util.CubicLerp(Util.ValCoord2D(seed, x0, y1), Util.ValCoord2D(seed, x1, y1), Util.ValCoord2D(seed, x2, y1), Util.ValCoord2D(seed, x3, y1), xs),
                Util.CubicLerp(Util.ValCoord2D(seed, x0, y2), Util.ValCoord2D(seed, x1, y2), Util.ValCoord2D(seed, x2, y2), Util.ValCoord2D(seed, x3, y2), xs),
                Util.CubicLerp(Util.ValCoord2D(seed, x0, y3), Util.ValCoord2D(seed, x1, y3), Util.ValCoord2D(seed, x2, y3), Util.ValCoord2D(seed, x3, y3), xs), ys)
                * Util.CUBIC_2D_BOUNDING;
    }

    public static float singleCellular(float x, float y, int seed, CellType cellType, CellDistanceFunc cellDistance, Module lookup) {
        int xr = Util.FastRound(x);
        int yr = Util.FastRound(y);

        float distance = 999999;
        int xc = 0, yc = 0;

        for (int xi = xr - 1; xi <= xr + 1; xi++) {
            for (int yi = yr - 1; yi <= yr + 1; yi++) {
                Util.Float2 vec = Util.CELL_2D[Util.Hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;

                float newDistance = cellDistance.apply(vecX, vecY);

                if (newDistance < distance) {
                    distance = newDistance;
                    xc = xi;
                    yc = yi;
                }
            }
        }

        switch (cellType) {
            case CellValue:
                return Util.ValCoord2D(0, xc, yc);
            case NoiseLookup:
                Util.Float2 vec = Util.CELL_2D[Util.Hash2D(seed, xc, yc) & 255];
                return lookup.getValue(xc + vec.x, yc + vec.y);
            case Distance:
                return distance - 1;
            default:
                return 0;
        }
    }

    public static float singleCellular2Edge(float x, float y, int seed, CellType cellType, CellDistanceFunc cellDistance) {
        int xr = Util.FastRound(x);
        int yr = Util.FastRound(y);

        float distance = 999999;
        float distance2 = 999999;

        for (int xi = xr - 1; xi <= xr + 1; xi++) {
            for (int yi = yr - 1; yi <= yr + 1; yi++) {
                Util.Float2 vec = Util.CELL_2D[Util.Hash2D(seed, xi, yi) & 255];

                float vecX = xi - x + vec.x;
                float vecY = yi - y + vec.y;

                float newDistance = cellDistance.apply(vecX, vecY);

                distance2 = Math.max(Math.min(distance2, newDistance), distance);
                distance = Math.min(distance, newDistance);
            }
        }

        return cellType.apply(distance, distance2);
    }
}
