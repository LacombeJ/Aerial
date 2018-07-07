package ax.commons.structs;

import java.util.Iterator;

import ax.commons.misc.ArrayUtils;

public class FloatArray implements Iterable<Float> {
    
    private float[] array;
    private int index = 0;
    
    public FloatArray(int initial) {
        array = new float[initial];
    }
    
    public void put(float i) {
        array[index++] = i;
    }
    
    public void put(float...values) {
        for (float f : values) {
            put(f);
        }
    }
    
    public void put(Iterable<Float> values) {
        for (float f : values) {
            put(f);
        }
    }
    
    public void reset() {
        index = 0;
    }
    
    public float get(int i) {
        return array[i];
    }
    
    public void set(int i, float v) {
        array[i] = v;
    }
    
    public boolean contains(float i) {
        for (int j=0; j<array.length; j++) {
            if (array[j]==i) return true;
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
    
    public float[] getArray() {
        return array;
    }
    
    public float[] toArray() {
        return ArrayUtils.copy(array);
    }

    @Override
    public Iterator<Float> iterator() {
        return new Iterator<Float>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i<array.length;
            }
            @Override
            public Float next() {
                return array[i++];
            }
        };
    }
    
}
