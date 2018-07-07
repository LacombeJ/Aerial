package ax.commons.structs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Entry set for a BijectiveMap
 * 
 * @author Jonathan Lacombe
 *
 */
class BijectiveEntrySet<K,V> implements Set<Entry<K,V>> {

    private final HashMap<K,V> keyMap;
    private final HashMap<V,K> valueMap;

    BijectiveEntrySet(HashMap<K,V> keyMap, HashMap<V,K> valueMap) {
        this.keyMap = keyMap;
        this.valueMap = valueMap;
    }
    
    @Override
    public int size() {
        return keyMap.entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return keyMap.entrySet().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return keyMap.entrySet().contains(o);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new SetIterator<>(keyMap.entrySet(),valueMap);
    }

    @Override
    public Object[] toArray() {
        return keyMap.entrySet().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return keyMap.entrySet().toArray(a);
    }

    @Override
    public boolean add(Entry<K, V> e) {
        throw new UnsupportedOperationException("Cannot add to BijectiveEntrySet use BijectiveMap.put instead");
    }
    
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Cannot remove from BijectiveEntrySet use BijectiveMap.remove instead");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return keyMap.entrySet().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Entry<K, V>> c) {
        throw new UnsupportedOperationException("Cannot add to BijectiveEntrySet use BijectiveMap.put instead");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove from BijectiveEntrySet use BijectiveMap.remove instead");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Cannot remove from BijectiveEntrySet use BijectiveMap.remove instead");
    }

    @Override
    public void clear() {
        keyMap.clear();
        valueMap.clear();
    }
    
    
    class SetIterator<KEY,VALUE> implements Iterator<Entry<KEY,VALUE>>
    {
        Iterator<Entry<KEY,VALUE>> setIter;
        Map<VALUE,KEY> coMap;
        
        Entry<KEY,VALUE> next;
        
        SetIterator(Set<Entry<KEY,VALUE>> set, Map<VALUE,KEY> coSet) {
            setIter = set.iterator();
            this.coMap = coSet;
        }
        @Override
        public boolean hasNext() {
            return setIter.hasNext();
        }
        @Override
        public Entry<KEY,VALUE> next() {
            next = setIter.next();
            return next;
        }
        @Override
        public void remove() {
            setIter.remove();
            coMap.remove(next.getValue(), next.getKey());
        }
    }
    
    
    
}
