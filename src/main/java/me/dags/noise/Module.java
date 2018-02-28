package me.dags.noise;

import me.dags.noise.combiner.*;
import me.dags.noise.modifier.*;
import me.dags.noise.source.Constant;
import me.dags.noise.source.fast.CellType;

/**
 * @author dags <dags@dags.me>
 */
public interface Module {

    float minValue();

    float maxValue();

    float getValue(float x, float y);

    /**
     * @return A module whose output is mapped and clamped between 0 and 1
     */
    default Module norm() {
        if (this instanceof Normalize) {
            return this;
        }
        return new Normalize(this);
    }

    /**
     * @return A module whose output is absolute (ie always a positive number)
     */
    default Modifier abs() {
        return new Abs(this);
    }

    /**
     * @return A module whose output is inverted (ie negatives become positives & visa versa)
     */
    default Modifier invert() {
        return new Invert(this);
    }

    /**
     * @return A module whose output is mapped between the min and max values provided
     */
    default Modifier map(float min, float max) {
        return new Map(this, min, max);
    }

    /**
     * @return A module whose output is clamped between the min and max values provided
     */
    default Modifier clamp(float min, float max) {
        return new Clamp(this, min, max);
    }

    /**
     * @return A module whose output is scaled (multiplied) by the given scale value
     */
    default Modifier scale(float scale) {
        return new Scale(this, scale);
    }

    /**
     * @return A module whose output is added to the given bias value
     */
    default Modifier bias(float bias) {
        return new Bias(this, bias);
    }

    /**
     * @return A module whose output values are mapped to stepped values between 0 and 1
     */
    default Modifier steps(int steps) {
        return new Steps(this, steps);
    }

    /**
     * @return A module whose output values are distorted in the x/y directions by Perlin noise built from the provided Builder
     */
    default Modifier turbulence(Builder builder) {
        int seed = builder.seed();
        Module x = builder.perlin();
        Module y = builder.seed(seed + 1).perlin();
        builder.seed(seed);
        return turbulence(x, y, builder.power());
    }

    /**
     * @return A module whose output values are distorted in the x/y directions by Modules x & y with the given power
     */
    default Modifier turbulence(Module x, Module y, float power) {
        return new Turbulence(this, x, y, power);
    }

    /**
     * @return A module whose output is the sum of this and the other module's outputs
     */
    default Combiner add(Module other) {
        return new Add(this, other);
    }

    /**
     * @return A module whose output is the difference of this and the other module's outputs
     */
    default Combiner sub(Module other) {
        return new Sub(this, other);
    }

    /**
     * @return A module whose output is the lowest of this and the other module's outputs
     */
    default Combiner min(Module other) {
        return new Min(this, other);
    }

    /**
     * @return A module whose output is the highest of this and the other module's outputs
     */
    default Combiner max(Module other) {
        return new Max(this, other);
    }

    /**
     * @return A module whose output is calculated by multiplying this and the other module's outputs
     */
    default Combiner mult(Module other) {
        return new Multiply(this, other);
    }

    /**
     * @return A module whose output value is this module's output to the power of the given module's output
     */
    default Combiner pow(Module other) {
        return new Multiply(this, other);
    }

    /**
     * @return A module whose output is blend of source0 and source1's outputs controlled by this module
     */
    default Combiner blend(Module source0, Module source1) {
        return blend(this, source0, source1);
    }

    /**
     * @return Similar to blend but blending is weighted by an S-curve, controlled by this module
     */
    default Combiner select(Module source0, Module source1, float lowerBound, float upperBound, float falloff) {
        return select(this, source0, source1, lowerBound, upperBound, falloff);
    }

    /**
     * @return A noise source builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source perlin(int seed, int scale, int octaves) {
        return perlin(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source perlin(int seed, float frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).perlin();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source billow(int seed, int scale, int octaves) {
        return billow(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source billow(int seed, float frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).billow();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source ridge(int seed, int scale, int octaves) {
        return ridge(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source ridge(int seed, float frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).ridge();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cubic(int seed, int scale, int octaves) {
        return cubic(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cubic(int seed, float frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).cubic();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cell(int seed, int scale, CellType cellType) {
        return cell(seed, 1F / scale, cellType);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cell(int seed, float frequency, CellType cellType) {
        return builder().seed(seed).frequency(frequency).cellType(cellType).cell();
    }

    /**
     * @return A module with a constant output value
     */
    static Source constant(float value) {
        return new Constant(value);
    }

    /**
     * @return A module whose output is blend of source0 and source1's outputs controlled by the control module
     */
    static Combiner blend(Module control, Module source0, Module source1) {
        return new Blend(control, source0, source1);
    }

    /**
     * @return Similar to blend but blending is weighted by an S-curve, controlled by the control module
     */
    static Combiner select(Module control, Module source0, Module source1, float lowerBound, float upperBound, float falloff) {
        return new Select(control, source0, source1, lowerBound, upperBound, falloff);
    }
}
