package me.dags.noise.util;

import com.google.common.collect.ImmutableList;
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
        if (a.isEmpty()) {
            return b;
        }
        if (b.isEmpty()) {
            return a;
        }
        return ImmutableList.<T>builder().addAll(a).addAll(b).build();
    }

    public static double round5(float value) {
        double round = NoiseUtil.FastRound(value * DECIMAL_PLACES);
        return round / DECIMAL_PLACES;
    }

    public static Set<String> set(String... names) {
        Set<String> set = new HashSet<>();
        Collections.addAll(set, names);
        return Collections.unmodifiableSet(set);
    }
}
