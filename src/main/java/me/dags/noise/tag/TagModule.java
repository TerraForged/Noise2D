package me.dags.noise.tag;

import com.google.common.collect.ImmutableList;
import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagModule<T> implements Tagged<T> {

    private final Module source;
    private final List<T> tags;
    private final TagCache<T> cache = TagCache.empty();

    public TagModule(Module source, List<T> tags) {
        this.source = source;
        this.tags = ImmutableList.copyOf(tags);
    }

    @Override
    public String getName() {
        return "tag_" + source.getName();
    }

    @Override
    public TagCache<T> getCache() {
        return cache;
    }

    @Override
    public List<T> getTags() {
        return tags;
    }

    @Override
    public List<T> getTags(float x, float z) {
        return tags;
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
    public float getValue(float x, float y) {
        return source.getValue(x, y);
    }

    @Override
    public void toNode(Node node) {

    }
}
