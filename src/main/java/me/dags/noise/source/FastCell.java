package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.Module;
import me.dags.noise.source.fast.CellDistanceFunc;
import me.dags.noise.source.fast.CellType;
import me.dags.noise.source.fast.Noise;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCell extends FastSource {

    private final Module lookup;
    private final CellType cellType;
    private final CellDistanceFunc cellDistance;

    public FastCell(Builder builder) {
        super(builder);
        cellType = builder.cellType();
        cellDistance = builder.cellDistance();
        lookup = builder.source();
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        return Noise.singleCellular(x, y, seed, cellType, cellDistance, lookup);
    }

    @Override
    public Builder toBuilder() {
        return super.toBuilder()
                .source(lookup)
                .cellType(cellType)
                .cellDistance(cellDistance);
    }
}
