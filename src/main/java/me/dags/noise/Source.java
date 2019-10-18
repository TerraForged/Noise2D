package me.dags.noise;

import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.source.Builder;
import me.dags.noise.source.Constant;
import me.dags.noise.source.Line;
import me.dags.noise.source.Rand;
import me.dags.noise.util.Vec2i;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public enum Source {
    BILLOW(Builder::billow),
    CELL(Builder::cell),
    CELL_EDGE(Builder::cellEdge),
    CONST(Builder::constant),
    CUBIC(Builder::cubic),
    PERLIN(Builder::perlin),
    RIDGE(Builder::ridge),
    SIMPLEX(Builder::simplex),
    SIN(Builder::sin),
    RAND(Builder::rand);

    public static final Module ONE = new Constant(1F);
    public static final Module ZERO = new Constant(0F);
    public static final Module HALF = new Constant(0.5F);

    private final Function<Builder, Module> fn;

    Source(Function<Builder, Module> fn) {
        this.fn = fn;
    }

    public Module build(Builder builder) {
        return fn.apply(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder build(int scale, int octaves) {
        return build(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Builder build(int seed, int scale, int octaves) {
        return builder().seed(seed).scale(scale).octaves(octaves);
    }

    public static Module perlin(int scale, int octaves) {
        return perlin(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Module perlin(int seed, double freq, int octaves) {
        return Source.builder().seed(seed).frequency(freq).octaves(octaves).perlin();
    }

    public static Module perlin(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).perlin();
    }

    public static Module simplex(int scale, int octaves) {
        return simplex(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Module simplex(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).simplex();
    }

    public static Module billow(int scale, int octaves) {
        return billow(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Module billow(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).billow();
    }

    public static Module ridge(int scale, int octaves) {
        return ridge(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Module ridge(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).ridge();
    }

    public static Module cubic(int scale, int octaves) {
        return cubic(ThreadLocalRandom.current().nextInt(), scale, octaves);
    }

    public static Module cubic(int seed, int scale, int octaves) {
        return Source.builder().seed(seed).scale(scale).octaves(octaves).cubic();
    }

    public static Module cell(int scale) {
        return cell(ThreadLocalRandom.current().nextInt(), scale);
    }

    public static Module cell(int scale, CellFunc cellFunc) {
        return cell(ThreadLocalRandom.current().nextInt(), scale, cellFunc);
    }

    public static Module cell(int seed, int scale) {
        return Source.cell(seed, scale, CellFunc.CELL_VALUE);
    }

    public static Module cell(int seed, int scale, DistanceFunc distFunc) {
        return Source.builder().seed(seed).scale(scale).distFunc(distFunc).cell();
    }

    public static Module cell(int seed, int scale, CellFunc cellFunc) {
        return Source.builder().seed(seed).scale(scale).cellFunc(cellFunc).cell();
    }

    public static Module cell(int seed, int scale, DistanceFunc distFunc, CellFunc cellFunc) {
        return Source.builder().seed(seed).scale(scale).distFunc(distFunc).cellFunc(cellFunc).cell();
    }

    public static Module cellNoise(int scale, Module source) {
        return cellNoise(ThreadLocalRandom.current().nextInt(), scale, source);
    }

    public static Module cellNoise(int seed, int scale, Module source) {
        return builder().seed(seed).scale(scale).cellFunc(CellFunc.NOISE_LOOKUP).source(source).cell();
    }

    public static Module cellNoise(int seed, int scale, DistanceFunc distFunc, Module source) {
        return builder().seed(seed).scale(scale)
                .cellFunc(CellFunc.NOISE_LOOKUP)
                .distFunc(distFunc)
                .source(source)
                .cell();
    }

    public static Module cellEdge(int scale) {
        return cellEdge(ThreadLocalRandom.current().nextInt(), scale);
    }

    public static Module cellEdge(int scale, EdgeFunc func) {
        return cellEdge(ThreadLocalRandom.current().nextInt(), scale, func);
    }

    public static Module cellEdge(int seed, int scale) {
        return Source.builder().seed(seed).scale(scale).cellEdge();
    }

    public static Module cellEdge(int seed, int scale, EdgeFunc func) {
        return Source.builder().seed(seed).scale(scale).edgeFunc(func).cellEdge();
    }

    public static Module cellEdge(int seed, int scale, DistanceFunc distFunc, EdgeFunc edgeFunc) {
        return Source.builder().seed(seed).scale(scale).distFunc(distFunc).edgeFunc(edgeFunc).cellEdge();
    }

    public static Rand rand(int scale) {
        return rand(ThreadLocalRandom.current().nextInt(), scale);
    }

    public static Rand rand(int seed, int scale) {
        return Source.build(seed, scale, 0).rand();
    }

    public static Module sin(int scale, Module source) {
        return Source.builder().scale(scale).source(source).sin();
    }

    public static Module line(int x1, int y1, int x2, int y2, double radius, double fadeIn, double fadeOut) {
        return line(x1, y1, x2, y2, Source.constant(radius * radius), Source.constant(fadeIn), Source.constant(fadeOut));
    }

    public static Module line(int x1, int y1, int x2, int y2, double radius, double fadeIn, double fadeOut, double feather) {
        return line(x1, y1, x2, y2, Source.constant(radius * radius), Source.constant(fadeIn), Source.constant(fadeOut), feather);
    }

    public static Module line(int x1, int y1, int x2, int y2, Module radius2, Module fadeIn, Module fadeOut) {
        return line(x1, y1, x2, y2, radius2, fadeIn, fadeOut, 0.33);
    }

    public static Module line(int x1, int y1, int x2, int y2, Module radius2, Module fadeIn, Module fadeOut, double feather) {
        return new Line(x1, y1, x2, y2, radius2, fadeIn, fadeOut, (float) feather);
    }

    public static Module constant(double value) {
        if (value == 0) {
            return ZERO;
        }
        if (value == 0.5) {
            return HALF;
        }
        if (value == 1) {
            return ONE;
        }
        return new Constant((float) value);
    }
}
