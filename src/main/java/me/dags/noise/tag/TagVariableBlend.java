package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.combiner.selector.VariableBlend;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class TagVariableBlend<T> extends VariableBlend implements Tagged<T> {

    private final List<T> mix;

    public TagVariableBlend(Module control, Module blend, Tagged<T> source0, Tagged<T> source1, List<T> mix, float midpoint, float minBlend, float maxBlend, Interpolation interpolation) {
        super(control, blend, source0, source1, midpoint, minBlend, maxBlend, interpolation);
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
