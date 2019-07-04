package me.dags.noise.source;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

public class FastSin extends FastSource {

    private final Module alpha;

    public FastSin(Builder builder) {
        super(builder);
        alpha = builder.getSource();
    }

    @Override
    public float getValue(float x, float y) {
        float a = alpha.getValue(x, y);
        x *= frequency;
        y *= frequency;

        final float noise;
        if (a == 0) {
            noise = NoiseUtil.sin(x);
        } else if (a == 1) {
            noise =NoiseUtil.sin(y);
        } else {
            float sx = NoiseUtil.sin(x);
            float sy = NoiseUtil.sin(y);
            noise = NoiseUtil.lerp(sx, sy, a);
        }

        return NoiseUtil.map(noise, -1, 1, 2);
    }
}
