package ax.aui;

import java.util.ArrayList;

public interface Layout {

    Widget parent();
    
    
    Widget getWidget();
    
    void setWidget(Widget widget);
    
    
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
    
    boolean isEmpty();
    
    boolean contains(Widget widget);
    
    boolean contains(LayoutItem item);
    
    Margin margin();
    
    void setMargin(Margin margin);
    
    void setMargin(int left, int right, int top, int bottom);
    
    int spacing();
    
    void setSpacing(int spacing);
    
}
