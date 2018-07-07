package ax.commons.structs;

import ax.commons.func.Callback;
import ax.commons.func.Function;

/**
 * Uses a bijective map internally meaning there are unique keys and unique values<br>
 * Will call create when trying to get a value whose key does not exist and destroy on
 * the previous value if a value already exists
 * @author Jonathan
 *
 * @param <K>
 * @param <V>
 */
public class Pool<K,V> {

    private BijectiveMap<K,V> map;
    private Function<K,V> create;
    private Callback<V> destroy;
    
    public Pool(int initialCapacity, Function<K,V> create, Callback<V> destroy) {
        map = new BijectiveMap<>(initialCapacity);
        this.create = create;
        this.destroy = destroy;
    }
    
    public Pool(Function<K,V> create, Callback<V> destroy) {
        map = new BijectiveMap<>();
        this.create = create;
        this.destroy = destroy;
    }
    
    private V create(K k, V v) {
        if (v == null) {
            v = create.f(k);
            map.put(k, v);
        }
        return v;
    }
    
    private V destroy(V v) {
        if (v != null) {
            destroy.f(v);
        }
        return v;
    }
    
    // ------------------------------------------------------------------------
    
    public V get(K key) {
        return create(key, map.get(key));
    }
    
    public V remove(Object o) {
        return destroy(map.remove(o));
    }
    
    @Override
    public String toString() {
        String ret = "{";
        for (V v : map.values()) {
            K k = map.getKey(v);
            ret +="["+k+":"+v+"]";
        }
        ret += "}";
        return ret;
    }

}
