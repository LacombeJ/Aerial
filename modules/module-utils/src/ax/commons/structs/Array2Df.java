package ax.commons.structs;

import ax.commons.misc.ArrayUtils;

public class Array2Df {

    private final float[] array;

    private final int rows;
    private final int columns;

    public Array2Df(float[] array, int rows) {
        int columns = (rows <= 0) ? 0 : array.length / rows;
        if (columns == 0)
            rows = 0;
        this.array = ArrayUtils.copy(array);
        this.rows = rows;
        this.columns = columns;
    }

    public Array2Df(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new float[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }
    
    public float get(int i, int j) {
        return array[j * rows + i];
    }

    public void set(int i, int j, float f) {
        array[j * rows + i] = f;
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
    public float[] getArray() {
        return array;
    }

    /** @return copy of array backing this object */
    public float[] toArray() {
        return ArrayUtils.copy(array);
    }

}
