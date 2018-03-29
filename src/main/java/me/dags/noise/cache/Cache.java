package me.dags.noise.cache;

/**
 * @author dags <dags@dags.me>
 */
public interface Cache {

    float getValue();

    boolean isCached(float x, float y);

    float cacheValue(float x, float y, float value);

    Cache NONE = new Cache() {
        @Override
        public float getValue() {
            return 0;
        }

        @Override
        public boolean isCached(float x, float y) {
            return false;
        }

        @Override
        public float cacheValue(float x, float y, float value) {
            return value;
        }
    };
}
