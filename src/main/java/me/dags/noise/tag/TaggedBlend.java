package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.combiner.Blend;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class TaggedBlend<T> extends Blend implements Tagged<T> {

    private final Tagged<T> t0;
    private final Tagged<T> t1;
    private final List<T> mix;

    public TaggedBlend(Module control, Tagged<T> source0, Tagged<T> source1) {
        super(control, source0, source1);
        t0 = source0;
        t1 = source1;
        mix = Util.combine(t0.getTags(), t1.getTags());
    }

    @Override
    public String getName() {
        return "tag_blend";
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    public List<T> getTags(float x, float y) {
        float val = control.getValue(x, y);
        if (val == control.minValue()) {
            return t0.getTags(x, y);
        }
        if (val == control.maxValue()) {
            return t1.getTags(x, y);
        }
        return mix;
    }
}
