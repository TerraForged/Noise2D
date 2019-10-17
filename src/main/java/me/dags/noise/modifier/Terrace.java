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
        super(source);
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
    public float getValue(float x, float y) {
        float value = source.getValue(x, y);
        value = NoiseUtil.clamp(value, 0, 1);
        return modify(x, y, value);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        int index = NoiseUtil.round(noiseValue * maxIndex);
        Step step = steps[index];
        if (noiseValue < step.lowerBound) {
            if (index > 0) {
                Step lower = steps[index - 1];
                float alpha = (noiseValue - lower.upperBound) / (step.lowerBound - lower.upperBound);
                alpha = 1 - Interpolation.CURVE3.apply(alpha);
                float range = step.value - lower.value;
                return step.value - (alpha * range * upperCurve.getValue(x, y));
            }
        } else if (noiseValue > step.upperBound) {
            if (index < maxIndex) {
                Step upper = steps[index + 1];
                float alpha = (noiseValue - step.upperBound) / (upper.lowerBound - step.upperBound);
                alpha = Interpolation.CURVE3.apply(alpha);
                float range = upper.value - step.value;
                return step.value + (alpha * range * lowerCurve.getValue(x, y));
            }
        }
        return step.value;
    }

    private int getIndex(float value) {
        int index = NoiseUtil.round(value * maxIndex);
        if (index > maxIndex) {
            return maxIndex;
        } else if (index < 0) {
            return 0;
        }
        return index;
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
