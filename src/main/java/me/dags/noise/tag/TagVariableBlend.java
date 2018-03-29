package me.dags.noise.tag;

import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;
import me.dags.noise.combiner.selector.VariableBlend;
import me.dags.noise.func.Interpolation;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagVariableBlend<T> extends VariableBlend implements Tagged<T> {

    private final List<T> mix;
    private final Tagged<T> source0;
    private final Tagged<T> source1;
    private final TagCache<T> cache = new TagCache<>();

    public TagVariableBlend(Module control, Module blend, Tagged<T> source0, Tagged<T> source1, List<T> mix, float midpoint, float minBlend, float maxBlend, Interpolation interpolation) {
        super(control, blend, source0, source1, midpoint, minBlend, maxBlend, interpolation);
        this.source0 = source0;
        this.source1 = source1;
        this.mix = mix;
    }

    @Override
    public String getName() {
        return "tag_var_blend";
    }

    @Override
    public TagCache<T> getCache() {
        return cache;
    }

    @Override
    public List<T> getTags() {
        return mix;
    }

    @Override
    protected void select(Module module, float x, float y) {
        if (module == source0) {
            cache.cacheTags(x, y, source0);
        } else {
            cache.cacheTags(x, y, source1);
        }
    }

    @Override
    protected void select(Module module0, Module module1, float x, float y) {
        cache.cacheTags(x, y, mix);
    }
}
