package me.dags.noise;

import java.util.List;
import me.dags.noise.func.Interpolation;
import me.dags.noise.tag.TagBase;
import me.dags.noise.tag.TagCombiner;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public interface Tagged<T> extends Module {

    @Override
    List<T> getTags();

    @Override
    List<T> getTags(float x, float y);

    default Tagged<T> tagBase(Tagged<T> upper, double falloff) {
        return tagBase(upper, falloff, Interpolation.LINEAR);
    }

    default Tagged<T> tagBase(Tagged<T> upper, List<T> mix, double falloff) {
        return tagBase(upper, mix, falloff, Interpolation.LINEAR);
    }

    default Tagged<T> tagBase(Tagged<T> upper, double falloff, Interpolation interpolation) {
        return tagBase(upper, Util.combine(getTags(), upper.getTags()), (float) falloff, interpolation);
    }

    default Tagged<T> tagBase(Tagged<T> upper, List<T> mix, double falloff, Interpolation interpolation) {
        return new TagBase<>(this, upper, mix, (float) falloff, interpolation);
    }

    default Tagged<T> tagCombine(Module source) {
        return new TagCombiner<>(source, this);
    }
}
