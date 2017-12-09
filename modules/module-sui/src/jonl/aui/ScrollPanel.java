package jonl.aui;

public interface ScrollPanel extends SingleSlot {
    
    void setScroll(Widget w, int x, int y, int width, int height);
    
    void setScrollX(int x);
    void setScrollY(int y);
    void setScrollWidth(int width);
    void setScrollHeight(int height);
    
}
