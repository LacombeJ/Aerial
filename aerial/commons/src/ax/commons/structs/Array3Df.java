package ax.commons.structs;

import ax.commons.misc.ArrayUtils;

public class Array3Df {

    private final float[] array;

    private final int rows;
    private final int columns;
    private final int layers;

    public Array3Df(int rows, int columns, int layers) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new float[rows * columns * layers];
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
    }

    public float get(int i, int j, int k) {
        return array[k * rows * columns + j * rows + i];
    }

    public float getInArray(int i) {
        return array[i];
    }

    public void set(int i, int j, int k, float value) {
        array[k * rows * columns + j * rows + i] = value;
    }

    public void setInArray(int i, float value) {
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
    public float[] getArray() {
        return array;
    }

    /** @return copy of array backing this object */
    public float[] toArray() {
        return ArrayUtils.copy(array);
    }

}
