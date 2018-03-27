package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Power extends Modifier {

    private final float n;

    public Power(Module source, float n) {
        super(source);
        this.n = n;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return (float) Math.pow(noiseValue, n);
    }

    @Override
    public String getName() {
        return "pow";
    }

    @Override
    public void toNode(Node node) {
        node.set("module", getName());
        node.set("n", n);
        source.toNode(node.node("source"));
    }
}
