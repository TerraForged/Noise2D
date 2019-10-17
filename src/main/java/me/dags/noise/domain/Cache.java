package me.dags.noise.domain;

public class Cache implements Domain {

    private final Domain domain;
    private boolean cached = false;
    private float cachedX;
    private float cachedY;
    private float x;
    private float y;

    public Cache(Domain domain) {
        this.domain = domain;
    }

    @Override
    public float getX(float x, float y) {
        if (cached && x == this.x && y == this.y) {
            return cachedX;
        }
        this.x = x;
        this.y = y;
        this.cachedX = domain.getX(x, y);
        return cachedX;
    }

    @Override
    public float getY(float x, float y) {
        if (cached && x == this.x && y == this.y) {
            return cachedY;
        }
        this.x = x;
        this.y = y;
        this.cachedY = domain.getY(x, y);
        return cachedY;
    }
}
