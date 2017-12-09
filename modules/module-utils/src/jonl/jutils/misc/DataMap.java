package jonl.jutils.misc;

import java.util.HashMap;
import java.util.Map.Entry;

import jonl.jutils.func.Tuple2;

public class DataMap {

    private HashMap<String,String> map;
    
    public DataMap() {
        map = new HashMap<>();
    }
    
    public DataMap(HashMap<String,String> map) {
        this.map = new HashMap<>();
        this.map.putAll(map);
    }
    
    public String get(String key) {
        return map.get(key);
    }
    
    public void put(String key, String value) {
        map.put(key, value);
    }
    
    public DataMap copy() {
        return new DataMap(map);
    }
    
    @Override
    public String toString() {
        String build = "";
        for (Entry<String,String> e : map.entrySet()) {
            build += new Tuple2<String,String>(e.getKey(),e.getValue());
        }
        return build;
    }
    
}
