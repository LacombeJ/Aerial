package jonl.ge;

import jonl.jutils.misc.SystemUtils;
import jonl.jutils.time.Clock;

public abstract class AbstractApp implements App {

    //TODO move as much things as possible here
    private Platform platform;
    private Clock clock;
    
    AbstractApp() {
        if (SystemUtils.isWindows()) {
            platform = Platform.WINDOWS;
        } else if (SystemUtils.isLinux()) {
            platform = Platform.LINUX;
        }
        clock = new Clock();
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public Clock getClock() {
        return clock;
    }
    
}
