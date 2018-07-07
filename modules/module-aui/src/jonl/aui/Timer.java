package jonl.aui;

import jonl.jutils.func.Callback0D;

public interface Timer {

    long interval();
    
    boolean isSingleShot();
    
    Signal<Callback0D> tick();
    
}
