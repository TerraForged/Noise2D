package me.dags.noise.source.fast;

/**
 * https://github.com/Auburns/FastNoise_Java
 */
public enum CellType {
    CellValue {
        @Override
        public float apply(float xc, float yc) {
            return 0;
        }
    },
    NoiseLookup {
        @Override
        public float apply(float xc, float yc) {
            return 0;
        }
    },
    Distance {
        @Override
        public float apply(float distance, float distance2) {
            return 0;
        }
    },
    Distance2 {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 - 1;
        }
    },
    Distance2Add {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 + distance - 1;
        }
    },
    Distance2Sub {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 - distance - 1;
        }
    },
    Distance2Mul {
        @Override
        public float apply(float distance, float distance2) {
            return distance2 * distance - 1;
        }
    },
    Distance2Div {
        @Override
        public float apply(float distance, float distance2) {
            return distance / distance2 - 1;
        }
    },
    ;

    public abstract float apply(float distance, float distance2);
}
