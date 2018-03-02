package me.dags.noise;

import me.dags.config.Config;
import me.dags.config.Node;
import me.dags.noise.combiner.*;
import me.dags.noise.func.Interpolation;
import me.dags.noise.modifier.*;
import me.dags.noise.util.Deserializer;

import java.nio.file.Path;

/**
 * @author dags <dags@dags.me>
 */
public interface Module {

    String getName();

    float minValue();

    float maxValue();

    float getValue(float x, float y);

    void toNode(Node node);

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
    default Modifier map(double min, double max) {
        return new Map(this, (float) min, (float) max);
    }

    /**
     * @return A module whose output is clamped between the min and max values provided
     */
    default Modifier clamp(double min, double max) {
        return new Clamp(this, (float) min, (float) max);
    }

    /**
     * @return A module whose output is scaled (multiplied) by the given scale value
     */
    default Modifier scale(double scale) {
        return new Scale(this, (float) scale);
    }

    /**
     * @return A module whose output is added to the given bias value
     */
    default Modifier bias(double bias) {
        return new Bias(this, (float) bias);
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
    default Modifier turbulence(Module x, Module y, double power) {
        return new Turbulence(this, x, y, (float) power);
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
        return new Power(this, other);
    }

    /**
     * @return A module whose output is taken from 'other' unless it's value drops into the falloff range, at which
     *         point the 'this' module's noise is blended into the result
     *
     *         The falloff range is calculated as this.maxValue() to this.maxValue() + falloff
     */
    default Combiner base(Module other, double falloff) {
        return Module.base(this, other, (float) falloff);
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
    default Combiner select(Module source0, Module source1, double lowerBound, double upperBound, double falloff) {
        return Module.select(this, source0, source1, lowerBound, upperBound, falloff);
    }

    /**
     * @return Similar to blend but blending is weighted by an S-curve, controlled by this module
     */
    default Combiner select(Module source0, Module source1, double lowerBound, double upperBound, double falloff, Interpolation interpolation) {
        return Module.select(this, source0, source1, lowerBound, upperBound, falloff, interpolation);
    }

    default void save(Path path, String... nodePath) {
        save(Config.must(path), nodePath);
    }

    default void save(Config config, String... nodePath) {
        Node node = config;
        for (String path : nodePath) {
            node = node.node(path);
        }
        toNode(node);
        config.save();
    }

    /**
     * @return A noise source builder
     */
    static Builder builder() {
        return new Builder();
    }

    /**
     * @return A module whose output is taken from 'other' unless it's value drops into the falloff range, at which
     *         point the 'this' module's noise is blended into the result
     *
     *         The falloff range is calculated as this.maxValue() to this.maxValue() + falloff
     */
    static Combiner base(Module lower, Module upper, double range) {
        return new Base(lower, upper, (float) range);
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
    static Combiner select(Module control, Module source0, Module source1, double lowerBound, double upperBound, double falloff) {
        return new Select(control, source0, source1, (float) lowerBound, (float) upperBound, (float) falloff, Interpolation.HERMITE);
    }

    /**
     * @return Similar to blend but blending is weighted by an S-curve, controlled by the control module
     */
    static Combiner select(Module control, Module source0, Module source1, double lowerBound, double upperBound, double falloff, Interpolation interpolation) {
        return new Select(control, source0, source1, (float) lowerBound, (float) upperBound, (float) falloff, interpolation);
    }

    static Module load(Path path, String... nodePath) {
        return load(Config.must(path), nodePath);
    }

    static Module load(Config config, String... nodePath) {
        Node node = config;
        for (String path : nodePath) {
            node = node.node(path);
        }
        return fromNode(node);
    }

    static Module fromNode(Node node) {
        return Deserializer.getInstance().deserialize(node);
    }
}
