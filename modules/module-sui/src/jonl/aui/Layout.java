package jonl.aui;

public interface Layout {

    Widget parent();
    
    Widget get();
    
    Widget get(int index);
    
    void set(Widget widget);
    
    void add(Widget widget);
    
    void remove(Widget widget);
    
    void removeAll();
    
    Widget[] widgets();
    
    boolean contains(Widget widget);
    
    boolean isEmpty();
    
    int size();
    
    Margin margin();
    
    void setMargin(Margin margin);
    
    void setMargin(int left, int right, int top, int bottom);
    
    int spacing();
    
    void setSpacing(int spacing);
    
}
