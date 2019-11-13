package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class AdvancedTerrace extends Modifier {

    private final int steps;
    private final int octaves;
    private final float modRange;
    private final float blendMin;
    private final float blendMax;
    private final float blendRange;
    private final Module slope;
    private final Module mask;
    private final Module modulation;

    public AdvancedTerrace(Module source, Module modulation, Module mask, Module slope, float blendMin, float blendMax, int steps, int octaves) {
        super(source);
        this.mask = mask;
        this.steps = steps;
        this.octaves = octaves;
        this.slope = slope;
        this.modulation = modulation;
        this.blendMin = blendMin;
        this.blendMax = blendMax;
        this.blendRange = this.blendMax - this.blendMin;
        this.modRange = source.maxValue() + modulation.maxValue();
    }

    @Override
    public float modify(float x, float y, float value) {
        if (value <= blendMin) {
            return value;
        }

        float mask = this.mask.getValue(x, y);
        if (mask == 0) {
            return value;
        }

        float result = value;
        float slope = this.slope.getValue(x, y);
        float modulation = this.modulation.getValue(x, y);
        for (int i = 1; i <= octaves; i++) {
            result = getStepped(result, steps * i);
            result = getSloped(value, result, slope);
        }

        result = getModulated(result, modulation);

        float alpha = getAlpha(value);
        if (mask != 1) {
            alpha *= mask;
        }

        return NoiseUtil.lerp(value, result, alpha);
    }

    private float getModulated(float value, float modulation) {
        return (value + modulation) / modRange;
    }

    private float getStepped(float value, int steps) {
        value = NoiseUtil.round(value * steps);
        return value / steps;
    }

    private float getSloped(float value, float stepped, float slope) {
        float delta = (value - stepped);
        float amount = delta * slope;
        return stepped + amount;
    }

    private float getAlpha(float value) {
        if (value > blendMax) {
            return 1;
        }
        return (value - blendMin) / blendRange;
    }
}
