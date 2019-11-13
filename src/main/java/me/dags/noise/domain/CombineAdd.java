package me.dags.noise.domain;

public class CombineAdd implements Domain {

    private final Domain a;
    private final Domain b;

    public CombineAdd(Domain a, Domain b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public float getOffsetX(float x, float y) {
        return a.getOffsetX(x, y) + b.getOffsetX(x, y);
    }

    @Override
    public float getOffsetY(float x, float y) {
        return a.getOffsetY(x, y) + b.getOffsetY(x, y);
    }
}
