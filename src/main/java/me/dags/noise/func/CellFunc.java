package me.dags.noise.func;

import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;
import me.dags.noise.util.Vec2f;

/**
 * @author dags <dags@dags.me>
 */
public enum CellFunc {
    CELL_VALUE {
        @Override
        public float apply(int xc, int yc, float distance, int seed, Vec2f vec2f, Module lookup) {
            return NoiseUtil.valCoord2D(seed, xc, yc);
        }
    },
    NOISE_LOOKUP {
        @Override
        public float apply(int xc, int yc, float distance, int seed, Vec2f vec2f, Module lookup) {
            return lookup.getValue(xc + vec2f.x, yc + vec2f.y);
        }
    },
    DISTANCE {
        @Override
        public float apply(int xc, int yc, float distance, int seed, Vec2f vec2f, Module lookup) {
            return distance - 1;
        }
    },
    ;

    public abstract float apply(int xc, int yc, float distance, int seed, Vec2f vec2f, Module lookup);
}
