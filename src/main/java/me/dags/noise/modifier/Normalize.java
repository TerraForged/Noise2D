package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Normalize extends Map {

    public Normalize(Module source) {
        super(source, 0F, 1F);
    }

    @Override
    public String getName() {
        return "norm";
    }

    @Override
    public float getValue(float x, float y) {
        return Math.min(1F, Math.max(0F, super.getValue(x, y)));
    }

    @Override
    public void toNode(Node node) {
        node.set("module", getName());
        source.toNode(node.node("source"));
    }
}
