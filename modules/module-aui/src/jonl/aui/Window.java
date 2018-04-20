package jonl.aui;

import java.awt.image.BufferedImage;

public interface Window extends Widget {

    /** Creates the window with some of the set parameters */
    void create();
    
    Widget widget();
    
    void setWidget(Widget widget);
    
    String title();
    void setTitle(String title);
    
    void setPosition(HAlign halign, VAlign valign);
    
    void setX(int x);
    void setY(int y);
    void setWidth(int width);
    void setHeight(int height);
    
    void setVisible(boolean visible);
    void setResizable(boolean resizable);
    void setDecorated(boolean decorated);
    
    void maximize();
    void minimize();
    void restore();

    void setIcon(BufferedImage image);
    
}
