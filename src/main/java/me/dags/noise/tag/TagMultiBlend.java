package me.dags.noise.tag;

import com.google.common.collect.ImmutableList;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.cache.TagCache;
import me.dags.noise.combiner.selector.MultiBlend;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

import java.util.Collections;
import java.util.List;

/**
 * @Author <dags@dags.me>
 */
public class TagMultiBlend<T> extends MultiBlend implements Tagged<T> {

    private final TagCache<T> cache = new TagCache<>();
    private final List<Tagged<T>> nodes;
    private final List<List<T>> mixes;

    public TagMultiBlend(Module control, List<Tagged<T>> sources, float blend, Interpolation interpolation) {
        super(blend, interpolation, control, sources.toArray(new Module[sources.size()]));
        ImmutableList.Builder<List<T>> builder = ImmutableList.builder();
        for (int i = 0; i < sources.size(); i++) {
            if (i + 1 < sources.size()) {
                List<T> mix = Util.combine(sources.get(i).getTags(), sources.get(i + 1).getTags());
                builder.add(mix);
            }
        }
        this.nodes = ImmutableList.copyOf(sources);
        this.mixes = builder.build();
    }

    @Override
    public void select(int index, float x, float y) {
        getCache().cacheTags(x, y, nodes.get(index).getTags(x, y));
    }

    @Override
    public void select(int index0, int index1, float x, float y) {
        int index = Math.min(index0, index1);
        getCache().cacheTags(x, y, mixes.get(index));
    }

    @Override
    public TagCache<T> getCache() {
        return cache;
    }

    @Override
    public List<T> getTags() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "multi_blend";
    }
}
