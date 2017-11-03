package jonl.jutils.structs;

import java.util.Iterator;
import jonl.jutils.misc.ArrayUtils;

public class IntArray2D implements Iterable<Integer[]> {
    
    private int[][] array;
    private int index = 0;
    
    public IntArray2D(int initial) {
        array = new int[initial][];
    }
    
    public void put(int[] i) {
        array[index++] = i;
    }
    
    public void put(IntArray i) {
        array[index++] = i.toArray();
    }
    
    public void putAll(int[] ...values) {
        for (int[] i : values) {
            put(i);
        }
    }
    
    public void putAll(Iterable<Integer[]> values) {
        for (Integer[] i : values) {
            put(ArrayUtils.unwrap(i));
        }
    }
    
    public void reset() {
        index = 0;
    }
    
    public int[] get(int i) {
        return array[i];
    }
    
    public void set(int i, int[] v) {
        array[i] = v;
    }
    
    public boolean contains(int[] i) {
        for (int j=0; j<array.length; j++) {
            if (ArrayUtils.equals(array[j],i)) return true;
        }
        return false;
    }
    
    public int size() {
        return array.length;
    }
    
    public int length() {
        return array.length;
    }
    
    public int capacity() {
        return array.length;
    }
    
    public int[][] getArray() {
        return array;
    }
    
    public int[][] toArray() {
        return ArrayUtils.copy(array);
    }

    @Override
    public Iterator<Integer[]> iterator() {
        return new Iterator<Integer[]>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i<array.length;
            }
            @Override
            public Integer[] next() {
                return ArrayUtils.wrap(array[i++]);
            }
        };
    }
    
}
