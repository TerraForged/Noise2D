package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.func.Interpolation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author dags <dags@dags.me>
 */
public class Builder {

    public static final int DEFAULT_SEED = 1337;
    public static final int DEFAULT_OCTAVES = 3;
    public static final float DEFAULT_GAIN = 0.5F;
    public static final float DEFAULT_RIDGE_GAIN = 0.975F;
    public static final float DEFAULT_LACUNARITY = 2F;
    public static final float DEFAULT_FREQUENCY = 0.01F;
    public static final CellFunc DEFAULT_CELL_FUNC = CellFunc.CELL_VALUE;
    public static final EdgeFunc DEFAULT_EDGE_FUNC = EdgeFunc.DISTANCE_2;
    public static final DistanceFunc DEFAULT_DIST_FUNC = DistanceFunc.EUCLIDEAN;
    public static Interpolation DEFAULT_INTERPOLATION = Interpolation.CURVE3;

    private int seed = DEFAULT_SEED;
    private int octaves = DEFAULT_OCTAVES;
    private float gain = Float.MAX_VALUE;
    private float lacunarity = DEFAULT_LACUNARITY;
    private float frequency = DEFAULT_FREQUENCY;
    private Module source = Source.ZERO;
    private CellFunc cellFunc = DEFAULT_CELL_FUNC;
    private EdgeFunc edgeFunc = DEFAULT_EDGE_FUNC;
    private DistanceFunc distFunc = DEFAULT_DIST_FUNC;
    private Interpolation interpolation = DEFAULT_INTERPOLATION;

    public Builder() {
    }

    public int getSeed() {
        return seed;
    }

    public int getOctaves() {
        return octaves;
    }

    public float getGain() {
        if (gain == Float.MAX_VALUE) {
            gain = DEFAULT_GAIN;
        }
        return gain;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getLacunarity() {
        return lacunarity;
    }

    public Interpolation getInterp() {
        return interpolation;
    }

    public CellFunc getCellFunc() {
        return cellFunc;
    }

    public EdgeFunc getEdgeFunc() {
        return edgeFunc;
    }

    public DistanceFunc getDistFunc() {
        return distFunc;
    }

    public Module getSource() {
        return source;
    }

    public Builder seed(int seed) {
        this.seed = seed;
        return this;
    }

    public Builder octaves(int octaves) {
        this.octaves = octaves;
        return this;
    }

    public Builder gain(double gain) {
        this.gain = (float) gain;
        return this;
    }

    public Builder lacunarity(double lacunarity) {
        this.lacunarity = (float) lacunarity;
        return this;
    }

    public Builder scale(int frequency) {
        this.frequency = 1F / frequency;
        return this;
    }

    public Builder frequency(double frequency) {
        this.frequency = (float) frequency;
        return this;
    }

    public Builder interp(Interpolation interpolation) {
        this.interpolation = interpolation;
        return this;
    }

    public Builder cellFunc(CellFunc cellFunc) {
        this.cellFunc = cellFunc;
        return this;
    }

    public Builder edgeFunc(EdgeFunc cellType) {
        this.edgeFunc = cellType;
        return this;
    }

    public Builder distFunc(DistanceFunc cellDistance) {
        this.distFunc = cellDistance;
        return this;
    }

    public Builder source(Module source) {
        this.source = source;
        return this;
    }

    public Module perlin() {
        return new FastPerlin(this);
    }

    public Module simplex() {
        return new FastSimplex(this);
    }

    public Module ridge() {
        if (gain == Float.MAX_VALUE) {
            gain = DEFAULT_RIDGE_GAIN;
        }
        return new FastRidge(this);
    }

    public Module billow() {
        return new FastBillow(this);
    }

    public Module cubic() {
        return new FastCubic(this);
    }

    public Module cell() {
        return new FastCell(this);
    }

    public Module cellEdge() {
        return new FastCellEdge(this);
    }

    public Module build(Class<? extends Module> type) {
        try {
            Constructor<? extends Module> constructor = type.getConstructor(Builder.class);
            return constructor.newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return perlin();
        }
    }
}
