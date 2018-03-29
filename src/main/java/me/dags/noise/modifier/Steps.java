package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class Steps extends Modifier {

    private final float steps;

    public Steps(Module source, int steps) {
        super(source.map(0, 1));
        if (steps == 0) {
            throw new IllegalArgumentException("steps cannot be 0");
        }
        this.steps = steps;
    }

    @Override
    public String getName() {
        return "steps";
    }

    @Override
    public float minValue() {
        return 0F;
    }

    public float maxValue() {
        return 1F;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        int round = NoiseUtil.FastRound(noiseValue * steps);
        return round / steps;
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("count", Util.round5(steps));
    }
}
