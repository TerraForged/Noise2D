package me.dags.noise.source;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.cache.Cache;
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
    protected final Cache cache;

    public FastSource(Builder builder) {
        this.seed = builder.getSeed();
        this.octaves = builder.getOctaves();
        this.lacunarity = builder.getLacunarity();
        this.gain = builder.getGain();
        this.frequency = builder.getFrequency();
        this.cache = builder.getCache();
    }

    protected abstract float value(float x, float y);

    @Override
    public float getValue(float x, float y) {
        if (!getCache().isCached(x, y)) {
            return getCache().cacheValue(x, y, value(x, y));
        }
        return getCache().getValue();
    }

    @Override
    public Cache getCache() {
        return cache;
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
        Util.setNonDefault(node, "getSeed", seed, Builder.SEED);
        Util.setNonDefault(node, "getOctaves", octaves, Builder.OCTAVES);
        Util.setNonDefault(node, "getGain", gain, Builder.GAIN);
        Util.setNonDefault(node, "getFrequency", frequency, Builder.FREQUENCY);
        Util.setNonDefault(node, "getLacunarity", lacunarity, Builder.LACUNARITY);
    }

    @Override
    public String toString() {
        return getName() + "{"
                + properties()
                + '}';
    }

    protected String properties() {
        return "getSeed=" + seed +
                ", getOctaves=" + octaves +
                ", getLacunarity=" + lacunarity +
                ", getGain=" + gain +
                ", getFrequency=" + frequency;
    }
}
