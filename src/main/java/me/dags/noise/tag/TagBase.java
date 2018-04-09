package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Tagged;
import me.dags.noise.combiner.selector.Base;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class TagBase<T> extends Base implements Tagged<T> {

    private final List<T> mix;

    public TagBase(Tagged<T> lower, Tagged<T> upper, List<T> mix, float falloff, Interpolation interpolation) {
        super(lower, upper, falloff, interpolation);
        this.mix = mix;
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getTags(float x, float y) {
        return (List<T>) super.getTags(x, y);
    }
}
