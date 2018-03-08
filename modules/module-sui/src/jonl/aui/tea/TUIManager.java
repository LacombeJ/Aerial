package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Font;
import jonl.aui.Layout;
import jonl.aui.ListLayout;
import jonl.aui.ScrollPanel;
import jonl.aui.SpacerItem;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.tea.graphics.TStyle;

public class TUIManager implements UIManager {
    
    private static TUIManager ui = null;
    
    public static TUIManager instance() {
        if (ui==null) {
            ui = new TUIManager();
        }
        return ui;
    }
    
    private TStyle style = new TStyle();
    
    private TUIManager() {
        
    }
    
    public TStyle getStyle() {
        return style;
    }
    
    void enroll(TWidget widget) {
        widget.style = style;
    }
    
    @Override
    public TWindow window() {
        return new TWindow();
    }

    @Override
    public TButton button() {
        return new TButton();
    }

    @Override
    public TButton button(String text) {
        return new TButton(text);
    }

    @Override
    public TLabel label() {
        return new TLabel();
    }

    @Override
    public TLabel label(String text) {
        return new TLabel(text);
    }

    @Override
    public TDial dial() {
        return new TDial();
    }

    @Override
    public TPanel panel() {
        return new TPanel();
    }

    @Override
    public TPanel panel(Layout layout) {
        return new TPanel(layout);
    }

    @Override
    public TSplitPanel splitPanel() {
        return new TSplitPanel();
    }

    @Override
    public TSplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        return new TSplitPanel();
    }

    @Override
    public ScrollPanel scrollPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tree tree() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreeItem treeItem() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreeItem treeItem(String text) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TOverlay overlay() {
        return new TOverlay();
    }

    @Override
    public Font font(String font, int size) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public SpacerItem spacerItem() {
        return new TSpacerItem();
    }
    
    @Override
    public TSpacerItem spacerItem(Align align) {
        return new TSpacerItem(align);
    }
    
    @Override
    public TWidgetItem widgetItem(Widget widget) {
        return new TWidgetItem(widget);
    }

    @Override
    public ListLayout listLayout() {
        return new TListLayout();
    }

    @Override
    public ListLayout listLayout(Align align) {
        return new TListLayout(align);
    }

    
    
}
