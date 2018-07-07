package jonl.jutils.io;

public abstract class ALogger implements Logger {

    private int level = ALL;
    
    @Override
    public void log(int level, String message) {
        if (this.level <= level) {
            log(message);
        }
    }
    
    @Override
    public void level(int level) {
        this.level = level;
    }
    
    @Override
    public void debug(String message) {
        log(DEBUG, message);
    }

    @Override
    public void info(String message) {
        log(INFO, message);
    }
    
    @Override
    public void warn(String message) {
        log(WARN, message);
    }
    
    @Override
    public void error(String message) {
        log(ERROR, message);
    }
    
    @Override
    public void fatal(String message) {
        log(FATAL, message);
    }
    
}
