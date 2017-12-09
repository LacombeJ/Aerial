package jonl.jutils.structs;

import java.util.Iterator;
import java.util.List;

import jonl.jutils.misc.MathUtils;

public class FloatList implements Iterable<Float> {
    
    private float[] list = new float[4];
    private int size = 0;
    
    public FloatList() { }
    
    public FloatList(int initial) {
        list = new float[initial];
    }
    
    public void insert(int index, float i) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,1);
        list[index] = i;
        size++;
    }
    
    public void insert(int index, float... numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.length);
        for (int i=0; i<numbers.length; i++) {
            list[i+index] = numbers[i];
        }
        size+=numbers.length;
    }
    
    public void insert(int index, FloatList numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.size);
        for (int i=0; i<numbers.size; i++) {
            list[i+index] = numbers.get(i);
        }
        size+=numbers.size;
    }
    
    public void insert(int index, List<Float> numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.size());
        for (int i=0; i<numbers.size(); i++) {
            list[i+index] = numbers.get(i);
        }
        size+=numbers.size();
    }
    
    public void addFirst(float i) {
        insert(0,i);
    }
    
    public void addFirst(FloatList intList) {
        insert(0,intList);
    }
    
    public void addLast(float i) {
        insert(size,i);
    }
    
    public void addLast(FloatList intList) {
        insert(size,intList);
    }
    
    public void add(float i) {
        addLast(i);
    }
    
    public void add(int index, float i) {
        insert(index,i);
    }
    
    public void put(float i) {
        addLast(i);
    }
    
    public void put(float... values) {
        for (float i : values) {
            put(i);
        }
    }
    
    public void delete(int index, int length) {
        if (index<0 || index+length>size) throw indexException(index+length);
        if (index+length!=size) {
            System.arraycopy(list,index+length,list,index,size-length);
        }
        size-=length;
    }
    
    public void removeFirst() {
        delete(0,1);
    }
    
    public void removeLast() {
        delete(size-1,1);
    }
    
    public void remove(int index) {
        delete(index,1);
    }
    
    public void remove(int index, int length) {
        delete(index,length);
    }
    
    public void removeFirstOf(float i) {
        for (int j=0; j<size; j++) {
            if (list[j]==i) {
                remove(j);
                break;
            }
        }
    }
    
    public void removeLastOf(float i) {
        for (int j=size-1; j>=0; j--) {
            if (list[j]==i) {
                remove(j);
                break;
            }
        }
    }
    
    public void removeAllOf(float i) {
        int j=0;
        while (j<size) {
            if (list[j]==i) {
                remove(j);
            } else {
                j++;
            }
        }
    }
    
    public float get(int index) {
        if (index<0 || index>=size) throw indexException(index);
        return list[index];
    }
    
    public void set(int index, float i) {
        if (index<0 || index>=size) throw indexException(index);
        list[index] = i;
    }
    
    public boolean contains(float i) {
        for (int j=0; j<size; j++) {
            if (list[j]==i) return true;
        }
        return false;
    }
    
    public int size() {
        return size;
    }
    
    public int capacity() {
        return list.length;
    }
    
    public boolean isEmpty() {
        return size==0;
    }
    
    private void expandIfNecessary(int from, int length) {
        if (size+length>list.length || from<=size) {
            int pow = length;
            if (!MathUtils.isPowerOfTwo(pow)) {
                pow = MathUtils.nextPowerOfTwo(pow);
            }
            int max = Math.max(pow,size);
            float[] newList = new float[max*2];
            System.arraycopy(list,0,newList,0,from);
            System.arraycopy(list,from,newList,from+length,size-from);
            list = newList;
        }
    }
    
    public float[] toArray() {
        float[] array = new float[size];
        System.arraycopy(list,0,array,0,size);
        return array;
    }
    
    public float[] getBackingArray() {
        return list;
    }

    @Override
    public Iterator<Float> iterator() {
        return new Iterator<Float>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index<size;
            }
            @Override
            public Float next() {
                return list[index++];
            }
        };
    }
    
    public IndexOutOfBoundsException indexException(int index) {
        return new IndexOutOfBoundsException("Targ: "+index+", Size: "+size);
    }
    
}
