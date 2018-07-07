package ax.aui;

import java.util.ArrayList;

public interface List extends Widget {

    public static final int SCROLL_BAR    = 0;
    //public static final int SCROLL_BUTTON = 1; //TODO add scroll button support
    
    Align align();
    
    void setAlign(Align align);
    
    int scrollType();
    
    void setScrollType(int scrollType);
    
    Widget getWidget(int index);
    
    void add(Widget widget);
    
    void remove(Widget widget);
    
    void remove(int index);
    
    void removeAll();
    
    int indexOf(Widget widget);
    
    int count();
    
    ArrayList<Widget> widgets();
    
    boolean isEmpty();
    
    boolean contains(Widget widget);
    
    Margin margin();
    
    void setMargin(Margin margin);
    
    void setMargin(int left, int right, int top, int bottom);
    
    int spacing();
    
    void setSpacing(int spacing);
    
}
