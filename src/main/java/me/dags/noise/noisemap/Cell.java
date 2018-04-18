package me.dags.noise.noisemap;

/**
 * @author dags <dags@dags.me>
 */
public class Cell {

    public static final Cell EMPTY = new Cell();

    public float value = 0F;

    public boolean isPresent() {
        return this != EMPTY;
    }
}
