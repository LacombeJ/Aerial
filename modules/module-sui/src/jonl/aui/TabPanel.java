package jonl.aui;

public interface TabPanel extends Widget {

    Widget get(int index);
    
    void add(Widget widget, String label);
    
    void remove(Widget widget);
    
    void remove(int index);
    
    int count();
    
    int indexOf(Widget widget);
    
    int index();
    
    void setIndex(int index);
    
}
