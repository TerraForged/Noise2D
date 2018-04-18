package me.dags.noise.noisemap;

/**
 * @author dags <dags@dags.me>
 */
public interface Filter {

    void apply(NoiseMap map, int x, int y, Cell cell);

    Filter NONE = (map, x, y, cell) -> {};
    Filter BLUR1 = blur(1);
    Filter BLUR2 = blur(2);
    Filter BLUR3 = blur(3);

    static Filter blur(int radius) {
        if (radius < 1) {
            return NONE;
        }

        final float radius2 = radius * radius;
        return (map, x, y, cell) -> {
            float total = 0;
            float contributions = 0;

            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    int distance2 = dx * dx + dy * dy;
                    if (distance2 >= radius2) {
                        continue;
                    }

                    Cell other = map.getCell(x + dx, y + dy);
                    if (other.isPresent()) {
                        float contribution = 1 - (distance2 / radius2);
                        contributions += contribution;
                        total += (other.value * contribution);
                    }
                }
            }

            cell.value = total / contributions;
        };
    }
}
