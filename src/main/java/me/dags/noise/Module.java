package me.dags.noise;

import me.dags.noise.combiner.*;
import me.dags.noise.func.CurveFunc;
import me.dags.noise.func.Interpolation;
import me.dags.noise.modifier.*;
import me.dags.noise.selector.*;
import me.dags.noise.source.Builder;
import me.dags.noise.source.FastPerlin;
import me.dags.noise.source.FastSource;
import me.dags.noise.util.NoiseUtil;
import me.dags.noise.util.Vec2i;

/**
 * @author dags <dags@dags.me>
 */
public interface Module extends Noise {

    default Module abs() {
        if (this instanceof Abs) {
            return this;
        }
        return new Abs(this);
    }

    default Module add(Module other) {
        return new Add(this, other);
    }

    default Module alpha(double alpha) {
        return alpha(Source.constant(alpha));
    }

    default Module alpha(Module alpha) {
        if (alpha.minValue() < 0 || alpha.maxValue() > 1) {
            return this;
        }
        return new Alpha(this, alpha);
    }

    default Module base(Module other, double falloff) {
        return base(other, falloff, Interpolation.CURVE3);
    }

    default Module base(Module other, double falloff, Interpolation interpolation) {
        return new Base(this, other, (float) falloff, interpolation);
    }

    default Module bias(Module bias) {
        if (bias.minValue() == 0 && bias.maxValue() == 0) {
            return this;
        }
        return new Bias(this, bias);
    }

    default Module bias(double bias) {
        return bias(Source.constant(bias));
    }

    default Module blend(Module source0, Module source1, double midpoint, double blendRange) {
        return blend(source0, source1, midpoint, blendRange, Interpolation.LINEAR);
    }

    default Module blend(Module source0, Module source1, double midpoint, double blendRange, Interpolation interpolation) {
        return new Blend(this, source0, source1, (float) midpoint, (float) blendRange, interpolation);
    }

    default Module blendVar(Module variable, Module source0, Module source1, double midpoint, double min, double max, Interpolation interpolation) {
        return new VariableBlend(this, variable, source0, source1, (float) midpoint, (float) min, (float) max, interpolation);
    }

    default Module cache() {
        if (this instanceof Cache) {
            return this;
        }
        return new Cache(this);
    }

    default Module clamp(Module min, Module max) {
        if (min.minValue() == min.maxValue() && min.minValue() == minValue() && max.minValue() == max.maxValue() && max.maxValue() == maxValue()) {
            return this;
        }
        return new Clamp(this, min, max);
    }

    default Module clamp(double min, double max) {
        return clamp(Source.constant(min), Source.constant(max));
    }

    default Module curve(CurveFunc func) {
        return new Curve(this, func);
    }

    default Module curve(double mid, double steepness) {
        return new Curve(this, new CurveFunc() {
            private final float m = (float) mid;
            private final float s = (float) steepness;
            @Override
            public float apply(float value) {
                return NoiseUtil.curve(value, m, s);
            }
        });
    }

    default Module curve(Module mid, Module steepness) {
        return new VariableCurve(this, mid, steepness);
    }

    default Module grad(double lower, double upper, double strength) {
        return grad(Source.constant(lower), Source.constant(upper), Source.constant(strength));
    }

    default Module grad(Module lower, Module upper, Module strength) {
        return new Grad(this, lower, upper, strength);
    }

    default Module invert() {
        return new Invert(this);
    }

    default Module map(Module min, Module max) {
        if (min.minValue() == min.maxValue() && min.minValue() == minValue() && max.minValue() == max.maxValue() && max.maxValue() == maxValue()) {
            return this;
        }
        return new Map(this, min, max);
    }

    default Module map(double min, double max) {
        return map(Source.constant(min), Source.constant(max));
    }

    default Module max(Module other) {
        return new Max(this, other);
    }

    default Module min(Module other) {
        return new Min(this, other);
    }

    default Module mod(Module direction, Module strength) {
        return new Modulate(this, direction, strength);
    }

    default Module mult(Module other) {
        if (other.minValue() == 1F && other.maxValue() == 1F) {
            return this;
        }
        return new Multiply(this, other);
    }

    default Module blend(double blend, Module... sources) {
        return blend(blend, Interpolation.LINEAR, sources);
    }

    default Module blend(double blend, Interpolation interpolation, Module... sources) {
        return new MultiBlend((float) blend, interpolation, this, sources);
    }

    default Module pow(Module n) {
        if (n.minValue() == 0 && n.maxValue() == 0) {
            return Source.ONE;
        }
        if (n.minValue() == 1 && n.maxValue() == 1) {
            return this;
        }
        return new Power(this, n);
    }

    default Module pow(double n) {
        return pow(Source.constant(n));
    }

    default Module powCurve(double n) {
        return new PowerCurve(this, (float) n);
    }

    default Module scale(Module scale) {
        if (scale.minValue() == 1 && scale.maxValue() == 1) {
            return this;
        }
        return new Scale(this, scale);
    }

    default Module scale(double scale) {
        return scale(Source.constant(scale));
    }

    default Module select(Module lower, Module upper, double lowerBound, double upperBound, double falloff) {
        return select(lower, upper, lowerBound, upperBound, falloff, Interpolation.CURVE3);
    }

    default Module select(Module lower, Module upper, double lowerBound, double upperBound, double falloff, Interpolation interpolation) {
        return new Select(this, lower, upper, (float) lowerBound, (float) upperBound, (float) falloff, interpolation);
    }

    default Module steps(Module steps) {
        return new Steps(this, steps);
    }

    default Module steps(int steps) {
        return steps(Source.constant(steps));
    }

    default Module sub(Module other) {
        return new Sub(this, other);
    }

    default Module terrace(double lowerCurve, double upperCurve, int steps, double blendRange) {
        return terrace(Source.constant(lowerCurve), Source.constant(upperCurve), steps, blendRange);
    }

    default Module terrace(Module lowerCurve, Module upperCurve, int steps, double blendRange) {
        return new Terrace(this, lowerCurve, upperCurve, steps, (float) blendRange);
    }

    default Module threshold(double threshold) {
        return new Threshold(this, Source.constant(threshold));
    }

    default Module threshold(Module threshold) {
        return new Threshold(this, threshold);
    }

    default Module warp(Module warpX, Module warpZ, double power) {
        return warp(warpX, warpZ, Source.constant(power));
    }

    default Module warp(Module warpX, Module warpZ, Module power) {
        return new Warp(this, warpX, warpZ, power);
    }

    default Module warp(int seed, int scale, int octaves, double power) {
        return warp(Source.PERLIN, seed, scale, octaves, power);
    }

    default Module warp(Source source, int seed, int scale, int octaves, double power) {
        Module x = Source.build(seed, scale, octaves).build(source);
        Module y = Source.build(seed + 1, scale, octaves).build(source);
        Module p = Source.constant(power);
        return warp(x, y, p);
    }

    default Module warp(Class<? extends FastSource> source, int seed, int scale, int octaves, double power) {
        Module x = Source.build(seed, scale, octaves).build(source);
        Module y = Source.build(seed + 1, scale, octaves).build(source);
        Module p = Source.constant(power);
        return warp(x, y, p);
    }
}
