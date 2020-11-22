package com.terraforged.noise.func;

import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.cereal.value.DataValue;
import com.terraforged.noise.util.NoiseUtil;

public class SCurve implements CurveFunc {

    private final float lower;
    private final float upper;

    public SCurve(float lower, float upper) {
        this.lower = lower;
        this.upper = upper < 0 ? Math.max(-lower, upper) : upper;
    }

    @Override
    public String getSpecName() {
        return "SCurve";
    }

    @Override
    public float apply(float value) {
        return NoiseUtil.pow(value, lower + (upper * value));
    }

    private static final DataFactory<SCurve> factory = (data, spec, context) -> new SCurve(
            spec.get("lower", data, DataValue::asFloat),
            spec.get("upper", data, DataValue::asFloat)
    );

    public static DataSpec<SCurve> spec() {
        return DataSpec.builder("SCurve", SCurve.class, factory)
                .add("lower", 0, s -> s.lower)
                .add("upper", 1, s -> s.upper)
                .build();
    }
}
