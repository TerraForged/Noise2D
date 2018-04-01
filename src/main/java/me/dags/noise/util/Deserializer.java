package me.dags.noise.util;

import me.dags.config.Node;
import me.dags.noise.Module;
import me.dags.noise.Source;
import me.dags.noise.combiner.*;
import me.dags.noise.combiner.selector.Base;
import me.dags.noise.combiner.selector.Blend;
import me.dags.noise.combiner.selector.Select;
import me.dags.noise.func.CellFunc;
import me.dags.noise.func.DistanceFunc;
import me.dags.noise.func.EdgeFunc;
import me.dags.noise.func.Interpolation;
import me.dags.noise.modifier.*;
import me.dags.noise.source.Builder;
import me.dags.noise.source.Constant;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * @author dags <dags@dags.me>
 */
public class Deserializer {

    private static final AtomicReference<Deserializer> deserializer = new AtomicReference<>(new Deserializer());
    private static final Set<String> combiners = Util.set("add", "blend", "max", "min", "mult", "select", "sub");
    private static final Set<String> modifiers = Util.set("abs", "bias", "clamp", "invert", "map", "norm", "pow", "scale", "steps", "turb");
    private static final Set<String> sources = Util.set("const", "billow", "cell", "cell_edge", "cubic", "perlin", "ridge");

    public static Deserializer getInstance() {
        return deserializer.get();
    }

    public static void setInstance(Deserializer deserializer) {
        Deserializer.deserializer.set(deserializer);
    }

    public Module deserialize(Node node) {
        String type = node.get("module", "none");

        if (sources.contains(type)) {
            return source(type, node);
        }

        if (combiners.contains(type)) {
            return combiner(type, node);
        }

        if (modifiers.contains(type)) {
            return modifier(type, node);
        }

        return Builder.SOURCE;
    }

    public Source source(String type, Node node) {
        Builder builder = builder(node);

        switch (type) {
            case "const":
                return new Constant(node.get("value", 0F));
            case "billow":
                return builder.billow();
            case "cell":
                return builder.cell();
            case "cell_edge":
                return builder.cellEdge();
            case "cubic":
                return builder.cubic();
            case "perlin":
                return builder.perlin();
            case "ridge":
                return builder.ridge();
            default:
                return Builder.SOURCE;
        }
    }

    public Module combiner(String type, Node node) {
        List<Node> nodes = node.node("sources").childList();
        Module[] modules = new Module[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            modules[i] = deserialize(nodes.get(i));
        }

        switch (type) {
            case "add":
                return new Add(modules);
            case "base":
                float baseFalloff = node.get("falloff", 0.25F);
                Module baseLower = deserialize(node.node("lower"));
                Module baseUpper = deserialize(node.node("upper"));
                Interpolation baseInterp = getEnum(node, "interp", Select.INTERPOLATION, Interpolation::valueOf);
                return new Base(baseLower, baseUpper, baseFalloff, baseInterp);
            case "blend":
                if (modules.length != 2) {
                    break;
                }
                float mid = node.get("center", 0.5F);
                float blend = node.get("blend", 0.5F);
                Module blendCtrl = deserialize(node.node("selector"));
                return new Blend(blendCtrl, modules[0], modules[1], mid, blend, Interpolation.LINEAR);
            case "max":
                return new Max(modules);
            case "min":
                return new Min(modules);
            case "mult":
                return new Multiply(modules);
            case "select":
                if (modules.length != 2) {
                    break;
                }
                Module selectCtrl = deserialize(node.node("selector"));
                float lower = node.get("boundLower", 0F);
                float upper = node.get("boundUpper", 1F);
                float falloff = node.get("falloff", 0F);
                Interpolation selInterp = getEnum(node, "interp", Select.INTERPOLATION, Interpolation::valueOf);
                return new Select(selectCtrl, modules[0], modules[1], lower, upper, falloff, selInterp);
            case "sub":
                return new Sub(modules);
        }
        return Builder.SOURCE;
    }

    public Module modifier(String type, Node node) {
        Module source = deserialize(node.node("getSource"));
        if (source == Builder.SOURCE) {
            return source;
        }

        switch (type) {
            case "abs":
                return new Abs(source);
            case "bias":
                float bias = node.get("bias", 0F);
                return new Bias(source, bias);
            case "clamp":
                float min = node.get("min", source.minValue());
                float max = node.get("max", source.maxValue());
                return new Clamp(source, min, max);
            case "invert":
                return new Invert(source);
            case "map":
                float outMin = node.get("min", 0F);
                float outMax = node.get("max", 1F);
                return new me.dags.noise.modifier.Map(source, outMin, outMax);
            case "pow":
                float n = node.get("n", 1F);
                return new Power(source, n);
            case "scale":
                float scale = node.get("scale", 1F);
                return new Scale(source, scale);
            case "steps":
                int steps = node.get("count", 1);
                return new Steps(source, steps);
            case "turb":
                Module x = deserialize(node.node("x"));
                Module y = deserialize(node.node("y"));
                float power = node.get("power", 1F);
                return new Turbulence(source, x, y, power);
            default:
                return Builder.SOURCE;
        }
    }

    public Builder builder(Node node) {
        Builder builder = Source.builder();
        builder.seed(node.get("seed", Builder.SEED));
        builder.gain(node.get("gain", Builder.GAIN));
        builder.octaves(node.get("octaves", Builder.OCTAVES));
        builder.frequency(node.get("frequency", Builder.FREQUENCY));
        builder.lacunarity(node.get("lacunarity", Builder.LACUNARITY));
        builder.cellFunc(getEnum(node, "cell", Builder.CELL_FUNC, CellFunc::valueOf));
        builder.edgeFunc(getEnum(node, "edge", Builder.EDGE_FUNC, EdgeFunc::valueOf));
        builder.distFunc(getEnum(node, "dist", Builder.DIST_FUNC, DistanceFunc::valueOf));
        builder.interp(getEnum(node, "interp", Builder.INTERP, Interpolation::valueOf));

        Node child = node.node("source");
        if (!child.isEmpty()) {
            Module source = deserialize(child);
            builder.source(source);
        }

        return builder;
    }

    private static <T extends Enum<T>> T getEnum(Node node, String key, T def, Function<String, T> func) {
        String value = node.get(key, def.toString());
        return func.apply(value);
    }
}
