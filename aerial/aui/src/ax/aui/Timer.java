package ax.aui;

import ax.commons.func.Callback0D;

public interface Timer {

    long interval();
    
    boolean isSingleShot();
    
    Signal<Callback0D> tick();
    
}
