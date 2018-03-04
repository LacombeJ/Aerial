package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Font;
import jonl.aui.Label;
import jonl.aui.Layout;
import jonl.aui.ListLayout;
import jonl.aui.ScrollPanel;
import jonl.aui.SplitPanel;
import jonl.aui.Tree;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.aui.Widget;

public class TUIManager implements UIManager {
    
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
    public Label label() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Label label(String text) {
        // TODO Auto-generated method stub
        return null;
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
    public SplitPanel splitPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        // TODO Auto-generated method stub
        return null;
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
    public Font font(String font, int size) {
        // TODO Auto-generated method stub
        return null;
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
