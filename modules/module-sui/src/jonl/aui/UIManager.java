package jonl.aui;

public interface UIManager {

    //UI Widgets
    
    Window window();
    
    Button button();
    
    Button button(String text);
    
    Label label();
    
    Label label(String text);
    
    Dial dial();
    
    Panel panel();
    
    Panel panel(Layout layout);
    
    SplitPanel splitPanel();
    
    SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio);
    
    ScrollPanel scrollPanel();
    
    Tree tree();
    
    TreeItem treeItem();
    
    TreeItem treeItem(String text);
    
    Overlay overlay();
    
    // UI Elements
    
    Font font(String font, int size);
    
    SpacerItem spacerItem();
    
    SpacerItem spacerItem(Align align);
    
    WidgetItem widgetItem(Widget widget);
    
    ListLayout listLayout();
    
    ListLayout listLayout(Align align);
    
}
