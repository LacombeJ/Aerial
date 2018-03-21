package jonl.aui;

public interface UIManager {

    //UI Widgets
    
    Window window();
    
    Frame frame();
    
    Button button();
    
    Button button(String text);
    
    Label label();
    
    Label label(String text);
    
    Dial dial();
    
    Slider slider();
    
    Panel panel();
    
    Panel panel(Layout layout);
    
    SplitPanel splitPanel();
    
    SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio);
    
    ScrollPanel scrollPanel();
    
    TabPanel tabPanel();
    
    Tree tree();
    
    TreeItem treeItem();
    
    TreeItem treeItem(String text);
    
    MenuBar menuBar();
    
    MenuButton menuButton();
    
    MenuButton menuButton(String text);
    
    ToolBar toolBar();
    
    ToolButton toolButton();
    
    ToolButton toolButton(Icon icon);
    
    Overlay overlay();
    
    // UI Elements
    
    Icon icon(String file);
    
    Font font(String font, int size);
    
    SpacerItem spacerItem();
    
    SpacerItem spacerItem(Align align);
    
    WidgetItem widgetItem(Widget widget);
    
    ListLayout listLayout();
    
    ListLayout listLayout(Align align);
    
}
