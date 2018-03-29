package me.dags.noise.modifier;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;

/**
 * @author dags <dags@dags.me>
 *
 * Modifiers alter the output of one or more Modules
 */
public abstract class Modifier implements Module {

    protected final Module source;

    public Modifier(Module source) {
        this.source = source;
    }

    @Override
    public final float getValue(float x, float y) {
        if (!getCache().isCached(x, y)) {
            float value = value(x, y);
            return getCache().cacheValue(x, y, value);
        }
        return getCache().getValue();
    }

    @Override
    public Cache getCache() {
        return source.getCache();
    }

    @Override
    public float minValue() {
        return source.minValue();
    }

    @Override
    public float maxValue() {
        return source.maxValue();
    }

    @Override
    public void toNode(Node node) {
        node.clear();
        node.set("module", getName());
        source.toNode(node.node("getSource"));
    }

    @Override
    public String toString() {
        return getName() + "{" + source + "}";
    }

    protected float value(float x, float y) {
        float value = source.getValue(x, y);
        return modify(x, y, value);
    }

    public abstract float modify(float x, float y, float noiseValue);
}
