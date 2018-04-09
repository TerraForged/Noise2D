package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.combiner.selector.Select;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class TagSelect<T> extends Select implements Tagged<T> {

    private final List<T> mix;

    public TagSelect(Module control, Tagged<T> source0, Tagged<T> source1, List<T> mix, float lower, float upper, float falloff, Interpolation interpolation) {
        super(control, source0, source1, lower, upper, falloff, interpolation);
        this.mix = mix;
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getTags(float x, float y) {
        return (List<T>) super.getTags();
    }
}
