package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class Grad extends Modifier {

    private final Module lower;
    private final Module upper;
    private final Module strength;

    public Grad(Module source, Module lower, Module upper, Module strength) {
        super(source);
        this.lower = lower;
        this.upper = upper;
        this.strength = strength;
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        float upperBound = upper.getValue(x, y);
        if (noiseValue > upperBound) {
            return noiseValue;
        }

        float amount = strength.getValue(x, y);
        float lowerBound = lower.getValue(x, y);
        if (noiseValue < lowerBound) {
            return NoiseUtil.pow(noiseValue, 1 - amount);
        }

        float alpha = 1 - ((noiseValue - lowerBound) / (upperBound - lowerBound));
        float power = 1 - (amount * alpha);
        return NoiseUtil.pow(noiseValue, power);
    }
}
