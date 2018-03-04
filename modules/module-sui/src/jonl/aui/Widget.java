package jonl.aui;

import jonl.jutils.func.Callback;

public interface Widget {
    
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
    
    int preferredWidth();
    int preferredHeight();
    void setPreferredSize(int width, int height);
    
    int windowX();
    int windowY();
    
    boolean enabled();
    
    void setEnabled(boolean enable);
    
    /**
     * Emitted when widget is painted<br>
     * Graphics g -> graphics used to paint widget
     */
    Signal<Callback<Graphics>> paint();
    
}
