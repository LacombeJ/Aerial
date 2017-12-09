package jonl.jutils.io;

import java.io.IOException;

public abstract class Reader {

    public abstract String readln() throws IOException;
    
    public abstract void close() throws IOException;
    
}
