package jonl.jutils.data;

import java.io.File;

import jonl.jutils.misc.SystemUtils;

/**
 * File wrapper class
 * 
 * @author Jonathan
 *
 */
public class Dir {

    private final File file;
    
    public Dir(File file) {
        this.file = file;
    }
    
    public Dir(String path) {
        file = new File(path);
    }
    
    public Dir dir(String name) {
        return new Dir(path(name));
    }
    
    public Dir parent() {
        return new Dir(file.getParent());
    }
    
    /** @return child directory and creates it if it does not exist */
    public Dir child(String name) {
        Dir dir = dir(name);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
    
    public boolean exists() {
        return file.exists();
    }
    
    public boolean isDirectory() {
        return file.isDirectory();
    }
    
    public boolean isFile() {
        return file.isFile();
    }
    
    public String name() {
        return file.getName();
    }
    
    public String path() {
        return file.getPath();
    }
    
    public String path(String name) {
        return path() + separator() + name;
    }
    
    public String absolute() {
        return file.getAbsolutePath();
    }
    
    public String absolute(String name) {
        return absolute() + separator() + name;
    }
    
    /** @return true if this directory exists absolutely or relatively */
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
    
    public boolean delete() {
        return file.delete();
    }
    
    public Json json(String name) {
        return new Json(path(name));
    }
    
    // ------------------------------------------------------------------------
    
    public static char separator() {
        return File.separatorChar;
    }
    
    public static Dir current() {
        return new Dir(SystemUtils.currentDir());
    }
    
    public static Dir home() {
        return new Dir(SystemUtils.userHome());
    }
    
    public static Dir appData() {
        return new Dir(SystemUtils.appDataLocation());
    }
    
}
