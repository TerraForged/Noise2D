package me.dags.noise.noisemap;

/**
 * @author dags <dags@dags.me>
 */
public interface CellVisitor {

    void visit(int x, int y, Cell cell);
}
