package jonl.ge.editor;

import jonl.aui.Widget;

public abstract class SubEditor {

    /**
     * @return The name of this sub editor instance
     */
    public abstract String name();
    
    /**
     * @return This editor's widget
     */
    public abstract Widget widget();
    
    /**
     * @return Whether this editor should store it's state or not
     */
    public abstract boolean shouldStore();
    
    /**
     * This method should be synchronized and should save a copy of the subeditor state
     */
    public abstract void setStore();
    
    /**
     * This method should also be synchronized and return the copy of the saved state
     * @return copy of state
     */
    public abstract Object getStore();
    
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
