package ax.commons.structs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Two-way HashMap
 * 
 * @author Jonathan Lacombe
 *
 */
public class BijectiveMap<K,V> implements Map<K,V> {

    private final HashMap<K,V> keyMap;
    private final HashMap<V,K> valueMap;
    
    private final BijectiveKeySet<K,V> keySet;
    private final BijectiveKeySet<V,K> valueSet;
    
    private final BijectiveEntrySet<K,V> entrySet;
    
    public BijectiveMap(int initialCapacity) {
        keyMap = new HashMap<>(initialCapacity);
        valueMap = new HashMap<>(initialCapacity);
        
        keySet = new BijectiveKeySet<>(keyMap,valueMap);
        valueSet = new BijectiveKeySet<>(valueMap,keyMap);
        
        entrySet = new BijectiveEntrySet<>(keyMap,valueMap);
    }
    
    public BijectiveMap() {
        this(16);
    }
    
    public int size() {
        return keyMap.size();
    }
    
    public V getValue(Object key) {
        return keyMap.get(key);
    }
    
    public K getKey(Object value) {
        return valueMap.get(value);
    }
    
    public V removeKey(Object key) {
        V value = keyMap.remove(key);
        valueMap.remove(value);
        return value;
    }
    
    public K removeValue(Object value) {
        K key = valueMap.remove(value);
        keyMap.remove(key);
        return key;
    }
    
    @Override
    public Set<Entry<K,V>> entrySet() {
        return entrySet;
    }
    
    @Override
    public Set<K> keySet() {
        return keySet;
    }
    
    public Set<V> valueSet() {
        return valueSet;
    }
    
    @Override
    public void clear() {
        keyMap.clear();
        valueMap.clear();
    }

    @Override
    public boolean isEmpty() {
        return keyMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return keyMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return valueMap.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return getValue(key);
    }

    @Override
    public V put(K key, V value) {
        keyMap.remove(valueMap.remove(value));
        valueMap.put(value,key);
        return keyMap.put(key,value);
    }

    @Override
    public V remove(Object key) {
        return removeKey(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            put(key,value);
        }
    }

    @Override
    public Collection<V> values() {
        return valueSet;
    }
    
    @Override
    public String toString() {
        String ret = "{";
        for (Entry<K,V> entry : entrySet) {
            ret +="["+entry.getKey()+":"+entry.getValue()+"]";
        }
        ret += "}";
        return ret;
    }
    
}
