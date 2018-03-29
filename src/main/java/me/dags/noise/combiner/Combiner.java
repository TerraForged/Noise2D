package me.dags.noise.combiner;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author dags <dags@dags.me>
 *
 * Combiners output values that may be the result of one or more input Modules
 */
public abstract class Combiner implements Module {

    private final float min;
    private final float max;
    private final Module[] sources;
    private final Cache cache;

    public Combiner(Cache cache, Module... sources) {
        float min = 0F;
        float max = 0F;
        if (sources.length > 0) {
            min = sources[0].minValue();
            max = sources[0].maxValue();
            for (int i = 1; i < sources.length; i++) {
                Module next = sources[i];
                min = minTotal(min, next);
                max = maxTotal(max, next);
            }
        }
        this.min = min;
        this.max = max;
        this.cache = cache;
        this.sources = sources;
    }

    @Override
    public void toNode(Node node) {
        node.clear();
        List<Node> nodes = new LinkedList<>();
        for (Module module : sources) {
            Node n = Node.create();
            module.toNode(n);
            nodes.add(n);
        }
        node.set("module", getName());
        node.node("sources").set(nodes);
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
    public Cache getCache() {
        return cache;
    }

    @Override
    public float getValue(float x, float y) {
        if (!getCache().isCached(x, y)) {
            float result = 0F;
            if (sources.length > 0) {
                result = sources[0].getValue(x, y);
                for (int i = 1; i < sources.length; i++) {
                    Module module = sources[i];
                    float value = module.getValue(x, y);
                    result = combine(result, value);
                }
            }
            return getCache().cacheValue(x, y, result);
        }
        return getCache().getValue();
    }

    @Override
    public String toString() {
        return getName() + "{"
                + "sources=" + Arrays.toString(sources)
                + "}";
    }

    protected abstract float minTotal(float result, Module next);

    protected abstract float maxTotal(float result, Module next);

    protected abstract float combine(float result, float value);
}
