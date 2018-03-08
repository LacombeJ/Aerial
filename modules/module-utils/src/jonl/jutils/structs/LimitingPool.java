package jonl.jutils.structs;

import java.util.ArrayList;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Function;

/**
 * Uses a bijective map internally meaning there are unique keys and unique values<br>
 * Will call create when trying to get a value whose key does not exist and destroy on
 * the the least recently used 
 * @author Jonathan
 *
 * @param <K>
 * @param <V>
 */
public class LimitingPool<K,V> {

    private BijectiveMap<K,V> map;
    private Function<K,V> create;
    private Callback<V> destroy;
    private ArrayList<V> lru;
    private int limit;
    
    public LimitingPool(int limit, Function<K,V> create, Callback<V> destroy) {
        map = new BijectiveMap<>(limit);
        lru = new ArrayList<>(limit);
        this.limit = limit;
        this.create = create;
        this.destroy = destroy;
    }
    
    public int limit() {
        return limit;
    }
    
    private V create(K k, V v) {
        if (v == null) {
            v = create.f(k);
            if (map.size() >= limit) {
                remove(map.getKey(lru.get(0)));
            }
            map.put(k, v);
            lru.add(v);
        } else {
            lru.remove(v);
            lru.add(v);
        }
        return v;
    }
    
    private V destroy(V v) {
        if (v != null) {
            destroy.f(v);
            lru.remove(v);
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
        for (V v : lru) {
            K k = map.getKey(v);
            ret +="["+k+":"+v+"]";
        }
        ret += "}";
        return ret;
    }

}
