package ax.commons.structs;

import ax.commons.misc.ArrayUtils;

public class Array3Di {

    private final int[] array;

    private final int rows;
    private final int columns;
    private final int layers;

    public Array3Di(int rows, int columns, int layers) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new int[rows * columns * layers];
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
    }

    public int get(int i, int j, int k) {
        return array[k * rows * columns + j * rows + i];
    }

    public int getInArray(int i) {
        return array[i];
    }

    public void set(int i, int j, int k, int value) {
        array[k * rows * columns + j * rows + i] = value;
    }

    public void setInArray(int i, int value) {
        array[i] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getLayers() {
        return layers;
    }

    public int size() {
        return array.length;
    }

    /** @return array backing this object */
    public int[] getArray() {
        return array;
    }

    /** @return copy of array backing this object */
    public int[] toArray() {
        return ArrayUtils.copy(array);
    }

}
