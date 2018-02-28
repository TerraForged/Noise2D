package me.dags.noise;

import me.dags.noise.modifier.Modifier;
import me.dags.noise.modifier.Turbulence;
import me.dags.noise.source.*;
import me.dags.noise.source.fast.CellDistanceFunc;
import me.dags.noise.source.fast.CellType;
import me.dags.noise.source.fast.Interpolation;

/**
 * @author dags <dags@dags.me>
 */
public class Builder {

    public static final int SEED = 1337;
    public static final int OCTAVES = 3;
    public static final float GAIN = 0.5F;
    public static final float LACUNARITY = 2F;
    public static final float FREQUENCY = 0.01F;
    private static final Source DEFAULT = new Constant(0.5F);

    private int seed = SEED;
    private int octaves = OCTAVES;
    private float gain = GAIN;
    private float lacunarity = LACUNARITY;
    private float frequency = FREQUENCY;
    private float power = 1F;
    private Interpolation interpolation = Interpolation.Quintic;
    private CellType cellType = CellType.CellValue;
    private CellDistanceFunc cellDistance = CellDistanceFunc.Euclidean;
    private Module source = DEFAULT;

    protected Builder() {}

    public int seed() {
        return seed;
    }

    public int octaves() {
        return octaves;
    }

    public float gain() {
        return gain;
    }

    public float power() {
        return power;
    }

    public float frequency() {
        return frequency;
    }

    public float lacunarity() {
        return lacunarity;
    }

    public Interpolation interp() {
        return interpolation;
    }

    public CellType cellType() {
        return cellType;
    }

    public CellDistanceFunc cellDistance() {
        return cellDistance;
    }

    public Module source() {
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

    public Builder gain(float gain) {
        this.gain = gain;
        return this;
    }

    public Builder power(float power) {
        this.power = power;
        return this;
    }

    public Builder lacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
        return this;
    }

    public Builder scale(int frequency) {
        this.frequency = 1F / frequency;
        return this;
    }

    public Builder frequency(float frequency) {
        this.frequency = frequency;
        return this;
    }

    public Builder interp(Interpolation interpolation) {
        this.interpolation = interpolation;
        return this;
    }

    public Builder cellType(CellType cellType) {
        this.cellType = cellType;
        return this;
    }

    public Builder cellDistance(CellDistanceFunc cellDistance) {
        this.cellDistance = cellDistance;
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
        return new FastRidge(this);
    }

    public Source billow() {
        return new FastBillow(this);
    }

    public Source cubic() {
        return new FastCubic(this);
    }

    public Source cell() {
        switch (cellType()) {
            case CellValue:
            case Distance:
            case NoiseLookup:
                return new FastCell(this);
            default:
                return new FastCellEdge(this);
        }
    }

    public Modifier turbulence() {
        int seed = seed();
        Module turb0 = perlin();
        Module turb1 = seed(seed + 1).perlin();
        seed(seed);
        return new Turbulence(source(), turb0, turb1, power());
    }
}
