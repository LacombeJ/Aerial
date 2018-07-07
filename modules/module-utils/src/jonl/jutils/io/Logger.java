package jonl.jutils.io;

public interface Logger {

    public static final int ALL     = Integer.MIN_VALUE;
    public static final int DEBUG   = 100;
    public static final int INFO    = 200;
    public static final int WARN    = 300;
    public static final int ERROR   = 400;
    public static final int FATAL   = 500;
    public static final int NONE    = Integer.MAX_VALUE;
    
    // --------------------------------
    
    public void level(int level);
    
    public void log(String message);
    
    public void log(int level, String message);
    
    // --------------------------------
    
    public void debug(String message);
    
    public void info(String message);
    
    public void warn(String message);
    
    public void error(String message);
    
    public void fatal(String message);

}
