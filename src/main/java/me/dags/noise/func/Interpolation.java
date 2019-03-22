package me.dags.noise.func;

import me.dags.noise.util.NoiseUtil;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum Interpolation implements CurveFunc {
    LINEAR {
        @Override
        public float apply(float f) {
            return f;
        }
    },
    CURVE3 {
        @Override
        public float apply(float f) {
            return NoiseUtil.interpHermite(f);
        }
    },
    CURVE4 {
        @Override
        public float apply(float f) {
            return NoiseUtil.interpQuintic(f);
        }
    },;

    public abstract float apply(float f);
}
