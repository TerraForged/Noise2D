package me.dags.noise.domain;

public class CombineWarp implements Domain {

    private final Domain a;
    private final Domain b;

    public CombineWarp(Domain a, Domain b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float getOffsetX(float x, float y) {
        float ax = a.getX(x, y);
        float ay = a.getY(x, y);
        return b.getOffsetX(ax, ay);
    }

    @Override
    public float getOffsetY(float x, float y) {
        float ax = a.getX(x, y);
        float ay = a.getY(x, y);
        return b.getOffsetY(ax, ay);
    }
}
