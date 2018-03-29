package me.dags.noise.tag;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagCombiner<T> implements Tagged<T> {

    private final Module source;
    private final Tagged<T> tagSource;
    private final TagCache<T> cache = new TagCache<>();

    public TagCombiner(Module source, Tagged<T> tagSource) {
        this.source = source;
        this.tagSource = tagSource;
    }

    @Override
    public float getValue(float x, float y) {
        if (!cache.isCached(x, y)) {
            float value = source.getValue(x, y);
            return getCache().cacheValue(x, y, value);
        }
        return cache.getValue();
    }

    @Override
    public TagCache<T> getCache() {
        return cache;
    }

    @Override
    public List<T> getTags() {
        return tagSource.getTags();
    }

    @Override
    public String getName() {
        return "tag_combine_" + source.getName();
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

    }
}
