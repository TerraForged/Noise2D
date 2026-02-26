import com.terraforged.noise.Module;
import com.terraforged.noise.Source;
import com.terraforged.noise.util.N2DUtil;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Module noise = Source.simplex(1231, 200, 3)
                        .warp(3123, 50, 3, 25)
                        .steps(12);

        N2DUtil.display(512, 512, (x, z, ctx) -> {
            ctx.setRGB(x, z, Color.HSBtoRGB(noise.getValue(12312, x, z), 0.7f, 0.7f));
        }).setVisible(true);
    }
}
