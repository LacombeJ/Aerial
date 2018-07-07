package jonl.aui;

public interface SwitchWidget extends Widget {

    Widget current();
    
    Widget get(int index);
    
    void add(Widget widget);
    
    void remove(Widget widget);
    
    void remove(int index);
    
    int count();
    
    int indexOf(Widget widget);
    
    int index();
    
    void setIndex(int index);
    
    void setWidget(Widget widget);
    
}
