package example;

import java.awt.Color;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.func.EdgeFunc;

/**
 * @author dags <dags@dags.me>
 */
public class Example {

    public static void main(String[] args) {
        Module cell = Source.cellEdge(1, 256, EdgeFunc.DISTANCE_2_ADD);
        Module ridge = Source.ridge(2, 256 + 128, 4);
        Module source = cell.mult(ridge).scale(0.7).bias(0.25);
        Viewer viewer = new Viewer(512, 768);
        viewer.setRenderer(render(source));
    }

    private static Viewer.Renderer render(Module source) {
        Module module = source.clamp(0, 1);
        return (buffer, xOff, zOff) -> {
            Viewer.clear(buffer, Color.BLUE);

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

                    float noise = module.getValue(x, z);
                    int gray = (int) (255 * noise);
                    Color color = new Color(gray, gray, gray);

                    if (((x & 15) == 0) || ((z & 15) == 0)) {
                        buffer.setRGB(dx, dz, Color.BLACK.getRGB());
                    } else {
                        buffer.setRGB(dx, dz, color.getRGB());
                    }

                    if (dz == sectionLine && sectionHeight > 0) {
                        buffer.setRGB(dx, dz, Color.GREEN.getRGB());

                        for (int dy = (int) (noise * sectionHeight); dy > 0; dy--) {
                            int y = planHeight + sectionHeight - dy;
                            buffer.setRGB(dx, y, color.getRGB());
                        }
                    }
                }
            }
        };
    }
}
