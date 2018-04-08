package jonl.aui;

public interface ArrayLayout extends Layout {

    void add(Widget widget, int row, int col);
    
    void add(LayoutItem item, int row, int col);
    
    Widget getWidget(int row, int col);
    
    LayoutItem getItem(int row, int col);
    
    int rowCount();
    
    int columnCount();
    
}
