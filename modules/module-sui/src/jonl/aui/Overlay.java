package jonl.aui;

public interface Overlay extends Widget {

    Widget get(int index);
    
    void add(Widget widget);
    
    void add(Widget widget, int x, int y);
    
    void add(Widget widget, int x, int y, int width, int height);
    
    void remove(Widget widget);
    
    void setPosition(int index, int x, int y);
    
    void setSize(int index, int width, int y);
    
}
