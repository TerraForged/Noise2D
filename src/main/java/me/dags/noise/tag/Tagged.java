package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public interface Tagged<T> extends Module {

    List<T> getTags();

    List<T> getTags(float x, float y);

    default Tagged<T> tagBase(Tagged<T> upper, double falloff) {
        return new TaggedBase<>(this, upper, (float) falloff);
    }

    default Tagged<T> tagCombine(Module source) {
        return new TaggedCombiner<>(source, this);
    }
}
