package ax.commons.misc;

import java.util.HashMap;

import ax.commons.structs.ReferenceSet;

public class Tools {

    private static HashMap<String,Integer> uniqueIncrement = new HashMap<>();
    
    private static ReferenceSet<Object> uniqueObjects = new ReferenceSet<>();
    
    public static int increment(String key) {
        if (!uniqueIncrement.containsKey(key)) {
            uniqueIncrement.put(key, 0);
            return 0;
        }
        int value = uniqueIncrement.get(key);
        value++;
        uniqueIncrement.put(key, value);
        return value;
    }
    
    public static boolean unique(Object key) {
        if (!uniqueObjects.contains(key)) {
            uniqueObjects.add(key);
            return true;
        }
        return false;
    }

}
