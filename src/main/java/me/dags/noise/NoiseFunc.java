package me.dags.noise;

public interface NoiseFunc {

    float getValue(float x, float y);

    default void apply(float x, float y, Value value) {
        value.value = getValue(x, y);
    }

    default float maxValue() {
        return 1;
    }

    default float minValue() {
        return 0;
    }
}
