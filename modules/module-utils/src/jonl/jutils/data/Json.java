package jonl.jutils.data;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jonl.jutils.io.FileUtils;

public class Json {

    File file = null;
    
    public Json(File file) {
        this.file = file;
    }
    
    public Json(String path) {
        file = new File(path);
    }
    
    public void save(Object o, boolean pretty) {
        GsonBuilder gb = new GsonBuilder();
        if (pretty) {
            gb.setPrettyPrinting();
        }
        Gson gson = gb.create();
        String serializedObject = gson.toJson(o);
        FileUtils.writeToFile(file.getPath(), serializedObject);
    }
    
    public void save(Object o) {
       save(o,true);
    }
    
    public <T> T load(Class<T> c) {
        String content = FileUtils.readFromFile(file.getPath());
        Gson gson = new Gson();
        T object = gson.fromJson(content, c);
        return object;
    }
    
    public boolean exists() {
        return file.exists();
    }
    
    public String name() {
        return file.getName();
    }
    
    public String path() {
        return file.getPath();
    }
    
}
