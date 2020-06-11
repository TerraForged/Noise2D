package com.terraforged.n2d.modifier;

import com.terraforged.cereal.spec.DataFactory;
import com.terraforged.cereal.spec.DataSpec;
import com.terraforged.n2d.Module;

public class Freq extends Modifier {

    private final Module x;
    private final Module y;

    public Freq(Module source, Module x, Module y) {
        super(source);
        this.x = x;
        this.y = y;
    }

    @Override
    public String getSpecName() {
        return "Freq";
    }

    @Override
    public float getValue(float x, float y) {
        float fx = this.x.getValue(x, y);
        float fy = this.y.getValue(x, y);
        return source.getValue(x * fx, y * fy);
    }

    @Override
    public float modify(float x, float y, float noiseValue) {
        return 0;
    }

    private static final DataFactory<Freq> factory = (data, spec, context) -> new Freq(
            spec.get("source", data, Module.class, context),
            spec.get("x", data, Module.class, context),
            spec.get("y", data, Module.class, context)
    );

    public static DataSpec<Freq> spec() {
        return sourceBuilder(Freq.class, factory)
                .addObj("x", f -> f.x)
                .addObj("y", f -> f.y)
                .build();
    }
}
