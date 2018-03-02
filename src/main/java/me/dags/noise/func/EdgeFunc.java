package me.dags.noise.func;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum EdgeFunc {
    DISTANCE_2 {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 - 1;
        }
    },
    DISTANCE_2_ADD {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 + distance - 1;
        }
    },
    DISTANCE_2_SUB {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 - distance - 1;
        }
    },
    DISTANCE_2_MUL {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 * distance - 1;
        }
    },
    DISTANCE_2_DIV {
        @Override
        public float apply(float distance, float distance2) {
            return distance / distance2 - 1;
        }
    },
    ;

    public abstract float apply(float distance, float distance2);
}
