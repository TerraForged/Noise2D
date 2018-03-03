package ui;

import me.dags.config.Config;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.combiner.Base;
import me.dags.noise.func.Interpolation;

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
        int sectionHeight = Math.abs(img.getHeight() - img.getWidth());

        for (int z = 0; z < img.getWidth(); z++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int px = x + xOff;
                int pz = z + zOff;

                if (z == 256) {
                    // draw green line on plan where section is taken from
                    img.setRGB(x, z, Color.GREEN.getRGB());

                    // use noise to vary the height of a column of white pixels, representing a section through
                    // the height map
                    float noise = module.getValue(px, pz);
                    int height = (int) (noise * sectionHeight);

                    // draw columns from bottom of image upwards selecting white if below 'height' level or
                    // blue if above
                    for (int y = sectionHeight; y > 1; y--) {
                        Color color = y > height ? Color.BLUE : Color.WHITE;
                        img.setRGB(x, img.getHeight() - y, color.getRGB());
                    }
                } else if ((px & 15) == 0 || (pz & 15) == 0) {
                    // black 16x16 chunk grid
                    img.setRGB(x, z, Color.BLACK.getRGB());
                } else {
                    // draw height-map using noise to vary gray-scale color
                    float noise = module.getValue(px, pz);

                    // norm() ensures noise is mapped and clamped between 0-1, would otherwise have to handle
                    // other values
                    int gray = (int) (noise * 255);
                    Color color = new Color(gray, gray, gray);
                    img.setRGB(x, z, color.getRGB());
                }
            }
        }
    }

    public static void main(String[] args) {
        Module root = example0();

        // visualize the output of 'root'
        Renderer renderer = new Renderer(root);

        // creates a 512x768 view:
        // 512x512 pixels for a plan view of the noise, 512x256 for a section view
        Viewer viewer = new Viewer(512, 512 + 256);
        viewer.setRenderer(renderer);
    }

    public static void test(Module module) {
        Renderer renderer = new Renderer(module);
        Viewer viewer = new Viewer(512, 512 + 256);
        viewer.setRenderer(renderer);
    }

    public static Module test0() {
        Module base = Source.perlin(1, 128, 3).norm().scale(0.25).bias(0.2);
        Module mountains = Source.ridge2(2, 512, 3).scale(0.8);
        return base.base(mountains, 0.2);
    }

    public static Module example0() {
        // create two noise modules to blend together
        // - the source0 produces low amplitude, rolling waves
        // - the source1 produces higher amplitude, more erratic waves
        Module source0 = Source.perlin(123, 160, 3).scale(0.25);
        Module source1 = Source.ridge(124, 256, 6).scale(1.5);

        // create a third module whose noise will be used to blend the above
        Module control = Source.perlin(125, 256, 1).norm();

        // select blends the two sources according to an S-curve
        // the 'bound' values control the position of the two curves
        // the 'falloff' value controls the transition range
        Module select = control.select(source0, source1, 0.6, 0.9, 0.1, Interpolation.QUINTIC);

        // write the 'select' module to a node tree and save
        Config config = Config.must("config.conf");
        select.save(config, "example");

        // load the module from a node tree
        Module example = Module.load(config, "example");

        // compare the input and output modules, only differences should be due to rounding
        System.out.println("Serialized:   " + select);
        System.out.println("Deserialized: " + example);

        // uniformly boost the noise values by 0.2 and clamp the output between 0.1 and 1.0
        return example.bias(0.2).clamp(0, 1);
    }

    public static Module example1() {
        Module lower = Source.perlin(123, 160, 1).norm().scale(0.2).bias(0.2);
        Module upper = Source.ridge(124, 256, 4).norm();
        return new Base(lower, upper, 0.4F).clamp(0, 1);
    }
}
