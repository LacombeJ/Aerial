package jonl.jutils.structs;

import java.util.Arrays;

public class Array2D<T> {

    private final Object[] array;

    private final int rows;
    private final int columns;

    public Array2D(Object[] array, int rows) {
        int columns = (rows <= 0) ? 0 : array.length / rows;
        if (columns == 0)
            rows = 0;
        this.array = new Object[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }

    public Array2D(int rows, int columns) {
        if (rows <= 0 || columns <= 0) {
            rows = 0;
            columns = 0;
        }
        array = new Object[rows * columns];
        this.rows = rows;
        this.columns = columns;
    }

    @SuppressWarnings("unchecked")
    public T get(int i, int j) {
        return (T) array[j * rows + i];
    }

    public void set(int i, int j, T obj) {
        array[j * rows + i] = obj;
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

    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

}
