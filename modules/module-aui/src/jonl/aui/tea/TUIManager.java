package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Font;
import jonl.aui.Icon;
import jonl.aui.Layout;
import jonl.aui.Margin;
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
    public TCheckBox checkBox() {
        return new TCheckBox();
    }

    @Override
    public TCheckBox checkBox(String text) {
        return new TCheckBox(text);
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
    public TLineEdit lineEdit() {
        return new TLineEdit();
    }

    @Override
    public TLineEdit lineEdit(String text) {
        return new TLineEdit(text);
    }
    
    @Override
    public TComboBox comboBox() {
        return new TComboBox();
    }

    @Override
    public TDial dial() {
        return new TDial();
    }
    
    @Override
    public TDial dial(int min, int max) {
        return new TDial(min,max);
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
    public TSlider slider(Align align, int min, int max) {
        return new TSlider(align, min, max);
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
    public TTitlePanel titlePanel(String title) {
        return new TTitlePanel(title);
    }

    @Override
    public TTitlePanel titlePanel(String title, Layout layout) {
        return new TTitlePanel(title,layout);
    }

    @Override
    public TSplitPanel splitPanel() {
        return new TSplitPanel();
    }
    
    @Override
    public TSplitPanel splitPanel(Align align) {
        return new TSplitPanel(align);
    }

    @Override
    public TSplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        return new TSplitPanel(w1,w2,align,ratio);
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
    public TMenu menu(String text) {
        return new TMenu(text);
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
    public TSpacer spacer() {
        return new TSpacer();
    }
    
    @Override
    public TSpacer spacer(Align align) {
        return new TSpacer(align);
    }

    @Override
    public TListLayout listLayout() {
        return new TListLayout();
    }

    @Override
    public TListLayout listLayout(Align align) {
        return new TListLayout(align);
    }
    
    @Override
    public TListLayout listLayout(Align align, Margin margin, int spacing) {
        return new TListLayout(align, margin, spacing);
    }
    
    @Override
    public TArrayLayout arrayLayout() {
        return new TArrayLayout();
    }
    
}
