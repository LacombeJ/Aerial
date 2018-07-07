package jonl.jutils.structs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import jonl.jutils.func.List;

//TODO use hashing and function to define what type of set

public class ReferenceSet<X> implements Set<X> {

    //TODO change backed structure
    ArrayList<X> list = new ArrayList<X>();
    
    /** @return the array list backing this object */
    public ArrayList<X> getArrayList() {
        return list;
    }
    
    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        Iterator<X> it = iterator();
        while (it.hasNext())
            if (it.next()==o)
                return true;
        return false;
    }

    @Override
    public Iterator<X> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean add(X x) {
        if (!contains(x)) {
            list.add(x);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = List.index(list, x -> x==o);
        if (index!=-1) {
            list.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object x : c) {
            if (!contains(x)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends X> c) {
        boolean changed = false;
        for (X x : c) {
            boolean added = add(x);
            if (added) changed = true;
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object x : c) {
            boolean removed = remove(x);
            if (removed) changed = true;
        }
        return changed;
    }

    @Override
    public void clear() {
        list.clear();
    }

    
    
}
