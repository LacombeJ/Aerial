package jonl.aui.sui;

import jonl.aui.Align;
import jonl.aui.Border;
import jonl.aui.Font;
import jonl.aui.Layout;
import jonl.aui.MultiSlot;
import jonl.aui.TreeItem;
import jonl.aui.UIManager;
import jonl.aui.Widget;

public class SUIManager implements UIManager {

    @Override
    public SWindow window() {
        return new SWindow();
    }

    @Override
    public SButton button() {
        return new SButton();
    }
    
    @Override
    public SButton button(String text) {
        return new SButton(text);
    }
    
    @Override
    public SLabel label() {
        return new SLabel();
    }
    
    @Override
    public SLabel label(String text) {
        return new SLabel(text);
    }

    @Override
    public SDial dial() {
        return new SDial();
    }
    
    @Override
    public SPanel panel() {
        return new SPanel();
    }
    
    @Override
    public SPanel panel(Layout<MultiSlot> layout) {
        return new SPanel(layout);
    }
    
    @Override
    public SSplitPanel splitPanel() {
        return new SSplitPanel();
    }

    @Override
    public SSplitPanel splitPanel(Widget w1, Widget w2, Align align, double ratio) {
        return new SSplitPanel(w1,w2,align,ratio);
    }
    
    @Override
    public SFixedSplitPanel fixedSplitPanel() {
        return new SFixedSplitPanel();
    }

    @Override
    public SFixedSplitPanel fixedSplitPanel(Widget w1, Widget w2, Border type, int fix) {
        return new SFixedSplitPanel(w1,w2,type,fix);
    }
    
    @Override
    public SScrollPanel scrollPanel() {
        return new SScrollPanel();
    }
    
    @Override
    public STree tree() {
        return new STree();
    }
    
    @Override
    public TreeItem treeItem() {
        return new STreeItem();
    }
    
    @Override
    public TreeItem treeItem(String text) {
        return new STreeItem(text);
    }

    @Override
    public Font font(String font, int size) {
        return new SFont(font,size);
    }
    
}
