package me.dags.noise.modifier;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class PowerCurve extends Modifier {

    private final float min;
    private final float max;
    private final float mid;
    private final float range;
    private final float power;

    public PowerCurve(Module source, float power) {
        super(source);
        float min = source.minValue();
        float max = source.maxValue();
        float mid = min + ((max - min) / 2F);
        this.power = power;
        this.min = mid - pow(mid - source.minValue(), power);
        this.max = mid + pow(source.maxValue() - mid, power);
        this.range = this.max - this.min;
        this.mid = this.min + (range / 2F);
    }

    @Override
    public float modify(float x, float y, float value) {
        if (value >= mid) {
            float part = value - mid;
            value = mid + (float) Math.pow(part, power);
        } else {
            float part = mid - value;
            value = mid - (float) Math.pow(part, power);
        }
        return NoiseUtil.map(value, min, max, range);
    }

    private static float pow(float value, float power) {
        return (float) Math.pow(value, power);
    }
}
