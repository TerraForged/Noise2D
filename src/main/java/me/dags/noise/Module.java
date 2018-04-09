package me.dags.noise;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import me.dags.noise.combiner.Add;
import me.dags.noise.combiner.Combiner;
import me.dags.noise.combiner.Max;
import me.dags.noise.combiner.Min;
import me.dags.noise.combiner.Multiply;
import me.dags.noise.combiner.Sub;
import me.dags.noise.combiner.selector.Base;
import me.dags.noise.combiner.selector.Blend;
import me.dags.noise.combiner.selector.MultiBlend;
import me.dags.noise.combiner.selector.Select;
import me.dags.noise.combiner.selector.VariableBlend;
import me.dags.noise.func.Interpolation;
import me.dags.noise.modifier.*;
import me.dags.noise.source.Builder;
import me.dags.noise.tag.TagBlend;
import me.dags.noise.tag.TagModule;
import me.dags.noise.tag.TagMultiBlend;
import me.dags.noise.tag.TagSelect;
import me.dags.noise.tag.TagVariableBlend;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 */
public interface Module {

    Interpolation DEFAULT_INTERP = Interpolation.CURVE3;

    float minValue();

    float maxValue();

    float getValue(float x, float y);

    default List<?> getTags() {
        return Collections.emptyList();
    }

    default List<?> getTags(float x, float z) {
        return Collections.emptyList();
    }

    default float[] generate(int width, int height, int xOffset, int yOffset) {
        return generate(new float[width * height], width, height, xOffset, yOffset);
    }

