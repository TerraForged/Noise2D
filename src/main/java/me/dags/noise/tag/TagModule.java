package me.dags.noise.tag;

import com.google.common.collect.ImmutableList;
import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.Tagged;

/**
 * @author dags <dags@dags.me>
 */
public class TagModule<T> implements Tagged<T> {

    private final Module source;
    private final List<T> tags;

    public TagModule(Module source, List<T> tags) {
        this.source = source;
        this.tags = ImmutableList.copyOf(tags);
    }

    @Override
    public float getValue(float x, float y) {
        return source.getValue(x, y);
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
}
