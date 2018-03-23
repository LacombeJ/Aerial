package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Font;
import jonl.aui.Icon;
import jonl.aui.Layout;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.tea.graphics.TStyleDefault;
import jonl.aui.tea.graphics.TStyle;

public class TUIManager implements UIManager {
    
    private static TUIManager ui = null;
    
    public static TUIManager instance() {
        if (ui==null) {
            ui = new TUIManager();
        }
        return ui;
    }
    
    private TStyle style = new TStyleDefault();
    
    private TUIManager() {
        
    }
    
    public TStyle getStyle() {
        return style;
    }
    
    public void setStyle(TStyle style) {
        this.style = style;
    }
    
    void enroll(TWidget widget) {
        widget.style = style;
    }
    
    @Override
    public TWindow window() {
        return new TWindow();
    }
    
    @Override
    public TFrame frame() {
        return new TFrame();
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
    public TSlider slider() {
        return new TSlider();
    }
    
    @Override
    public TSlider slider(Align align) {
        return new TSlider(align);
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
    public TScrollPanel scrollPanel() {
        return new TScrollPanel();
    }
    
    @Override
    public TTabPanel tabPanel() {
        return new TTabPanel();
    }

    @Override
    public TTree tree() {
        return new TTree();
    }

    @Override
    public TTreeItem treeItem() {
        return new TTreeItem();
    }

    @Override
    public TTreeItem treeItem(String text) {
        return new TTreeItem(text);
    }
    
    @Override
    public TMenuBar menuBar() {
        return new TMenuBar();
    }
    
    @Override
    public TMenuButton menuButton() {
        return new TMenuButton();
    }
    
    @Override
    public TMenuButton menuButton(String text) {
        return new TMenuButton(text);
    }
    
    @Override
    public TToolBar toolBar() {
        return new TToolBar();
    }
    
    @Override
    public TToolButton toolButton() {
        return new TToolButton();
    }
    
    @Override
    public TToolButton toolButton(Icon icon) {
        return new TToolButton(icon);
    }
    
    @Override
    public TOverlay overlay() {
        return new TOverlay();
    }
    
    @Override
    public TIcon icon(String file) {
        return new TIcon(file);
    }

    @Override
    public Font font(String font, int size) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public TSpacerItem spacerItem() {
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
    public TListLayout listLayout() {
        return new TListLayout();
    }

    @Override
    public TListLayout listLayout(Align align) {
        return new TListLayout(align);
    }

    
    
}
