package jonl.jutils.structs;

import jonl.jutils.misc.ArrayUtils;

public class Array2Db {

    private final boolean[] array;

    private final int rows;
    private final int columns;

    public Array2Db(boolean[] array, int rows) {
        int columns = (rows <= 0) ? 0 : array.length / rows;
        if (columns == 0)
            rows = 0;
        this.array = ArrayUtils.copy(array);
        this.rows = rows;
        this.columns = columns;
    }

    public Array2Db(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new boolean[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }
    
    public boolean get(int i, int j) {
        return array[j * rows + i];
    }

    public void set(int i, int j, boolean v) {
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
    public boolean[] getArray() {
        return array;
    }

    /** @return copy of array backing this object */
    public boolean[] toArray() {
        return ArrayUtils.copy(array);
    }

}
