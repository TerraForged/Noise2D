package me.dags.noise.cache;

import me.dags.noise.Tagged;

import java.util.Collections;
import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class TagCache<T> extends ValueCache {

    private static final TagCache EMPTY = new TagCache();

    private List<T> lastTags = Collections.emptyList();

    public List<T> getTags() {
        return lastTags;
    }

    public void cacheTags(float x, float y, Tagged<T> module) {
        cacheTags(x, y, module.getTags(x, y));
    }

    public void cacheTags(float x, float y, List<T> tags) {
        lastX = x;
        lastY = y;
        lastTags = tags;
    }

    @SuppressWarnings("unchecked")
    public static <T> TagCache<T> empty() {
        return (TagCache<T>) EMPTY;
    }
}
