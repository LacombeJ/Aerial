package jonl.aui;

public interface Container extends Widget {
    
    boolean contains(Widget w);
    
    void remove(Widget w);
    
    int getChildrenCount();
    
    boolean hasChildren();
    
    Widget[] getChildren();
    
    void layout();
    
}
