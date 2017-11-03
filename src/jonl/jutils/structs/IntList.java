package jonl.jutils.structs;

import java.util.Iterator;
import java.util.List;

import jonl.jutils.misc.MathUtils;

public class IntList implements Iterable<Integer> {
    
    private int[] list = new int[4];
    private int size = 0;
    
    public IntList() { }
    
    public IntList(int initial) {
        list = new int[initial];
    }
    
    public void insert(int index, int i) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,1);
        list[index] = i;
        size++;
    }
    
    public void insert(int index, int... numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.length);
        for (int i=0; i<numbers.length; i++) {
            list[i+index] = numbers[i];
        }
        size+=numbers.length;
    }
    
    public void insert(int index, IntList numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.size);
        for (int i=0; i<numbers.size; i++) {
            list[i+index] = numbers.get(i);
        }
        size+=numbers.size;
    }
    
    public void insert(int index, List<Integer> numbers) {
        if (index<0 || index>size) throw indexException(index);
        expandIfNecessary(index,numbers.size());
        for (int i=0; i<numbers.size(); i++) {
            list[i+index] = numbers.get(i);
        }
        size+=numbers.size();
    }
    
    public void addFirst(int i) {
        insert(0,i);
    }
    
    public void addFirst(IntList intList) {
        insert(0,intList);
    }
    
    public void addLast(int i) {
        insert(size,i);
    }
    
    public void addLast(IntList intList) {
        insert(size,intList);
    }
    
    public void add(int i) {
        addLast(i);
    }
    
    public void add(int index, int i) {
        insert(index,i);
    }
    
    public void put(int i) {
        addLast(i);
    }
    
    public void put(int... values) {
        for (int i : values) {
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
    
    public void removeFirstOf(int i) {
        for (int j=0; j<size; j++) {
            if (list[j]==i) {
                remove(j);
                break;
            }
        }
    }
    
    public void removeLastOf(int i) {
        for (int j=size-1; j>=0; j--) {
            if (list[j]==i) {
                remove(j);
                break;
            }
        }
    }
    
    public void removeAllOf(int i) {
        int j=0;
        while (j<size) {
            if (list[j]==i) {
                remove(j);
            } else {
                j++;
            }
        }
    }
    
    public int get(int index) {
        if (index<0 || index>=size) throw indexException(index);
        return list[index];
    }
    
    public void set(int index, int i) {
        if (index<0 || index>=size) throw indexException(index);
        list[index] = i;
    }
    
    public boolean contains(int i) {
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
            int[] newList = new int[max*2];
            System.arraycopy(list,0,newList,0,from);
            System.arraycopy(list,from,newList,from+length,size-from);
            list = newList;
        }
    }
    
    public int[] toArray() {
        int[] array = new int[size];
        System.arraycopy(list,0,array,0,size);
        return array;
    }
    
    /**
     * The backing array of this list may have a length larger than this size of this array
     * @return the backing array of this list
     */
    public int[] getBackingArray() {
        return list;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index<size;
            }
            @Override
            public Integer next() {
                return list[index++];
            }
        };
    }
    
    public IndexOutOfBoundsException indexException(int index) {
        return new IndexOutOfBoundsException("Targ: "+index+", Size: "+size);
    }
    
}
