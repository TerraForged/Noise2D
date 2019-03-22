package me.dags.noise;

import me.dags.noise.combiner.*;
import me.dags.noise.combiner.selector.*;
import me.dags.noise.func.Interpolation;
import me.dags.noise.modifier.*;
import me.dags.noise.source.FastPerlin;
import me.dags.noise.source.FastSource;

/**
 * @author dags <dags@dags.me>
 */
public interface Module extends NoiseFunc {

    default Module abs() {
        if (this instanceof Abs) {
            return this;
        }
        return new Abs(this);
    }

    default Module add(Module other) {
        return new Add(this, other);
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

    default Modifier mod(Module direction, Module power) {
        return new Modulate(this, direction, power);
    }

    default Module mult(Module other) {
        return new Multiply(this, other);
    }

    default Module multiBlend(double blend, Interpolation interpolation, Module... sources) {
        return new MultiBlend((float) blend, interpolation, this, sources);
    }

    default Module pow(Module n) {
        return new Power(this, n);
    }

    default Module pow(double n) {
        return pow(Source.constant(n));
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

    default Module select(Module source0, Module source1, double lowerBound, double upperBound, double falloff, Interpolation interpolation) {
        return new Select(this, source0, source1, (float) lowerBound, (float) upperBound, (float) falloff, interpolation);
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

    default Module warp(Module warpX, Module warpZ, Module power) {
        return new Warp(this, warpX, warpZ, power);
    }

    default Module warp(int seed, int scale, int octaves, double power) {
        return warp(FastPerlin.class, seed, scale, octaves, power);
    }

    default Module warp(Class<? extends FastSource> source, int seed, int scale, int octaves, double power) {
        Module x = Source.build(seed, scale, octaves).build(source);
        Module y = Source.build(seed + 1, scale, octaves).build(source);
        Module p = Source.constant(power);
        return warp(x, y, p);
    }
}
