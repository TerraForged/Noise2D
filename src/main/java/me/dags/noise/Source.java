package me.dags.noise;

import java.util.concurrent.ThreadLocalRandom;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.source.Builder;
import me.dags.noise.source.Constant;

/**
 * @author dags <dags@dags.me>
 *
 * Source modules are expected to return values between 0 and 1.0
 */
public interface Source extends Module {

    Source ONE = Source.constant(1);

    Source ZERO = Source.constant(0);

    Source HALF = Source.constant(0.5);

    Builder toBuilder();

    static Builder builder() {
        return new Builder();
    }

    static Source perlin(int scale, int octaves) {
        return perlin(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    static Source perlin(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).perlin();
    }

    static Source billow(int scale, int octaves) {
        return billow(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    static Source billow(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).billow();
    }

    static Source ridge(int scale, int octaves) {
        return ridge(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    static Source ridge(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).ridge();
    }

    static Source cubic(int scale, int octaves) {
        return cubic(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    static Source cubic(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).cubic();
    }

    static Source cell(int scale) {
        return cell(ThreadLocalRandom.current().nextInt(), scale);
    }

    static Source cell(int scale, CellFunc cellFunc) {
        return cell(ThreadLocalRandom.current().nextInt(), scale, cellFunc);
    }

    static Source cell(int seed, int scale) {
        return Source.cell(seed, scale, CellFunc.CELL_VALUE);
    }

    static Source cell(int seed, int scale, CellFunc cellFunc) {
        return Source.builder().seed(seed).scale(scale).cellFunc(cellFunc).cell();
    }

    static Source cellNoise(int scale, Module source) {
        return cellNoise(ThreadLocalRandom.current().nextInt(), scale, source);
    }

    static Source cellNoise(int seed, int scale, Module source) {
        return builder().seed(seed).scale(scale).cellFunc(CellFunc.NOISE_LOOKUP).source(source).cell();
    }

    static Source cellEdge(int scale) {
        return cellEdge(ThreadLocalRandom.current().nextInt(), scale);
    }

    static Source cellEdge(int scale, EdgeFunc func) {
        return cellEdge(ThreadLocalRandom.current().nextInt(), scale, func);
    }

    static Source cellEdge(int seed, int scale) {
        return Source.builder().seed(seed).scale(scale).cellEdge();
    }

    static Source cellEdge(int seed, int scale, EdgeFunc func) {
        return Source.builder().seed(seed).scale(scale).edgeFunc(func).cellEdge();
    }

    static Source constant(double value) {
        return new Constant((float) value);
    }
}