    default float[] generate(float[] backing, int width, int height, int xOffset, int yOffset) {
        int size = width * height;
        if (size != backing.length) {
            backing = new float[size];
        }

        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                int x = xOffset + dx;
                int y = yOffset + dy;
                int index = dy * width + dx;
                backing[index] = getValue(x, y);
            }
        }

        return backing;
    }

    default Modifier abs() {
        return new Abs(this);
    }

    default Modifier invert() {
        return new Invert(this);
    }

    default Modifier map(double min, double max) {
        return new Map(this, (float) min, (float) max);
    }

    default Modifier clamp(double min, double max) {
        return new Clamp(this, (float) min, (float) max);
    }

    default Modifier scale(double scale) {
        return new Scale(this, (float) scale);
    }

    default Modifier bias(double bias) {
        return new Bias(this, (float) bias);
    }

    default Modifier pow(double n) {
        return new Power(this, (float) n);
    }

    default Modifier steps(int steps) {
        return new Steps(this, steps);
    }

    default Combiner add(Module other) {
        return new Add(this, other);
    }

    default Combiner sub(Module other) {
        return new Sub(this, other);
    }

    default Combiner min(Module other) {
        return new Min(this, other);
    }

    default Combiner max(Module other) {
        return new Max(this, other);
    }

    default Combiner mult(Module other) {
        return new Multiply(this, other);
    }

    default Modifier turb(Module x, Module y, double power) {
        return new Turbulence(this, x, y, (float) power);
    }

    default Combiner base(Module other, double falloff, Interpolation interpolation) {
        return new Base(this, other, (float) falloff, interpolation);
    }

    default Combiner blend(Module source0, Module source1, double midpoint, double blendRange, Interpolation interpolation) {
        return new Blend(this, source0, source1, (float) midpoint, (float) blendRange, interpolation);
    }

    default Combiner multiBlend(double blend, Interpolation interpolation, Module control, Module... sources) {
        return new MultiBlend((float) blend, interpolation, control, sources);
    }

    default Combiner blendVar(Module variable, Module source0, Module source1, double midpoint, double min, double max, Interpolation interpolation) {
        return new VariableBlend(this, variable, source0, source1, (float) midpoint, (float) min, (float) max, interpolation);
    }

    default Combiner select(Module source0, Module source1, double lowerBound, double upperBound, double falloff, Interpolation interpolation) {
        return new Select(this, source0, source1, (float) lowerBound, (float) upperBound, (float) falloff, interpolation);
    }

    default <T> Tagged<T> tag(List<T> tags) {
        return new TagModule<>(this, tags);
    }

    default <T> Tagged<T> tagBlend(Tagged<T> source0, Tagged<T> source1, List<T> mix, double mid, double blend, Interpolation interpolation) {
        return new TagBlend<>(this, source0, source1, mix, (float) mid, (float) blend, interpolation);
    }

    default <T> Tagged<T> tagVarBlend(Module variable, Tagged<T> source0, Tagged<T> source1, List<T> mix, double mid, double min, double max, Interpolation interpolation) {
        return new TagVariableBlend<>(this, variable, source0, source1, mix, (float) mid, (float) min, (float) max, interpolation);
    }

    default <T> Tagged<T> tagMultiBlend(double blend, Interpolation interpolation, List<Tagged<T>> sources) {
        return new TagMultiBlend<>(this, sources, (float) blend, interpolation);
    }

    default <T> Tagged<T> tagSelect(Tagged<T> s0, Tagged<T> s1, List<T> mix, double lower, double upper, double falloff, Interpolation interpolation) {
        return new TagSelect<>(this, s0, s1, mix, (float) lower, (float) upper, (float) falloff, interpolation);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OVERLOADS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    default Modifier turb(Source source, double power) {
        Builder builder = source.toBuilder();
        builder.seed(builder.getSeed() + 1);
        Source source1 = builder.build(source.getClass());
        return turb(source, source1, power);
    }

    default Combiner base(Module other, double falloff) {
        return base(other, (float) falloff, DEFAULT_INTERP);
    }

    default Combiner blend(Module source0, Module source1, double midpoint, double blendRange) {
        return blend(source0, source1, (float) midpoint, (float) blendRange, DEFAULT_INTERP);
    }

    default Combiner multiBlend(double blend, Module control, Module... sources) {
        return multiBlend(blend, DEFAULT_INTERP, control, sources);
    }

    default Combiner blendVar(Module variable, Module source0, Module source1, double midpoint, double min, double max) {
        return blendVar(variable, source0, source1, (float) midpoint, (float) min, (float) max, DEFAULT_INTERP);
    }

    default Combiner select(Module source0, Module source1, double lowerBound, double upperBound, double falloff) {
        return select(source0, source1, lowerBound, upperBound, falloff, DEFAULT_INTERP);
    }

    @SuppressWarnings("unchecked")
    default <T> Tagged<T> tag(T... tag) {
        return tag(Arrays.asList(tag));
    }

    default <T> Tagged<T> tagBlend(Tagged<T> source0, Tagged<T> source1, double mid, double blend) {
        return tagBlend(source0, source1, Util.combine(source0.getTags(), source1.getTags()), mid, blend);
    }

    default <T> Tagged<T> tagBlend(Tagged<T> source0, Tagged<T> source1, List<T> mix, double mid, double blend) {
        return tagBlend(source0, source1, mix, mid, blend, DEFAULT_INTERP);
    }

    default <T> Tagged<T> tagBlend(Tagged<T> source0, Tagged<T> source1, double mid, double blend, Interpolation interpolation) {
        return tagBlend(source0, source1, Util.combine(source0.getTags(), source1.getTags()), mid, blend, interpolation);
    }

    default <T> Tagged<T> tagVarBlend(Module variable, Tagged<T> source0, Tagged<T> source1, double mid, double min, double max) {
        return tagVarBlend(variable, source0, source1, mid, min, max, DEFAULT_INTERP);
    }

    default <T> Tagged<T> tagVarBlend(Module variable, Tagged<T> source0, Tagged<T> source1, List<T> mix, double mid, double min, double max) {
        return tagVarBlend(variable, source0, source1, mix, mid, min, max, DEFAULT_INTERP);
    }

    default <T> Tagged<T> tagVarBlend(Module variable, Tagged<T> source0, Tagged<T> source1, double mid, double min, double max, Interpolation interpolation) {
        return tagVarBlend(variable, source0, source1, Util.combine(source0.getTags(), source1.getTags()), mid, min, max, interpolation);
    }

    @SuppressWarnings("unchecked")
    default <T> Tagged<T> tagMultiBlend(double blend, List<Tagged<T>> sources) {
        return tagMultiBlend(blend, DEFAULT_INTERP, sources);
    }

    default <T> Tagged<T> tagSelect(Tagged<T> s0, Tagged<T> s1, double lower, double upper, double falloff) {
        return tagSelect(s0, s1, Util.combine(s0.getTags(), s1.getTags()), lower, upper, falloff);
    }

    default <T> Tagged<T> tagSelect(Tagged<T> s0, Tagged<T> s1, List<T> mix, double lower, double upper, double falloff) {
        return tagSelect(s0, s1, mix, (float) lower, (float) upper, (float) falloff, DEFAULT_INTERP);
    }

    default <T> Tagged<T> tagSelect(Tagged<T> s0, Tagged<T> s1, double lower, double upper, double falloff, Interpolation interpolation) {
        return tagSelect(s0, s1, Util.combine(s0.getTags(), s1.getTags()), lower, upper, falloff, interpolation);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // STATIC
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    static Builder builder() {
        return new Builder();
    }
}
