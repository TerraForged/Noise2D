package me.dags.noise.combiner.selector;

import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Select extends Selector {

    public static final Interpolation INTERPOLATION = Interpolation.CURVE3;

    protected final Module control;
    protected final Module source0;
    protected final Module source1;

    protected final float lowerBound;
    protected final float upperBound;
    protected final float edgeFalloff;
    protected final float lowerCurveMin;
    protected final float lowerCurveMax;
    protected final float lowerCurveRange;
    protected final float upperCurveMin;
    protected final float upperCurveMax;
    protected final float upperCurveRange;

    public Select(Module control, Module source0, Module source1, float lowerBound, float upperBound, float edgeFalloff, Interpolation interpolation) {
        super(control, new Module[]{source0, source1}, interpolation);
        this.control = control;
        this.source0 = source0;
        this.source1 = source1;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.edgeFalloff = edgeFalloff;
        this.lowerCurveMin = lowerBound - edgeFalloff;
        this.lowerCurveMax = lowerBound + edgeFalloff;
        this.lowerCurveRange = lowerCurveMax - lowerCurveMin;
        this.upperCurveMin = upperBound - edgeFalloff;
        this.upperCurveMax = upperBound + edgeFalloff;
        this.upperCurveRange = upperCurveMax - upperCurveMin;
    }

    @Override
    public float selectValue(float x, float y, float value) {
        if (edgeFalloff == 0) {
            if (value < lowerCurveMax) {
                return source0.getValue(x, y);
            }

            if (value > upperCurveMin) {
                return source1.getValue(x, y);
            }

            return source0.getValue(x, y);
        }

        if (value < lowerCurveMin) {
            return source0.getValue(x, y);
        }

        // curve
        if (value < lowerCurveMax) {
            float alpha = (value - lowerCurveMin) / lowerCurveRange;
            return blendValues(source0.getValue(x, y), source1.getValue(x, y), alpha);
        }

        if (value < upperCurveMin) {
            return source1.getValue(x, y);
        }

        if (value < upperCurveMax) {
            float alpha = (value - upperCurveMin) / upperCurveRange;
            return blendValues(source1.getValue(x, y), source0.getValue(x, y), alpha);
        }

        return source0.getValue(x, y);
    }

    @Override
    public List<?> selectTags(float x, float y, float value) {
        if (edgeFalloff == 0) {
            if (value < lowerCurveMax) {
                return source0.getTags(x, y);
            }

            if (value > upperCurveMin) {
                return source1.getTags(x, y);
            }

            return source0.getTags(x, y);
        }

        if (value < lowerCurveMin) {
            return source0.getTags(x, y);
        }

        // curve
        if (value < lowerCurveMax) {
            return getTags();
        }

        if (value < upperCurveMin) {
            return source1.getTags(x, y);
        }

        if (value < upperCurveMax) {
            return getTags();
        }

        return source0.getTags(x, y);
    }
}
