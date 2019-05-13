package me.dags.noise;

public interface Noise {

    float getValue(float x, float y);

    default float maxValue() {
        return 1;
    }

    default float minValue() {
        return 0;
    }
}
