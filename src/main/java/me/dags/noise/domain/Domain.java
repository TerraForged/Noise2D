package me.dags.noise.domain;

import me.dags.noise.Module;
import me.dags.noise.Source;

public interface Domain {

    Domain DIRECT = new Domain() {
        @Override
        public float getOffsetX(float x, float y) {
            return 0;
        }

        @Override
        public float getOffsetY(float x, float y) {
            return 0;
        }
    };

    float getOffsetX(float x, float y);

    float getOffsetY(float x, float y);

    default float getX(float x, float y) {
        return x + getOffsetX(x, y);
    }

    default float getY(float x, float y) {
        return y + getOffsetY(x, y);
    }

    default Domain cache() {
        return new Cache(this);
    }

    default Domain add(Domain next) {
        return new CombineAdd(this, next);
    }

    default Domain warp(Domain next) {
        return new CombineWarp(this, next);
    }

    default Domain then(Domain next) {
        return new Combiner(this, next);
    }

    static Domain warp(Module x, Module y, Module distance) {
        return new DomainWarp(x, y, distance);
    }

    static Domain warp(int seed, int scale, int octaves, double strength) {
        return warp(Source.PERLIN, seed, scale, octaves, strength);
    }

    static Domain warp(Source type, int seed, int scale, int octaves, double strength) {
        return warp(
                Source.build(seed, scale, octaves).build(type),
                Source.build(seed + 1, scale, octaves).build(type),
                Source.constant(strength)
        );
    }

    static Domain direction(Module direction, Module distance) {
        return new DirectionWarp(direction, distance);
    }

    static Domain direction(int seed, int scale, int octaves, double strength) {
        return direction(Source.PERLIN, seed, scale, octaves, strength);
    }

    static Domain direction(Source type, int seed, int scale, int octaves, double strength) {
        return direction(
                Source.build(seed, scale, octaves).build(type),
                Source.constant(strength)
        );
    }
}
