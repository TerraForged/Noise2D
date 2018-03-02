package me.dags.noise.func;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum DistanceFunc {

    EUCLIDEAN {
        @Override
        public float apply(float vecX, float vecY) {
            return vecX * vecX + vecY * vecY;
        }
    },
    MANHATTAN {
        @Override
        public float apply(float vecX, float vecY) {
            return Math.abs(vecX) + Math.abs(vecY);
        }
    },
    NATURAL {
        @Override
        public float apply(float vecX, float vecY) {
            return (Math.abs(vecX) + Math.abs(vecY)) + (vecX * vecX + vecY * vecY);
        }
    },
    ;

    public abstract float apply(float vecX, float vecY);
}
