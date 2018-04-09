package me.dags.noise.source;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.func.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Builder {

    public static final int SEED = 1337;
    public static final int OCTAVES = 3;
    public static final float GAIN = 0.5F;
    public static final float LACUNARITY = 2F;
    public static final float FREQUENCY = 0.01F;
    public static final float CONST_VALUE = 0F;
    public static final CellFunc CELL_FUNC = CellFunc.CELL_VALUE;
    public static final EdgeFunc EDGE_FUNC = EdgeFunc.DISTANCE_2;
    public static final DistanceFunc DIST_FUNC = DistanceFunc.EUCLIDEAN;
    public static final Interpolation INTERP = Interpolation.CURVE3;
    public static final Source SOURCE = new Constant(CONST_VALUE);

    private int seed = SEED;
    private int octaves = OCTAVES;
    private float gain = GAIN;
    private float lacunarity = LACUNARITY;
    private float frequency = FREQUENCY;
    private Module source = SOURCE;
    private CellFunc cellFunc = CELL_FUNC;
    private EdgeFunc edgeFunc = EDGE_FUNC;
    private DistanceFunc distFunc = DIST_FUNC;
    private Interpolation interpolation = INTERP;

    public Builder() {}

    public int getSeed() {
        return seed;
    }

    public int getOctaves() {
        return octaves;
    }

    public float getGain() {
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

    public Source perlin() {
        return new FastPerlin(this);
    }

    public Source ridge() {
        return new FlowRidge(this);
    }

    public Source billow() {
        return new FastBillow(this);
    }

    public Source cubic() {
        return new FastCubic(this);
    }

    public Source cell() {
        return new FastCell(this);
    }

    public Source cellEdge() {
        return new FastCellEdge(this);
    }

    public Source build(Class<? extends Source> type) {
        try {
            Constructor<? extends Source> constructor = type.getConstructor(Builder.class);
            return constructor.newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return perlin();
        }
    }
}
