package jonl.aui;

public interface Window extends SingleSlot {

    /** Creates the window with some of the set parameters */
    void create();
    
    String getTitle();
    void setTitle(String title);
    
    void setPosition(HAlign halign, VAlign valign);
    
    void setX(int x);
    void setY(int y);
    void setWidth(int width);
    void setHeight(int height);
    
    void setVisible(boolean visible);
    void setResizable(boolean resizable);

    
    
}
