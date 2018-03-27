package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Builder;
import me.dags.noise.Module;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.Noise;
import me.dags.noise.util.Util;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public class FastCell extends FastSource {

    private final Module lookup;
    private final CellFunc cellFunc;
    private final DistanceFunc distFunc;

    public FastCell(Builder builder) {
        super(builder);
        lookup = builder.source();
        cellFunc = builder.cellFunc();
        distFunc = builder.distFunc();
    }

    @Override
    public String getName() {
        return "cell";
    }

    @Override
    public float getValue(float x, float y) {
        x *= frequency;
        y *= frequency;
        float value = Noise.singleCellular(x, y, seed, cellFunc, distFunc, lookup);
        return Noise.map(value, -1, 1, 2);
    }

    @Override
    public Builder toBuilder() {
        return super.toBuilder()
                .source(lookup)
                .cellFunc(cellFunc)
                .distFunc(distFunc);
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        Util.setNonDefault(node, "cell", cellFunc, Builder.CELL_FUNC);
        Util.setNonDefault(node, "dist", distFunc, Builder.DIST_FUNC);
        if (lookup != Builder.SOURCE) {
            lookup.toNode(node.node("source"));
        }
    }

    @Override
    public String toString() {
        return getName() + "{"
                + properties()
                + ", cell=" + cellFunc
                + ", dist=" + distFunc
                + "}";
    }
}
