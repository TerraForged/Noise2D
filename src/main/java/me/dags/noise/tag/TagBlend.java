package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.combiner.selector.Blend;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class TagBlend<T> extends Blend implements Tagged<T> {

    private final List<T> mix;

    public TagBlend(Module selector, Tagged<T> source0, Tagged<T> source1, List<T> mix, float midPoint, float blendRange, Interpolation interpolation) {
        super(selector, source0, source1, midPoint, blendRange, interpolation);
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
