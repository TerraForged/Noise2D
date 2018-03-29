package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.cache.Cache;

/**
 * @author dags <dags@dags.me>
 */
public class Constant implements Source {

    private final float value;

    public Constant(float value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return "const";
    }

    @Override
    public float getValue(float x, float y) {
        return value;
    }

    @Override
    public float minValue() {
        return value;
    }

    @Override
    public float maxValue() {
        return value;
    }

    @Override
    public Builder toBuilder() {
        return Module.builder();
    }

    @Override
    public void toNode(Node node) {
        node.set("type", getName());
        node.set("value", value);
    }

    @Override
    public Cache getCache() {
        return Cache.NONE;
    }
}
