package jonl.jutils.structs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AttributeMap implements Map<String,Object> {

    private HashMap<String,Object> map;
    
    public AttributeMap() {
        map = new HashMap<>();
    }
    
    public AttributeMap(int capacity) {
        map = new HashMap<>(capacity);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> m) {
        map.putAll(m);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Gets or puts the value for the given key. If the key doesn't exist put and return
     * the value given by defaultValue
     * @param key
     * @param defaultValue
     * @return the value under the given key or the default value if it does not exist
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object ret = map.get(key);
        if (ret != null) {
            return (T) ret;
        }
        map.put(key, defaultValue);
        return defaultValue;
    }
    
    public boolean getBoolean(String key) {
        return (boolean) map.get(key);
    }
    
    public byte getByte(String key) {
        return (byte) map.get(key);
    }
    
    public char getChar(String key) {
        return (char) map.get(key);
    }
    
    public short getShort(String key) {
        return (short) map.get(key);
    }
    
    public int getInt(String key) {
        return (int) map.get(key);
    }
    
    public long getLong(String key) {
        return (long) map.get(key);
    }
    
    public float getFloat(String key) {
        return (float) map.get(key);
    }
    
    public double getDouble(String key) {
        return (double) map.get(key);
    }
    
    public String getString(String key) {
        return (String) map.get(key);
    }
    
    @SuppressWarnings("unchecked")
    public <T> T getAsType(String key, Class<T> c) {
        return (T) map.get(key);
    }
    
    // ------------------------------------------------------------------------
    
    public void add(AttributeMap attrib) {
        map.putAll(attrib.map);
    }
    
    public void sub(AttributeMap attrib) {
        for (String key : attrib.keySet()) {
            map.remove(key);
        }
    }
    
    public void update(AttributeMap attrib) {
        add(intersection(attrib,this));
    }
    
    public void join(AttributeMap attrib) {
        add(attrib);
    }
    
    public AttributeMap fork() {
        AttributeMap map = new AttributeMap();
        add(this);
        return map;
    }
    
    public static AttributeMap union(AttributeMap a, AttributeMap b) {
        AttributeMap map = new AttributeMap();
        map.add(a);
        map.add(b);
        return map;
    }
    
    public static AttributeMap intersection(AttributeMap a, AttributeMap b) {
        AttributeMap map = new AttributeMap();
        for (String key : a.keySet()) {
            if (b.containsKey(key)) {
                map.put(key, a.get(key));
            }
        }
        return map;
    }
    
}
