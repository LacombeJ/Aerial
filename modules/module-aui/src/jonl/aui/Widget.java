package jonl.aui;

import jonl.jutils.func.Callback;

public interface Widget {
    
    void setStyle(String style);
    void addStyle(String style);
    
    String name();
    void setName(String name);
    
    Info info();
    
    Object data();
    void setData(Object data);
    
    int x();
    int y();
    int width();
    int height();
    
    int minWidth();
    int minHeight();
    void setMinSize(int width, int height);
    
    int maxWidth();
    int maxHeight();
    void setMaxSize(int width, int height);
    
    /**
     * Sets min and max sizes or use -1 to ignore
     */
    void setSizeConstraint(int width, int height);
    
    SizePolicy sizePolicy();
    void setSizePolicy(SizePolicy policy);
    
    int windowX();
    int windowY();
    
    boolean enabled();
    
    void setEnabled(boolean enable);
    
    /**
     * Emitted when widget is painted<br>
     * Graphics g -> graphics used to paint widget
     */
    Signal<Callback<Graphics>> paint();
    
    /**
     * Translates this string to a call
     * @param call string to call
     * @return return null if call doesn't exist, an object if call is a get type, or true if succeeded
     */
    Object call(String call);
    
}
