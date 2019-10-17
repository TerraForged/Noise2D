package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.NoiseUtil;

public class Terrace extends Modifier {

    private final int maxIndex;
    private final Step[] steps;
    private final Module lowerCurve;
    private final Module upperCurve;

    public Terrace(Module source, Module lowerCurve, Module upperCurve, int steps, float blendRange) {
        super(source.map(0, 1));
        this.maxIndex = steps - 1;
        this.steps = new Step[steps];
        this.lowerCurve = lowerCurve;
        this.upperCurve = upperCurve;
        float min = source.minValue();
        float max = source.maxValue();
        float range = max - min;
        float spacing = range / (steps - 1);

        for (int i = 0; i < steps; i++) {
            float value = i * spacing;
            this.steps[i] = new Step(value, spacing, blendRange);
        }
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        int index = NoiseUtil.round(noiseValue * maxIndex);
        Step step = steps[index];
        if (noiseValue < step.lowerBound) {
            Step lower = steps[index - 1];
            float alpha = (noiseValue - lower.upperBound) / (step.lowerBound - lower.upperBound);
            alpha = 1 - Interpolation.CURVE3.apply(alpha);
            float range = step.value - lower.value;
            return step.value - (alpha * range * upperCurve.getValue(x, y));
        } else if (noiseValue > step.upperBound) {
            Step upper = steps[index + 1];
            float alpha = (noiseValue - step.upperBound) / (upper.lowerBound - step.upperBound);
            alpha = Interpolation.CURVE3.apply(alpha);
            float range = upper.value - step.value;
            return step.value + (alpha * range * lowerCurve.getValue(x, y));
        } else {
            return step.value;
        }
    }

    private static class Step {

        private final float value;
        private final float lowerBound;
        private final float upperBound;

        private Step(float value, float distance, float blendRange) {
            this.value = value;
            float blend = distance * blendRange;
            float bound = (distance - blend) / 2F;
            lowerBound = value - bound;
            upperBound = value + bound;
        }
    }
}
