package me.dags.noise.domain;

import me.dags.noise.Module;
import me.dags.noise.Source;

public interface Domain {

    Domain DIRECT = new Domain() {
        @Override
        public float getX(float x, float y) {
            return x;
        }

        @Override
        public float getY(float x, float y) {
            return y;
        }
    };

    float getX(float x, float y);

    float getY(float x, float y);

    default Domain cache() {
        return new Cache(this);
    }

    default Domain then(Domain next) {
        return new Combiner(this, next);
    }

    default Domain then(int seed, int scale, int octaves, double strength, boolean cache) {
        Domain next = warp(seed, scale, octaves, strength);
        if (cache) {
            next = next.cache();
        }
        return then(next);
    }

    static Domain warp(Module x, Module y, Module distance) {
        return new DomainWarp(x, y, distance);
    }

    static Domain warp(int seed, int scale, int octaves, double strength) {
        return warp(
                Source.perlin(seed, scale, octaves),
                Source.perlin(seed + 1, scale, octaves),
                Source.constant(strength)
        );
    }
}
