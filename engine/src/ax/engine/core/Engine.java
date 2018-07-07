package ax.engine.core;

import ax.commons.io.Logger;
import ax.commons.io.LoggerFactory;

class Engine {

    static Logger log = LoggerFactory.create(
        (msg) -> {
            
            System.out.println(msg);
            
        }
    );
    
}
