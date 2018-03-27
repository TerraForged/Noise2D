package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Builder;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.util.Util;

/**
 * @author dags <dags@dags.me>
 * <p>
 * FastSource modules are based on the work of FastNoise_Java https://github.com/Auburns/FastNoise_Java
 */
public abstract class FastSource implements Source {

    protected final int seed;
    protected final int octaves;
    protected final float lacunarity;
    protected final float gain;
    protected final float frequency;

    public FastSource(Builder builder) {
        this(builder.seed(), builder.octaves(), builder.lacunarity(), builder.gain(), builder.frequency());
    }

    public FastSource(int seed, int octaves, float lacunarity, float gain, float frequency) {
        this.seed = seed;
        this.octaves = octaves;
        this.lacunarity = lacunarity;
        this.gain = gain;
        this.frequency = frequency;
    }

    @Override
    public float minValue() {
        return 0;
    }

    @Override
    public float maxValue() {
        return 1;
    }

    @Override
    public Builder toBuilder() {
        return Module.builder()
                .seed(seed)
                .octaves(octaves)
                .lacunarity(lacunarity)
                .gain(gain)
                .frequency(frequency);
    }

    @Override
    public void toNode(Node node) {
        node.clear();
        node.set("module", getName());
        Util.setNonDefault(node, "seed", seed, Builder.SEED);
        Util.setNonDefault(node, "octaves", octaves, Builder.OCTAVES);
        Util.setNonDefault(node, "gain", gain, Builder.GAIN);
        Util.setNonDefault(node, "frequency", frequency, Builder.FREQUENCY);
        Util.setNonDefault(node, "lacunarity", lacunarity, Builder.LACUNARITY);
    }

    @Override
    public String toString() {
        return getName() + "{"
                + properties()
                + '}';
    }

    protected String properties() {
        return "seed=" + seed +
                ", octaves=" + octaves +
                ", lacunarity=" + lacunarity +
                ", gain=" + gain +
                ", frequency=" + frequency;
    }
}
