package jonl.aui;

public interface UIManager {

    //UI Widgets
    
    public Window window();
    
    public Button button();
    
    public Button button(String text);
    
    public Label label();
    
    public Label label(String text);
    
    public Dial dial();
    
    public Panel panel();
    
    public Panel panel(Layout layout);
    
    public SplitPanel splitPanel();
    
    public SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio);
    
    public ScrollPanel scrollPanel();
    
    public Tree tree();
    
    public TreeItem treeItem();
    
    public TreeItem treeItem(String text);
    
    // UI Elements
    
    public Font font(String font, int size);
    
    public ListLayout listLayout();
    
    public ListLayout listLayout(Align align);
    
}
