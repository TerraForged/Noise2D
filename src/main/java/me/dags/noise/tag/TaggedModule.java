package me.dags.noise.tag;

import com.google.common.collect.ImmutableList;
import java.util.List;
import me.dags.config.Node;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class TaggedModule<T> implements Tagged<T> {

    private final Module source;
    private final List<T> tags;

    public TaggedModule(Module source, List<T> tags) {
        this.source = source;
        this.tags = ImmutableList.copyOf(tags);
    }

    public List<T> getTags() {
        return tags;
    }

    public List<T> getTags(float x, float z) {
        return tags;
    }

    @Override
    public String getName() {
        return "tag_module";
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
