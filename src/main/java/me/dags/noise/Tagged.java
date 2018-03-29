package me.dags.noise;

import me.dags.noise.cache.TagCache;
import me.dags.noise.func.Interpolation;
import me.dags.noise.tag.TagBase;
import me.dags.noise.tag.TagCombiner;
import me.dags.noise.util.Util;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public interface Tagged<T> extends Module {

    @Override
    TagCache<T> getCache();

    List<T> getTags();

    default List<T> getTags(float x, float y) {
        if (!getCache().isCached(x, y)) {
            getValue(x, y);
        }
        return getCache().getTags();
    }

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
