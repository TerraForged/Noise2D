package me.dags.noise.noisemap;

import me.dags.noise.Module;

/**
 * @author dags <dags@dags.me>
 */
public class NoiseMap {

    private final Cell[] cells;
    private final int width;
    private final int height;

    public NoiseMap(int width, int height) {
        this.cells = createBacking(width, height);
        this.width = width;
        this.height = height;
    }

    protected Cell[] createBacking(int width, int height) {
        return new Cell[width * height];
    }

    protected Cell createCell() {
        return new Cell();
    }

    protected Cell getOrCreate(int index) {
        Cell cell = cells[index];
        if (cell == null) {
            cell = createCell();
            cells[index] = cell;
        }
        return cell;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getCell(int x, int y) {
        int index = (y * width) + x;
        if (index < 0 || index >= cells.length) {
            return Cell.EMPTY;
        }
        return getOrCreate(index);
    }

    public void iterate(CellVisitor visitor) {
        int index = 0;
        for (int dy = 0; dy < getHeight(); dy++) {
            for (int dx = 0; dx < getWidth(); dx++) {
                Cell cell = getOrCreate(index);
                visitor.visit(dx, dy, cell);
                index++;
            }
        }
    }

    public void populate(Module source, int xOffset, int yOffset) {
        iterate((dx, dy, cell) -> {
            int x = xOffset + dx;
            int y = yOffset + dy;
            cell.value = source.getValue(x, y);
        });
    }

    public void filter(Filter filter) {
        if (filter == Filter.NONE) {
            return;
        }
        iterate((x, z, cell) -> filter.apply(this, x, z, cell));
    }
}
