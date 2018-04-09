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

        @Override
        public float max() {
            return 1;
        }

        @Override
        public float min() {
            return -1;
        }

        @Override
        public float range() {
            return 2;
        }
    },
    DISTANCE_2_ADD {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 + distance - 1;
        }

        @Override
        public float max() {
            return 1.6F;
        }

        @Override
        public float min() {
            return -1;
        }

        @Override
        public float range() {
            return 2.6F;
        }
    },
    DISTANCE_2_SUB {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 - distance - 1;
        }

        @Override
        public float max() {
            return 0.8F;
        }

        @Override
        public float min() {
            return -1;
        }

        @Override
        public float range() {
            return 1.8F;
        }
    },
    DISTANCE_2_MUL {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 * distance - 1;
        }

        @Override
        public float max() {
            return 0.7F;
        }

        @Override
        public float min() {
            return -1F;
        }

        @Override
        public float range() {
            return 1.7F;
        }
    },
    DISTANCE_2_DIV {
        @Override
        public float apply(float distance, float distance2) {
            return distance / distance2 - 1;
        }

        @Override
        public float max() {
            return 0;
        }

        @Override
        public float min() {
            return -1;
        }

        @Override
        public float range() {
            return 1;
        }
    },
    ;

    public abstract float apply(float distance, float distance2);

    public abstract float max();

    public abstract float min();

    public abstract float range();
}
