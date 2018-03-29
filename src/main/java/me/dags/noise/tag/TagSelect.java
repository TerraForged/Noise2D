package me.dags.noise.tag;

import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;
import me.dags.noise.combiner.selector.Select;
import me.dags.noise.func.Interpolation;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagSelect<T> extends Select implements Tagged<T> {

    private final List<T> mix;
    private final Tagged<T> source0;
    private final Tagged<T> source1;
    private final TagCache<T> cache = new TagCache<>();

    public TagSelect(Module control, Tagged<T> source0, Tagged<T> source1, List<T> mix, float lower, float upper, float falloff, Interpolation interpolation) {
        super(control, source0, source1, lower, upper, falloff, interpolation);
        this.source0 = source0;
        this.source1 = source1;
        this.mix = mix;
    }

    @Override
    public String getName() {
        return "tag_select";
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
    protected void select(Module source0, Module source1, float x, float y) {
        cache.cacheTags(x, y, mix);
    }
}
