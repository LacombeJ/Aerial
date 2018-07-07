package ax.aui;

public interface Spacer extends LayoutItem {
    
    void setMinSize(int width, int height);
    
    void setMaxSize(int width, int height);
    
    void setSizeConstraint(int width, int height);
    
    void setSizePolicy(SizePolicy policy);
    
    void setHint(int width, int height);
    
}
