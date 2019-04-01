package me.dags.noise.modifier;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class Cache implements Module {

    private final Module source;
    private final Value value = new Value();

    public Cache(Module source) {
        this.source = source;
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
        Value value = this.value;
        if (value.matches(x, y)) {
            return value.value;
        }
        return value.set(x, y, source.getValue(x, y));
    }

    private static class Value {

        private float x = 0;
        private float y = 0;
        private float value = 0;
        private boolean empty = true;

        private boolean matches(float x, float y) {
            return !empty && x == this.x && y == this.y;
        }

        private float set(float x, float y, float value) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.empty = false;
            return value;
        }
    }
}
