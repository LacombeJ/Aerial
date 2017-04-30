package jonl.jutils.structs;

import java.util.Arrays;

public class Array3D<T> {

    private final Object[] array;

    private final int rows;
    private final int columns;
    private final int layers;

    public Array3D(int rows, int columns, int layers) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new Object[rows * columns * layers];
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
    }

    @SuppressWarnings("unchecked")
    public T get(int i, int j, int k) {
        return (T) array[k * rows * columns + j * rows + i];
    }

    public void set(int i, int j, int k, T obj) {
        array[k * rows * columns + j * rows + i] = obj;
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

    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

}
