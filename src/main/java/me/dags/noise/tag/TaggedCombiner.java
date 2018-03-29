package me.dags.noise.tag;

import java.util.List;
import me.dags.config.Node;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class TaggedCombiner<T> implements Tagged<T> {

    private final Module source;
    private final Tagged<T> tagSource;

    public TaggedCombiner(Module source, Tagged<T> tagSource) {
        this.source = source;
        this.tagSource = tagSource;
    }

    @Override
    public List<T> getTags() {
        return tagSource.getTags();
    }

    @Override
    public List<T> getTags(float x, float y) {
        return tagSource.getTags(x, y);
    }

    @Override
    public String getName() {
        return "tag_combine";
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
