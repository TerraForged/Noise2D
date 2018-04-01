package me.dags.noise.tag;

import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;
import me.dags.noise.combiner.selector.Base;
import me.dags.noise.func.Interpolation;

import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagBase<T> extends Base implements Tagged<T> {

    private final List<T> mix;
    private final Tagged<T> lower;
    private final Tagged<T> upper;
    private final TagCache<T> cache = new TagCache<>();

    public TagBase(Tagged<T> lower, Tagged<T> upper, List<T> mix, float falloff, Interpolation interpolation) {
        super(lower, upper, falloff, interpolation);
        this.lower = lower;
        this.upper = upper;
        this.mix = mix;
    }

    @Override
    public String getName() {
        return "tag_base";
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
    protected void select(int index, float x, float y) {
        if (index == 0) {
            cache.cacheTags(x, y, lower);
        } else {
            cache.cacheTags(x, y, upper);
        }
    }

    @Override
    protected void select(int i0, int i1, float x, float y) {
        cache.cacheTags(x, y, mix);
    }
}
