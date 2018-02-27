package jonl.ge.base;

import jonl.jutils.io.Logger;
import jonl.jutils.io.LoggerFactory;

public class Engine {

    public static Logger log = LoggerFactory.create(
        (msg) -> {
            
            System.out.println(msg);
            
        }
    );
    
}
