package jonl.ge.utils;

import com.google.gson.Gson;

public class Cereal {

    public static String serialize(Object o) {
        Gson gson = new Gson();
        String serializedObject = gson.toJson(o);
        return serializedObject;
    }
    
    public static <T> T deserialize(String s, Class<T> c){
        Gson gson = new Gson();
        T object = gson.fromJson(s, c);
        return object;
    }

    public static <T> T copy(Object o, Class<T> c){
        String s = serialize(o);
        T object = deserialize(s,c);
        return object;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T copy(T o) {
        String s = serialize(o);
        T object = (T) deserialize(s,o.getClass());
        return object;
    }
    
}
