package jonl.aui;

import jonl.jutils.func.Callback;

public interface Widget {
    
    public int x();
    public int y();
    public int width();
    public int height();
    
    public int windowX();
    public int windowY();
    
    public boolean enabled();
    
    public void setEnabled(boolean enable);
    
    /**
     * Emitted when widget is painted<br>
     * Graphics g -> graphics used to paint widget
     */
    Signal<Callback<Graphics>> paint();
    
}
