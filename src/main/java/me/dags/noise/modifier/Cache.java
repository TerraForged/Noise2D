package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Cache implements Module {

    private final Module source;
    private final ThreadLocal<Value> value = ThreadLocal.withInitial(Value::new);

    public Cache(Module source) {
        this.source = source;
        getValue(1, 1);
    }

    @Override
    public float minValue() {
        return source.minValue();
    }

    @Override
    public float maxValue() {
        return source.maxValue();
    }

    @Override
    public float getValue(float x, float y) {
        Value value = this.value.get();
        if (value.matches(x, y)) {
            return value.value;
        }
        return value.set(x, y, source.getValue(x, y));
    }

    private static class Value {

        private boolean set = false;
        private float x = 0;
        private float y = 0;
        private float value = 0;

        private boolean matches(float x, float y) {
            return set && x == this.x && y == this.y;
        }

        private float set(float x, float y, float value) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.set = true;
            return value;
        }
    }
}
