package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.combiner.Base;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class TaggedBase<T> extends Base implements Tagged<T> {

    private final Tagged<T> lower;
    private final Tagged<T> upper;
    private final List<T> mix;

    public TaggedBase(Tagged<T> lower, Tagged<T> upper, float falloff) {
        super(lower, upper, falloff);
        this.lower = lower;
        this.upper = upper;
        mix = Util.combine(lower.getTags(), upper.getTags());
    }

    @Override
    public String getName() {
        return "tag_base";
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    public List<T> getTags(float x, float y) {
        float value = upper.getValue(x, y);
        if (value < max) {
            if (falloff > 0) {
                return mix;
            }
            return lower.getTags(x, y);
        }
        return upper.getTags(x, y);
    }
}
