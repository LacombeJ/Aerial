package jonl.aui.tea;

public class TManager {

    /*
     * Manages widget layouts and events.
     * There should exist one instance of this per root widget / window.
     */
    
    private TManagerEvent event;
    
    TManager() {
        event = new TManagerEvent();
    }
    
    public TManagerEvent event() {
        return event;
    }
    
}
