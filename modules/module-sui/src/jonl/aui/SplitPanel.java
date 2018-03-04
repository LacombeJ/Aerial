package jonl.aui;

public interface SplitPanel extends Widget {
    
    void setSplit(Widget w1, Widget w2, Align align, double ratio);
    
    Widget widgetOne();
    Widget widgetTwo();
    Widget[] widgets();
    
    void setWidgetOne(Widget widget);
    void setWidgetTwo(Widget widget);
    void setWidgets(Widget w1, Widget w2);
    
    Align align();
    
    void setAlign(Align align);
    
    double ratio();
    
    void setRatio(double d);
    
}
