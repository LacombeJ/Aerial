package jonl.ge;

import jonl.jutils.misc.SystemUtils;

public abstract class AbstractApp implements App {

    //TODO move as much things as possible here
    private Platform platform;
    private Time time;
    
    AbstractApp() {
        if (SystemUtils.isWindows()) {
            platform = Platform.WINDOWS;
        } else if (SystemUtils.isLinux()) {
            platform = Platform.LINUX;
        }
        time = new Time();
    }
    
    @Override
    public Platform getPlatform() {
        return platform;
    }
    
    @Override
    public Time getTime() {
        return time;
    }
    
}
