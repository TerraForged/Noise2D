package me.dags.noise.source.fast;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum CellDistanceFunc {

    Euclidean {
        @Override
        public float apply(float vecX, float vecY) {
            return vecX * vecX + vecY * vecY;
        }
    },
    Manhattan {
        @Override
        public float apply(float vecX, float vecY) {
            return Math.abs(vecX) + Math.abs(vecY);
        }
    },
    Natural {
        @Override
        public float apply(float vecX, float vecY) {
            return (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);
        }
    },
    ;

    public abstract float apply(float vecX, float vecY);
}
