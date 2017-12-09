package jonl.jutils.io;

import java.io.IOException;

public abstract class Writer {
    
    public abstract void print(String... strings);
    
    public abstract void print(Object... objects);
    
    public abstract void println(Object... objects);
    
    public abstract void close() throws IOException;
    
}
