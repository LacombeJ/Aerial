package ax.aui;

import ax.commons.jss.Style;

public interface UIManager {

    void setStyle(String style);
    
    void setStyle(Style style);
    
    void addStyle(String style);
    
    void addStyle(Style style);
    
    Resource resource(String key);
    
    void resource(String key, Object data);
    
    FileDialog fileDialog();
    
    MessageDialog messageDialog();
    
    //UI Widgets
    
    Window window();
    
    Frame frame();
    
    Button button();
    
    Button button(String text);
    
    CheckBox checkBox();
    
    CheckBox checkBox(String text);
    
    Label label();
    
    Label label(String text);
    
    LineEdit lineEdit();
    
    LineEdit lineEdit(String text);
    
    List list();
    
    List list(Align align);
    
    List list(Align align, int scrollType);
    
    ComboBox comboBox();
    
    Dial dial();
    
    Dial dial(int min, int max);
    
    Slider slider();
    
    Slider slider(Align align);
    
    Slider slider(Align align, int min, int max);
    
    Panel panel();
    
    Panel panel(Layout layout);
    
    RadioButton radioButton();
    
    RadioButton radioButton(String text);
    
    TitlePanel titlePanel(String title);
    
    TitlePanel titlePanel(String title, Layout layout);
    
    SplitPanel splitPanel();
    
    SplitPanel splitPanel(Align align);
    
    SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio);
    
    ScrollPanel scrollPanel();
    
    SwitchWidget switchWidget();
    
    TabPanel tabPanel();
    
    Tree tree();
    
    TreeItem treeItem();
    
    TreeItem treeItem(String text);
    
    MenuBar menuBar();
    
    Menu menu(String text);
    
    ToolBar toolBar();
    
    ToolButton toolButton();
    
    ToolButton toolButton(Icon icon);
    
    ToolButton toolButton(Resource icon);
    
    Overlay overlay();
    
    // UI Elements
    
    Timer timer(long interval);
    
    Timer timer(Widget widget, long interval);
    
    Timer timer(Widget widget, long interval, boolean singleShot);
    
    Icon icon(String file);
    
    Font font(String font, int size);
    
    Spacer spacer();
    
    Spacer spacer(Align align);
    
    ListLayout listLayout();
    
    ListLayout listLayout(Align align);
    
    ListLayout listLayout(Align align, Margin margin, int spacing);
    
    ArrayLayout arrayLayout();
    
}
