package jonl.aui;

import java.util.ArrayList;

public interface Panel extends Widget {
    
    Layout layout();
    
    void setLayout(Layout layout);
    
    Widget get(int index);
    
    void add(Widget widget);
    
    void remove(Widget widget);
    
    void removeAll();
    
    ArrayList<Widget> widgets();
    
}
