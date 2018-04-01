package example;

import me.dags.noise.Source;
import me.dags.noise.Tagged;
import me.dags.noise.func.Interpolation;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author dags <dags@dags.me>
 */
public class Advanced {

    private static final Color GREEN = new Color(96, 180, 64);
    private static final Color BLUE = new Color(96, 128, 240);
    private static final Color YELLOW = new Color(220, 180, 90);
    private static final Random random = new Random();

    public static void main(String[] args) {
        Viewer viewer = new Viewer(512, 512+256);
        viewer.setRenderer(render(test1()));
    }

    private static Tagged<Color> test0() {
        Source.SEED_RANDOM.setSeed(123);
        Tagged<Color> lowland = Source.perlin(128, 1).scale(0.05).bias(0.1).tag(BLUE);
        Tagged<Color> highland = Source.perlin(128, 2).scale(0.1).bias(0.25).tag(GREEN);
        return Source.perlin(512, 1)
                .turbulence(Source.perlin(32, 1), 24)
                .tagVarBlend(Source.perlin(512, 1), lowland, highland, Collections.singletonList(YELLOW), 0.5, 0.01, 0.3, Interpolation.CURVE3);
    }

    private static Tagged<Color> test1() {
        Source.SEED_RANDOM.setSeed(123);
        return Source.perlin(512, 1)
                .tagMultiBlend(0.35, Arrays.asList(
                        Source.constant(0.1).tag(BLUE, YELLOW),
                        Source.constant(0.3).tag(YELLOW),
                        Source.constant(0.6).tag(GREEN)
                ));
    }

    private static Viewer.Renderer render(Tagged<Color> source) {
        return (buffer, xOff, zOff) -> {
            Viewer.clear(buffer, Color.BLACK);

            int planWidth = buffer.getWidth();
            int planHeight = buffer.getHeight();
            int sectionLine = -1;
            int sectionHeight = -1;

            if (planHeight > planWidth) {
                sectionHeight = planHeight - planWidth;
                planHeight = planHeight - sectionHeight;
                sectionLine = planHeight / 2;
            }

            for (int dz = 0; dz < planHeight; dz++) {
                for (int dx = 0; dx < planWidth; dx++) {
                    int x = dx + xOff * 3;
                    int z = dz + zOff * 3;
                    float noise = source.getValue(x, z);
                    int height = Math.round(255 * noise);

                    Color c = getColor(source, x, z);
                    c = shade(c, noise, 1.5F, 0.2F);

                    if (((x & 15) == 0) || ((z & 15) == 0)) {
                        buffer.setRGB(dx, dz, Color.BLACK.getRGB());
                    } else if (height < 64) {
                        buffer.setRGB(dx, dz, Color.BLUE.getRGB());
                    } else {
                        buffer.setRGB(dx, dz, c.getRGB());
                    }

                    if (dz == sectionLine && sectionHeight > 0) {
                        buffer.setRGB(dx, dz, Color.GREEN.getRGB());
                        int surface = Math.max(64, height);

                        for (int dy = surface; dy > 0; dy--) {
                            Color col = c;
                            if (dy > height) {
                                col = Color.BLUE;
                            }
                            int y = planHeight + sectionHeight - dy;
                            buffer.setRGB(dx, y, col.getRGB());
                        }
                    }
                }
            }
        };
    }

    private static Color getColor(Tagged<Color> tagged, int x, int z) {
        List<Color> list = tagged.getTags(x, z);
        if (list.isEmpty()) {
            return Color.BLACK;
        }
        if (list.size() == 1) {
            return list.get(0);
        }
        return list.get(random.nextInt(list.size()));
    }

    private static Color shade(Color color, float amount, float range, float min) {
        float shade = min + amount * range;
        int r = Math.min(255, Math.round(color.getRed() * shade));
        int g = Math.min(255, Math.round(color.getGreen() * shade));
        int b = Math.min(255, Math.round(color.getBlue() * shade));
        return new Color(r, g, b);
    }
}