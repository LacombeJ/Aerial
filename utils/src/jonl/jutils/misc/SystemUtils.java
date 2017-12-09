package jonl.jutils.misc;

public class SystemUtils {

    private static final String OSNAME = System.getProperty("os.name").toLowerCase();
    
    public static final int WINDOWS = 0;
    public static final int LINUX   = 1;
    public static final int MAC     = 2;
    
    public static final int UNKNOWN = -1;
    
    public static int getOS() {
        if (isWindows()) return WINDOWS;
        if (isLinux()) return LINUX;
        if (isMac()) return MAC;
        return UNKNOWN;
    }
    
    public static boolean isWindows() {
        return OSNAME.indexOf("win") >= 0;
    }
    
    public static boolean isLinux() {
        return OSNAME.indexOf("lin") >= 0;
    }
    
    public static boolean isMac() {
        return OSNAME.indexOf("mac") >= 0;
    }
    
}
