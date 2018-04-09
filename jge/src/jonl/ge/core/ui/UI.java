package jonl.ge.core.ui;

import jonl.aui.Align;
import jonl.aui.ArrayLayout;
import jonl.aui.Button;
import jonl.aui.CheckBox;
import jonl.aui.Dial;
import jonl.aui.Font;
import jonl.aui.Frame;
import jonl.aui.Icon;
import jonl.aui.Label;
import jonl.aui.Layout;
import jonl.aui.LineEdit;
import jonl.aui.ListLayout;
import jonl.aui.Margin;
import jonl.aui.MenuBar;
import jonl.aui.MenuButton;
import jonl.aui.Overlay;
import jonl.aui.Panel;
import jonl.aui.ScrollPanel;
import jonl.aui.Slider;
import jonl.aui.Spacer;
import jonl.aui.SplitPanel;
import jonl.aui.TabPanel;
import jonl.aui.TitlePanel;
import jonl.aui.ToolBar;
import jonl.aui.ToolButton;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.aui.tea.TUIManager;
import jonl.aui.tea.TWidget;
import jonl.aui.tea.graphics.TStyle;
import jonl.aui.tea.graphics.TStyleDark;
import jonl.vmath.Color;

public class UI implements UIManager {

    private TUIManager ui;
    
    private TWidget widget;
    
    public UI() {
        
        ui = TUIManager.instance();
        
        TStyle style = new TStyleDark();
        Color transparent = Color.fromFloat(0,0,0,0);
        style.background(transparent);
        ui.setStyle(style);
        
    }
    
    public Widget getWidget() {
        return widget;
    }
    
    public void setWidget(Widget widget) {
        this.widget = (TWidget) widget;
    }
    
    @Override
    public Window window() {
        return ui.window();
    }

    @Override
    public Frame frame() {
        return ui.frame();
    }

    @Override
    public Button button() {
        return ui.button();
    }

    @Override
    public Button button(String text) {
        return ui.button(text);
    }
    
    @Override
    public CheckBox checkBox() {
        return ui.checkBox();
    }

    @Override
    public CheckBox checkBox(String text) {
        return ui.checkBox(text);
    }

    @Override
    public Label label() {
        return ui.label();
    }

    @Override
    public Label label(String text) {
        return ui.label("text");
    }

    @Override
    public LineEdit lineEdit() {
        return ui.lineEdit();
    }

    @Override
    public LineEdit lineEdit(String text) {
        return ui.lineEdit(text);
    }

    @Override
    public Dial dial() {
        return ui.dial();
    }

    @Override
    public Slider slider() {
        return ui.slider();
    }

    @Override
    public Slider slider(Align align) {
        return ui.slider(align);
    }

    @Override
    public Panel panel() {
        return ui.panel();
    }

    @Override
    public Panel panel(Layout layout) {
        return ui.panel(layout);
    }
    
    @Override
    public TitlePanel titlePanel(String title) {
        return ui.titlePanel(title);
    }

    @Override
    public TitlePanel titlePanel(String title, Layout layout) {
        return ui.titlePanel(title, layout);
    }

    @Override
    public SplitPanel splitPanel() {
        return ui.splitPanel();
    }
    
    @Override
    public SplitPanel splitPanel(Align align) {
        return ui.splitPanel(align);
    }

    @Override
    public SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        return ui.splitPanel(w1,w2,align,ratio);
    }

    @Override
    public ScrollPanel scrollPanel() {
        return ui.scrollPanel();
    }

    @Override
    public TabPanel tabPanel() {
        return ui.tabPanel();
    }

    @Override
    public Tree tree() {
        return ui.tree();
    }

    @Override
    public TreeItem treeItem() {
        return ui.treeItem();
    }

    @Override
    public TreeItem treeItem(String text) {
        return ui.treeItem(text);
    }

    @Override
    public MenuBar menuBar() {
        return ui.menuBar();
    }

    @Override
    public MenuButton menuButton() {
        return ui.menuButton();
    }

    @Override
    public MenuButton menuButton(String text) {
        return ui.menuButton(text);
    }

    @Override
    public ToolBar toolBar() {
        return ui.toolBar();
    }

    @Override
    public ToolButton toolButton() {
        return ui.toolButton();
    }

    @Override
    public ToolButton toolButton(Icon icon) {
        return ui.toolButton(icon);
    }

    @Override
    public Overlay overlay() {
        return ui.overlay();
    }

    @Override
    public Icon icon(String file) {
        return ui.icon(file);
    }

    @Override
    public Font font(String font, int size) {
        return ui.font(font,size);
    }

    @Override
    public Spacer spacer() {
        return ui.spacer();
    }
    
    @Override
    public Spacer spacer(Align align) {
        return ui.spacer(align);
    }
    
    @Override
    public ListLayout listLayout() {
        return ui.listLayout();
    }

    @Override
    public ListLayout listLayout(Align align) {
        return ui.listLayout(align);
    }
    
    @Override
    public ListLayout listLayout(Align align, Margin margin, int spacing) {
        return ui.listLayout(align,margin,spacing);
    }
    
    @Override
    public ArrayLayout arrayLayout() {
        return ui.arrayLayout();
    }
    
}
