package jonl.aui;

public interface SpacerItem extends LayoutItem {

    Align align();
    
    void setAlign(Align align);
    
    void setMinSize(int width, int height);
    
    void setMaxSize(int width, int height);
    
    void setPreferredSize(int width, int height);
    
}
