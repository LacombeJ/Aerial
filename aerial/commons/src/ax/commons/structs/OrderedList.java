package ax.commons.structs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * An array list with elements always ordered by the given comparator
 * <p>
 * Complexity:
 * <ul>
 * <li>insert O(log n)</li>
 * <li>remove O(log n)</li>
 * <li>search O(log n)</li>
 * </ul>
 * </p>
 * 
 * @author Jonathan Lacombe
 *
 * @param <E>
 */
public final class OrderedList<E> implements Iterable<E> {

    private Object[] list;

    private int size;

    private final Comparator<E> comparator;

    /**
     * Creates a new ordered list with the given comparator to define
     * the sorting used in this ordered list and a given initial capacity
     * @param comparator
     * @param capacity
     */
    public OrderedList(Comparator<E> comparator, int capacity) {
        this.comparator = comparator;
        list = new Object[capacity];
    }
    
    /**
     * Creates an ordered list with a default capacity of 4
     * @see #OrderedList(Comparator, int)
     */
    public OrderedList(Comparator<E> comparator) {
        this(comparator,4);
    }

    /**
     * @param e comparable element to search
     * @return index of element A where A.compareTo(e)==0
     * or -1 if there is no such element
     */
    @SuppressWarnings("unchecked")
    private int binarySearch(E e) {
        int max = size-1;
        int min = 0;
        while (max>=min) {
            int mid = (max+min)/2;
            int compare = comparator.compare(e,(E)list[mid]);
            if (compare==0) {
                return mid;
            } else if (compare==-1) {
                max = mid-1;
            } else {
                min = mid+1;
            }
        }
        return -1;
    }

    /**
     * This uses the binary search algorithm
     * @param e element to check
     * @return index of at which element e would be inserted
     */
    @SuppressWarnings("unchecked")
    private int binaryInsert(E e) {
        int max = size-1;
        int min = 0;
        while (max>=min) {
            int mid = (max+min)/2;
            
            //if out of upper bounds, insert 1 above maximum
            if (comparator.compare(e,(E)list[max])==1) return max+1;
            
            //if out of lower bounds, insert at minimum
            if (comparator.compare(e,(E)list[min])==-1) return min;
            
            //continue binary search
            int compare = comparator.compare(e,(E)list[mid]);
            if (compare==0)
                return mid;
            else if (compare==-1)
                max = mid-1;
            else
                min = mid+1;
        }
        throw new IllegalStateException("Binary Insert Algorithm failed");
    }

    /**
     * @param e element to be indexed
     * @return index of element e or -1 if element is not found
     */
    @SuppressWarnings("unchecked")
    public int indexOf(E e) {
        int index = binarySearch(e);
        if (index==-1) return -1;
        if (e.equals(list[index])) return index;
        for (int i=index+1; i<size; i++) { //target object might be above searched index
            if (e.equals(list[i])) return i;
            if (comparator.compare(e,(E)list[i])!=0) break; //target object not above
        }
        for (int i=index-1; i>=0; i--) { //target object might be below searched index
            if (e.equals(list[i])) return i;
            if (comparator.compare(e,(E)list[i])!=0) break; //target object not below
        }
        return -1;
    }

    /**
     * Adds the element to the list to a position where elements
     * are still ordered
     * @param e element to add
     */
    public void add(E e) {
        int index = 0;
        if (size!=0) index = binaryInsert(e);
        expandIfNecessary(index,1);
        list[index] = e;
        size++;
    }

    /**
     * Removes elements at the given index to index+length from the list
     * @param index index to start removal
     * @param length length of removal
     */
    public void remove(int index, int length) {
        if (index<0 || index+length>size) throw indexException(index+length);
        if (index+length!=size) {
            System.arraycopy(list,index+length,list,index,size-length);
        }
        size-=length;
    }
    
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index<0 || index+1>size) throw indexException(index+1);
        E e = (E) list[index];
        if (index+1!=size) {
            System.arraycopy(list,index+1,list,index,size-1);
        }
        size--;
        return e;
    }
    
    public E removeFirst() {
        return remove(0);
    }
    
    public E removeLast() {
        return remove(size-1);
    }
    
    public void remove(E e) {
        int index = indexOf(e);
        if (index!=-1) {
            remove(index);
        }
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index<0 || index>=size) throw indexException(index);
        return (E) list[index];
    }
    
    public boolean contains(E e) {
        return indexOf(e)!=-1;
    }

    /**
     * @return the size of this list (number of elements stored)
     */
    public int size() {
        return size;
    }

    /**
     * The capacity will double if size >= capacity
     * @return the capacity of this list
     */
    public int capacity() {
        return list.length;
    }
    
    public boolean isEmpty() {
        return size==0;
    }

    /**
     * Expands this array by twice its size if its full
     * <p>
     * list[index] ... list[index+length] will be used and array
     * will be expanded "around"
     * @param from index to expand from
     * @param length length of expansion
     */
    private void expandIfNecessary(int from, int length) {
        if (size+length>list.length || from<=size) {
            Object[] newList = new Object[list.length*2];
            System.arraycopy(list,0,newList,0,from);
            System.arraycopy(list,from,newList,from+length,size-from);
            list = newList;
        }
    }
    
    public Object[] toArray() {
        Object[] array = new Object[size];
        System.arraycopy(list,0,array,0,size);
        return array;
    }
    
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(list, size, a.getClass());
        System.arraycopy(list, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public Iterator<E> iterator() {
        OrderedList<E> thisList = this;
        return new Iterator<E>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index<size;
            }
            @SuppressWarnings("unchecked")
            @Override
            public E next() {
                return (E) list[index++];
            }
            @Override
            public void remove() {
                thisList.remove(index);
                index--;
            }
        };
    }

    public IndexOutOfBoundsException indexException(int index) {
        return new IndexOutOfBoundsException("Targ: "+index+", Size: "+size);
    }

}
