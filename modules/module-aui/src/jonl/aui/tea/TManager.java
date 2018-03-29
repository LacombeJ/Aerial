package jonl.aui.tea;

public class TManager {

    /*
     * Manages widget layouts and events.
     * There should exist one instance of this per root widget / window.
     */
    
    private TManagerLayout layout;
    private TManagerEvent event;
    
    TManager() {
        layout = new TManagerLayout(this);
        event = new TManagerEvent();
    }
    
    public TManagerLayout layout() {
        return layout;
    }
    
    public TManagerEvent event() {
        return event;
    }
    
}
