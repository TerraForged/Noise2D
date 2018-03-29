package me.dags.noise.cache;

/**
 * @author dags <dags@dags.me>
 */
public class ValueCache implements Cache {

    protected float lastX;
    protected float lastY;
    protected float lastValue;

    @Override
    public float getValue() {
        return lastValue;
    }

    @Override
    public boolean isCached(float x, float y) {
        return x == lastX && y == lastY;
    }

    @Override
    public float cacheValue(float x, float y, float value) {
        lastX = x;
        lastY = y;
        lastValue = value;
        return value;
    }
}
