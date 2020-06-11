import com.terraforged.cereal.Cereal;
import com.terraforged.cereal.spec.Context;
import com.terraforged.n2d.Module;
import com.terraforged.n2d.Source;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class Tester {

    public static void main(String[] args) throws IOException {
        Module module = Source.line(-0.4, -0.3, 0.9, 20.5, 10, 0.2, 0.1)
                .warp(123, 100, 2, 50)
                .blend(0.2, Source.perlin(324, 300, 3), Source.ridge(324, 300, 3));

        for (int i = 0; i < 1; i++) {
            String text = write(module);
            System.out.println(text);
            System.out.println("\n\n");
            module = read(text, Context.NONE);
            System.out.println(module);
            System.out.println("\n\n==============================================\n\n");
        }
    }

    private static String write(Module module) throws IOException {
        StringWriter writer = new StringWriter();
        Cereal.write(module, writer);
        return writer.toString();
    }

    private static Module read(String string, Context context) throws IOException {
        StringReader reader = new StringReader(string);
        return Cereal.read(reader, Module.class, context);
    }
}
