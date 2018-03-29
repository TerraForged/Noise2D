package me.dags.noise.tag;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.combiner.Select;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public class TaggedSelect<T> extends Select implements Tagged<T> {

    private final Tagged<T> source0;
    private final Tagged<T> source1;
    private final List<T> mix;

    public TaggedSelect(Module selector, Tagged<T> t0, Tagged<T> t1, float lower, float upper, float falloff) {
        super(selector, t0, t1, lower, upper, falloff, Interpolation.LINEAR);
        source0 = t0;
        source1 = t1;
        mix = Util.combine(t0.getTags(), t1.getTags());
    }

    @Override
    public String getName() {
        return "tag_select";
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    public List<T> getTags(float x, float y) {
        float controlValue = control.getValue(x, y);
        if (edgeFalloff > 0.0) {
            if (controlValue < lowerBoundMin) {
                return source0.getTags(x, y);
            } else if (controlValue < lowerBoundMax) {
                return getMix(source0, source1, x, y, controlValue, lowerCurve0, upperCurve0);
            } else if (controlValue < upperBoundMin) {
                return source1.getTags(x, y);
            } else if (controlValue < upperBoundMax) {
                return getMix(source1, source0, x, y, controlValue, lowerCurve1, upperCurve1);
            } else {
                return source0.getTags(x, y);
            }
        } else if (controlValue < lowerBound || controlValue > upperBound) {
            return source0.getTags(x, y);
        } else {
            return source1.getTags(x, y);
        }
    }

    private List<T> getMix(Tagged<T> a, Tagged<T> b, float x, float y, float control, float lower, float upper) {
        double alpha = curve((control - lower) / (upper - lower));
        if (alpha < 0) {
            return mix;
        }
        if (alpha < 0.5) {
            return a.getTags(x, y);
        }
        return b.getTags(x, y);
    }
}
