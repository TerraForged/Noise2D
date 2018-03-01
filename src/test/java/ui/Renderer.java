package ui;

import me.dags.noise.Module;
import me.dags.noise.source.fast.CellType;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author dags <dags@dags.me>
 */
public class Renderer implements Viewer.Renderer {

    private final Module module;

    public Renderer(Module module) {
        this.module = module;
    }

    @Override
    public void accept(BufferedImage img, int xOff, int zOff) {
        // reset to black
        Viewer.clear(img);

        for (int z = 0; z < img.getWidth(); z++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int px = x + xOff;
                int pz = z + zOff;

                if (z == 256) {
                    // draw green line on plan where section is taken from
                    img.setRGB(x, z, Color.GREEN.getRGB());
                    float noise = module.getValue(px, pz);
                    int height = (int) (noise * 255);
                    // draw a section slice from the bottom of the image upwards
                    for (int y = 1; y <= height; y++) {
                        img.setRGB(x, img.getHeight() - y, Color.WHITE.getRGB());
                    }
                } else if ((px & 15) == 0 || (pz & 15) == 0) {
                    // leaves a 16x16 chunk grid
                    continue;
                } else {
                    // draw height-map
                    float noise = module.getValue(px, pz);
                    int gray = (int) (noise * 255);
                    Color color = new Color(gray, gray, gray);
                    img.setRGB(x, z, color.getRGB());
                }
            }
        }
    }

    public static void main(String[] args) {
        Module cell = Module.cell(1, 128, CellType.Distance2); // creates a Cell-Edge source module, seed:1, freq:1/128
        Module ridge = Module.ridge(2, 128, 3); // Ridge-Mutlti source module, seed:2, freq:1/128, octs:3
        Module perlin = Module.perlin(3, 96, 3); // Perlin source module, seed:3, freq:1/96, octs:3
        Module blend = perlin.blend(cell, ridge); // Create a Blend combiner using perlin to control the mix of cell & ridge
        Renderer renderer = new Renderer(blend.norm()); // Create a Normalize modifier of blend so that values range 0-1
        Viewer viewer = new Viewer(512, 512 + 256);
        viewer.setRenderer(renderer);
    }
}
