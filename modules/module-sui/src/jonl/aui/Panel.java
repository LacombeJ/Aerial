package jonl.aui;

import java.util.ArrayList;

public interface Panel extends Widget {
    
    Layout layout();
    
    void setLayout(Layout layout);
    
    Widget get(int index);
    
    void add(Widget widget);
    
    void remove(Widget widget);
    
    void remove(int index);
    
    void removeAll();
    
    int indexOf(Widget widget);
    
    int count();
    
    ArrayList<Widget> widgets();
    
}
