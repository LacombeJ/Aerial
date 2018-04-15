package jonl.aui;

public interface UIManager {

    //UI Widgets
    
    Window window();
    
    Frame frame();
    
    Button button();
    
    Button button(String text);
    
    Button checkBox();
    
    Button checkBox(String text);
    
    Label label();
    
    Label label(String text);
    
    LineEdit lineEdit();
    
    LineEdit lineEdit(String text);
    
    Dial dial();
    
    Dial dial(int min, int max);
    
    Slider slider();
    
    Slider slider(Align align);
    
    Slider slider(Align align, int min, int max);
    
    Panel panel();
    
    Panel panel(Layout layout);
    
    TitlePanel titlePanel(String title);
    
    TitlePanel titlePanel(String title, Layout layout);
    
    SplitPanel splitPanel();
    
    SplitPanel splitPanel(Align align);
    
    SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio);
    
    ScrollPanel scrollPanel();
    
    TabPanel tabPanel();
    
    Tree tree();
    
    TreeItem treeItem();
    
    TreeItem treeItem(String text);
    
    MenuBar menuBar();
    
    Menu menu(String text);
    
    ToolBar toolBar();
    
    ToolButton toolButton();
    
    ToolButton toolButton(Icon icon);
    
    Overlay overlay();
    
    // UI Elements
    
    Icon icon(String file);
    
    Font font(String font, int size);
    
    Spacer spacer();
    
    Spacer spacer(Align align);
    
    ListLayout listLayout();
    
    ListLayout listLayout(Align align);
    
    ListLayout listLayout(Align align, Margin margin, int spacing);
    
    ArrayLayout arrayLayout();
    
}
