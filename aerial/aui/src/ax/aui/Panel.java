package ax.aui;

import java.util.ArrayList;

public interface Panel extends Widget {
    
    Layout layout();
    
    void setLayout(Layout layout);
    
    Widget getWidget(int index);
    
    LayoutItem getItem(int index);
    
    void add(Widget widget);
    
    void add(LayoutItem item);
    
    void remove(Widget widget);
    
    void remove(LayoutItem item);
    
    void remove(int index);
    
    void removeAll();
    
    int indexOf(Widget widget);
    
    int indexOf(LayoutItem item);
    
    int count();
    
    ArrayList<Widget> widgets();
    
    ArrayList<LayoutItem> items();
    
}
