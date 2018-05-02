package jonl.jutils.data;

import java.io.File;

import jonl.jutils.misc.SystemUtils;

public class Dir {

    File file = null;
    
    public Dir(File file) {
        this.file = file;
    }
    
    public Dir(String path) {
        file = new File(path);
    }
    
    public Dir dir(String name) {
        return new Dir(path(name));
    }
    
    public boolean exists() {
        return file.exists();
    }
    
    public String path() {
        return file.getPath();
    }
    
    public String path(String name) {
        return path() + separator() + name;
    }
    
    public boolean exists(String name) {
        File abs = new File(name);
        if (abs.isAbsolute()) {
            return abs.exists();
        }
        File rel = new File(path(name));
        return rel.exists();
    }
    
    public boolean mkdir() {
        return file.mkdir();
    }
    
    public Dir mkdir(String dirName) {
        Dir d = dir(dirName);
        if (d.mkdir()) {
            return d;
        }
        return null;
    }
    
    public Json json(String name) {
        return new Json(path(name));
    }
    
    // ------------------------------------------------------------------------
    
    public static char separator() {
        return File.separatorChar;
    }
    
    public Dir current() {
        return new Dir(SystemUtils.currentDir());
    }
    
    public Dir home() {
        return new Dir(SystemUtils.userHome());
    }
    
    public Dir appData() {
        return new Dir(SystemUtils.appDataLocation());
    }
    
}
