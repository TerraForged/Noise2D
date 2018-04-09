package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCell extends FastSource {

    private final Module lookup;
    private final CellFunc cellFunc;
    private final DistanceFunc distFunc;
    private final float min;
    private final float max;
    private final float range;

    public FastCell(Builder builder) {
        super(builder);
        lookup = builder.getSource();
        cellFunc = builder.getCellFunc();
        distFunc = builder.getDistFunc();
        min = min(cellFunc, lookup);
        max = max(cellFunc, lookup);
        range = max - min;
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        float value = Noise.singleCellular(x, y, seed, cellFunc, distFunc, lookup);
        return NoiseUtil.map(value, min, max, range);
    }

    @Override
    public Builder toBuilder() {
        return super.toBuilder()
                .source(lookup)
                .cellFunc(cellFunc)
                .distFunc(distFunc);
    }

    private static float min(CellFunc func, Module lookup) {
        if (func == CellFunc.NOISE_LOOKUP) {
            return lookup.minValue();
        }
        if (func == CellFunc.DISTANCE) {
            return -1;
        }
        return -1;
    }

    private static float max(CellFunc func, Module lookup) {
        if (func == CellFunc.NOISE_LOOKUP) {
            return lookup.maxValue();
        }
        if (func == CellFunc.DISTANCE) {
            return 0.25F;
        }
        return 1;
    }
}
