package me.dags.noise.func;

import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum Interpolation {
    LINEAR {
        @Override
        public float apply(float f) {
            return f;
        }
    },
    CURVE3 {
        @Override
        public float apply(float f) {
            return NoiseUtil.InterpHermiteFunc(f);
        }
    },
    CURVE4 {
        @Override
        public float apply(float f) {
            return NoiseUtil.InterpQuinticFunc(f);
        }
    },;

    public abstract float apply(float f);
}
