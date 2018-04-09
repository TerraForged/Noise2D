package me.dags.noise.source;

import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.util.Noise;
import me.dags.noise.util.NoiseUtil;

/**
 * @author dags <dags@dags.me>
 */
public class FastCellEdge extends FastSource {

    private final EdgeFunc edgeFunc;
    private final DistanceFunc distFunc;

    public FastCellEdge(Builder builder) {
        super(builder);
        this.edgeFunc = builder.getEdgeFunc();
        this.distFunc = builder.getDistFunc();
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        float value = Noise.singleCellular2Edge(x, y, seed, edgeFunc, distFunc);
        return NoiseUtil.map(value, edgeFunc.min(), edgeFunc.max(), edgeFunc.range());
    }
}
