package spec;

import org.junit.Test;
import util.SpecTester;

public class Modifier extends SpecTester {
    @Test
    public void abs() {
        test(n1().abs());
    }

    @Test
    public void alpha() {
        test(n1().alpha(n2()));
    }

    @Test
    public void bias() {
        test(n1().alpha(n2()));
    }

    @Test
    public void boost() {
        test(n1().boost(12));
    }

    @Test
    public void clamp() {
        test(n1().clamp(n2(), n3()));
    }

    @Test
    public void curve() {
        test(n1().curve(n2(), n3()));
    }

    @Test
    public void freq() {
        test(n1().freq(n2(), n3()));
    }

    @Test
    public void grad() {
        test(n1().grad(n1(), n2(), n3()));
    }

    @Test
    public void invert() {
        test(n1().invert());
    }

//    @Test
    public void legacyTerrace() {
        test(n1().legacyTerrace(n2(), n3(), 13, 0.5));
    }

    @Test
    public void map() {
        test(n1().map(n2(), n3()));
    }

    @Test
    public void modulate() {
        test(n1().mod(n2(), n3()));
    }

    @Test
    public void power() {
        test(n1().pow(n2()));
    }

    @Test
    public void powerCurve() {
        test(n1().powCurve(0.8));
    }

    @Test
    public void scale() {
        test(n1().scale(n2()));
    }

    @Test
    public void steps() {
        test(n1().steps(56));
    }

    @Test
    public void terrace() {
        test(n1().terrace(n2(), 0.2, 0.8, 0.5, 10));
    }

    @Test
    public void threshold() {
        test(n1().threshold(n2()));
    }

    @Test
    public void warp() {
        test(n1().warp(n1(), n2(), n3()));
    }
}
