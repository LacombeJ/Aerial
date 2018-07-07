package ax.commons.misc;

import java.io.File;

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
    
    public static String userHome() {
        return System.getProperty("user.home");
    }
    
    public static String currentDir() {
        return System.getProperty("user.dir");
    }
    
    public static String appDataLocation() {
        if (isWindows()) {
            return userHome() + File.separatorChar + "AppData" + File.separatorChar + "Local";
        } else if (isLinux()) {
            throw new Error("App data location not implemented for Linux");
        } else if (isMac()) {
            throw new Error("App data location not implemented for Mac");
        }
        else throw new Error("App data location not implemented for unknown OS");
    }
    
    public static String appDataLocation(String app) {
        return appDataLocation() + File.separatorChar + app;
    }
    
}
