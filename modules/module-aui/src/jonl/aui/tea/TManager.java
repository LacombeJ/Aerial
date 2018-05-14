package jonl.aui.tea;

public class TManager {

    /*
     * There should exist one instance of this per root widget / window.
     */
    
    private TManagerEvent event;
    
    private TManagerUpdate update;
    
    public TManager() {
        event = new TManagerEvent();
        update = new TManagerUpdate();
    }
    
    public TManagerEvent event() {
        return event;
    }
    
    public TManagerUpdate update() {
        return update;
    }
    
}
