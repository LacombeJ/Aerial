package jonl.ge.editor;

import java.util.HashMap;

public abstract class StoreTrait {
    
    HashMap<SubEditor,String> storePaths = new HashMap<>();
    
    public abstract Class<?> getStoreDefinition();
    
    /**
     * This method will be called by another thread and return a saved state for an editor as an object
     * @return a serialized object to store
     */
    public abstract Object getStore(SubEditor editor);
    
    /**
     * @return How often state should get stored in milliseconds
     */
    public long storeFrequency() {
        return 60000; // 1 minute
    }
    
    /**
     * @return The limit of past stores saved
     */
    public int historyLimit() {
        return 3;
    }
    
    /**
     * @return The spanning time of history saves in milliseconds
     */
    public long historySpan() {
        return 3600000; // 1 hour
    }
    
}
