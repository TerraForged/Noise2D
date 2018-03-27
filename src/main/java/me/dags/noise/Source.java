package me.dags.noise;

import me.dags.noise.func.CellFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.source.Constant;

/**
 * @author dags <dags@dags.me>
 *
 * Source modules are expected to return values between 0 and 1.0
 */
public interface Source extends Module {

    Builder toBuilder();

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
        return Source.perlin(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source perlin(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).perlin();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source billow(int seed, int scale, int octaves) {
        return Source.billow(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source billow(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).billow();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source ridge(int seed, int scale, int octaves) {
        return Source.ridge(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source ridge(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).ridge();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cubic(int seed, int scale, int octaves) {
        return Source.cubic(seed, 1F / scale, octaves);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cubic(int seed, double frequency, int octaves) {
        return builder().seed(seed).frequency(frequency).octaves(octaves).cubic();
    }

    static Source cell(int seed, int scale) {
        return Source.cell(seed, 1F / scale);
    }

    static Source cell(int seed, int scale, CellFunc cellFunc) {
        return Source.cell(seed, 1F / scale, cellFunc);
    }

    static Source cell(int seed, double frequency) {
        return builder().seed(seed).frequency(frequency).cell();
    }

    static Source cell(int seed, double frequency, CellFunc cellFunc) {
        return builder().seed(seed).frequency(frequency).cellFunc(cellFunc).cell();
    }

    /**
     * @return A Source Module with the given seed, scale and octaves
     */
    static Source cellEdge(int seed, int scale) {
        return Source.cellEdge(seed, 1F / scale);
    }

    static Source cellEdge(int seed, int scale, EdgeFunc func) {
        return Source.cellEdge(seed, 1F / scale, func);
    }

    /**
     * @return A Source Module with the given seed, frequency and octaves
     */
    static Source cellEdge(int seed, double frequency) {
        return builder().seed(seed).frequency(frequency).cellEdge();
    }

    static Source cellEdge(int seed, double frequency, EdgeFunc func) {
        return builder().seed(seed).frequency(frequency).edgeFunc(func).cellEdge();
    }

    /**
     * @return A module with a constant output value
     */
    static Source constant(double value) {
        return new Constant((float) value);
    }
}
