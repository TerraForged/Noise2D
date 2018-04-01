package me.dags.noise.combiner.selector;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.cache.Cache;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

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
        super(control, new Module[]{source0, source1}, Cache.NONE, interpolation);
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
    public String getName() {
        return "select";
    }

    @Override
    public void toNode(Node node) {
        super.toNode(node);
        node.set("bound0", Util.round5(lowerBound));
        node.set("bound1", Util.round5(upperBound));
        node.set("falloff", Util.round5(edgeFalloff));
    }

    @Override
    public float selectValue(float x, float y, float value) {
        if (edgeFalloff == 0) {
            if (value < lowerCurveMax) {
                return selectOne(source0, 0,x, y);
            }

            if (value > upperCurveMin) {
                return selectOne(source1, 1, x, y);
            }

            return selectOne(source0, 0, x, y);
        }

        if (value < lowerCurveMin) {
            return selectOne(source0, 0, x, y);
        }

        // curve
        if (value < lowerCurveMax) {
            float alpha = (value - lowerCurveMin) / lowerCurveRange;
            return selectTwo(source0, source1, 0, 1, x, y, alpha);
        }

        if (value < upperCurveMin) {
            return selectOne(source1, 1, x, y);
        }

        if (value < upperCurveMax) {
            float alpha = (value - upperCurveMin) / upperCurveRange;
            return selectTwo(source1, source0, 1, 0, x, y, alpha);
        }

        return selectOne(source0, 0, x, y);
    }

    @Override
    public String toString() {
        return getName() + "{"
                + "selector=" + control
                + ",source0=" + source0
                + ",source1=" + source1
                + "}";
    }
}
