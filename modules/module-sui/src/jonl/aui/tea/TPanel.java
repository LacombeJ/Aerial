package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Align;
import jonl.aui.Layout;
import jonl.aui.LayoutItem;
import jonl.aui.Panel;
import jonl.aui.Widget;

public class TPanel extends TWidget implements Panel {

    public TPanel(Layout layout) {
        super();
        setLayout(layout);
    }
    
    public TPanel() {
        this(new TListLayout(Align.VERTICAL));
    }
    
    @Override
    public Layout layout() {
        return widgetLayout();
    }

    @Override
    public void setLayout(Layout layout) {
        setWidgetLayout((TLayout) layout);
    }

    @Override
    public Widget getWidget(int index) {
        return layout().getWidget(index);
    }
    
    @Override
    public LayoutItem getItem(int index) {
        return layout().getItem(index);
    }

    @Override
    public void add(Widget widget) {
        layout().add(widget);
    }

    @Override
    public void add(LayoutItem item) {
        layout().add(item);
    }
    
    @Override
    public void remove(Widget widget) {
        layout().remove(widget);
    }
    
    @Override
    public void remove(LayoutItem item) {
        layout().remove(item);
    }
    
    @Override
    public void remove(int index) {
        layout().remove(index);
    }

    @Override
    public void removeAll() {
        layout().removeAll();
    }
    
    @Override
    public int indexOf(Widget widget) {
        return layout().indexOf(widget);
    }
    
    @Override
    public int indexOf(LayoutItem item) {
        return layout().indexOf(item);
    }
    
    @Override
    public int count() {
        return layout().count();
    }

    @Override
    public ArrayList<Widget> widgets() {
        return layout().widgets();
    }
    
    @Override
    public ArrayList<LayoutItem> items() {
        return layout().items();
    }
    
}
