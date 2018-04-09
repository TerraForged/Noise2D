package me.dags.noise.tag;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import me.dags.noise.Module;
import me.dags.noise.Tagged;
import me.dags.noise.combiner.selector.MultiBlend;
import me.dags.noise.func.Interpolation;
import me.dags.noise.util.Util;

/**
 * @Author <dags@dags.me>
 */
public class TagMultiBlend<T> extends MultiBlend implements Tagged<T> {

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
        this.mixes = builder.build();
    }

    @Override
    public List<T> getTags() {
        return Collections.emptyList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getTags(float x, float y) {
        return (List<T>) super.getTags(x, y);
    }

    @Override
    protected List<T> blendTags(int index0, int index1, float alpha) {
        int index = Math.min(index0, index1);
        return mixes.get(index);
    }
}
