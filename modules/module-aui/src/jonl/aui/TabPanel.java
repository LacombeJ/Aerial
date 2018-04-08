package jonl.aui;

import jonl.jutils.func.Callback0D;

public interface TabPanel extends Widget {

    Widget get(int index);
    
    void add(Widget widget, String label);
    
    void remove(Widget widget);
    
    void remove(int index);
    
    int count();
    
    int indexOf(Widget widget);
    
    int index();
    
    void setIndex(int index);
    
    void setWidget(Widget widget);

    void setCloseable(boolean enabled);
    
    void setAddable(boolean enable);
    
    Signal<Callback0D> newTab();
    
}
