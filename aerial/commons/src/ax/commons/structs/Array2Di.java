package ax.commons.structs;

import ax.commons.misc.ArrayUtils;

public class Array2Di {

    private final int[] array;

    private final int rows;
    private final int columns;

    public Array2Di(int[] array, int rows) {
        int columns = (rows <= 0) ? 0 : array.length / rows;
        if (columns == 0)
            rows = 0;
        this.array = ArrayUtils.copy(array);
        this.rows = rows;
        this.columns = columns;
    }

    public Array2Di(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new int[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }
    
    public int get(int i, int j) {
        return array[j * rows + i];
    }

    public void set(int i, int j, int v) {
        array[j * rows + i] = v;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
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
