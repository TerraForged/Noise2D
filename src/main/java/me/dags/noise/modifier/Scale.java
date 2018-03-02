package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Scale extends Modifier {

    private final float scale;
    private final float min;
    private final float max;

    public Scale(Module source, float scale) {
        super(source);
        this.scale = scale;
        this.min = source.minValue() * scale;
        this.max = source.maxValue() * scale;
    }

    @Override
    public String getName() {
        return "scale";
    }

    @Override
    public float minValue() {
        return min;
    }

    @Override
    public float maxValue() {
        return max;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return noiseValue * scale;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("scale", Util.round5(scale));
    }
}
