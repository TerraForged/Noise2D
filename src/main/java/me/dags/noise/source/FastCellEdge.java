package me.dags.noise.source;

import me.dags.noise.Builder;
import me.dags.noise.source.fast.CellDistanceFunc;
import me.dags.noise.source.fast.CellType;
import me.dags.noise.source.fast.Noise;

/**
 * @author dags <dags@dags.me>
 */
public class FastCellEdge extends FastSource {

    private final CellType cellType;
    private final CellDistanceFunc cellDistance;

    public FastCellEdge(Builder builder) {
        super(builder);
        this.cellType = builder.cellType();
        this.cellDistance = builder.cellDistance();
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        return Noise.singleCellular2Edge(x, y, seed, cellType, cellDistance);
    }
}
