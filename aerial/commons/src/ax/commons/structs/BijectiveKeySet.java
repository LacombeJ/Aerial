package ax.commons.structs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * Key set for a BijectiveMap
 * 
 * @author Jonathan Lacombe
 *
 */
class BijectiveKeySet<K,V> implements Set<K> {
    
    private final HashMap<K,V> map;
    private final HashMap<V,K> coMap;

    BijectiveKeySet(HashMap<K,V> map, HashMap<V,K> coMap) {
        this.map = map;
        this.coMap = coMap;
    }
    
    @Override
    public int size() {
        return map.keySet().size();
    }

    @Override
    public boolean isEmpty() {
        return map.keySet().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return map.keySet().contains(o);
    }

    @Override
    public Iterator<K> iterator() {
        return new SetIterator<>(map.keySet(),coMap.values());
    }

    @Override
    public Object[] toArray() {
        return map.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return map.keySet().toArray(a);
    }

    @Override
    public boolean add(K e) {
        boolean add = map.keySet().add(e);
        add = coMap.values().add(e);
        return add;
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = map.keySet().remove(o);
        remove = coMap.values().remove(o);
        return remove;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return map.keySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
        boolean add = map.keySet().addAll(c);
        add = coMap.values().addAll(c);
        return add;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retain = map.keySet().retainAll(c);
        retain = coMap.values().retainAll(c);
        return retain;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean remove = map.keySet().removeAll(c);
        remove = coMap.values().removeAll(c);
        return remove;
    }

    @Override
    public void clear() {
        map.keySet().clear();
        coMap.values().clear();
    }
    
    
    class SetIterator<T> implements Iterator<T>
    {
        Iterator<T> setIter;
        Collection<T> coSet;
        T next;
        SetIterator(Set<T> set, Collection<T> coSet) {
            setIter = set.iterator();
            this.coSet = coSet;
        }
        @Override
        public boolean hasNext() {
            return setIter.hasNext();
        }
        @Override
        public T next() {
            next = setIter.next();
            return next;
        }
        @Override
        public void remove() {
            setIter.remove();
            coSet.remove(next);
        }
    }
    
}
