package me.dags.noise.source.fast;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum Interpolation {
    Linear {
        @Override
        public float apply(float i0, float i1) {
            return i1 - i0;
        }
    },
    Hermite {
        @Override
        public float apply(float i0, float i1) {
            return Util.InterpHermiteFunc(i1 - i0);
        }
    },
    Quintic {
        @Override
        public float apply(float i0, float i1) {
            return Util.InterpQuinticFunc(i1 - i0);
        }
    },
    ;

    public abstract float apply(float i0, float i1);
}
