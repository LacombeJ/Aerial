package jonl.ge;

import jonl.jutils.misc.SystemUtils;

public abstract class AbstractApp implements App {

    private Platform platform;
    
    AbstractApp() {
        if (SystemUtils.isWindows()) {
            platform = Platform.WINDOWS;
        } else if (SystemUtils.isLinux()) {
            platform = Platform.LINUX;
        }
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
}
