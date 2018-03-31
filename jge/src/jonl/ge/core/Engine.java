package jonl.ge.core;

import jonl.jutils.io.Logger;
import jonl.jutils.io.LoggerFactory;

class Engine {

    static Logger log = LoggerFactory.create(
        (msg) -> {
            
            System.out.println(msg);
            
        }
    );
    
}
