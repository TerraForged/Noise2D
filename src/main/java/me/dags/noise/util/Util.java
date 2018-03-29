package me.dags.noise.util;

import com.google.common.collect.ImmutableList;
import me.dags.config.Node;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dags <dags@dags.me>
 */
public class Util {

    private static final int DECIMAL_PLACES = 100000000;

    public static <T> List<T> combine(List<T> a, List<T> b) {
        return ImmutableList.<T>builder().addAll(a).addAll(b).build();
    }

    public static double round5(float value) {
        double round = NoiseUtil.FastRound(value * DECIMAL_PLACES);
        return round / DECIMAL_PLACES;
    }

    public static void setNonDefault(Node node, String name, int val, int def) {
        if (val != def) {
            node.set(name, val);
        }
    }

    public static void setNonDefault(Node node, String name, float val, float def) {
        if (Math.abs(val - def) > 0.0001) {
            node.set(name, round5(val));
        }
    }

    public static <T extends Enum<T>> void setNonDefault(Node node, String name, T val, T def) {
        if (val != def) {
            node.set(name, val.toString());
        }
    }

    public static Set<String> set(String... names) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, names);
        return Collections.unmodifiableSet(set);
    }
}
