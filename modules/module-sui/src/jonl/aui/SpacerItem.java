package jonl.aui;

public interface SpacerItem extends LayoutItem {

    Align align();
    
    void setAlign(Align align);
    
    void setMinSize(int width, int height);
    
    void setMaxSize(int width, int height);
    
    void setSizePolicy(SizePolicy policy);
    
    void setHint(int width, int height);
    
}
