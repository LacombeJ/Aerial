package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Layout;
import jonl.aui.LayoutItem;
import jonl.aui.Margin;
import jonl.aui.Widget;
import jonl.jutils.func.List;

public abstract class TLayout implements Layout {

    TWidget parent = null;
    
    private ArrayList<TLayoutItem> items = new ArrayList<>();
    
    private Margin margin = new Margin(9,9,9,9);
    private int spacing = 6;
    
    public TLayout() {
        
    }
    
    @Override
    public TWidget parent() {
        return parent;
    }

    @Override
    public TWidget getWidget() {
        for (TLayoutItem item : items) {
            if (item instanceof TWidgetItem) {
                return item.asWidget();
            }
        }
        return null;
    }
    
    @Override
    public void setWidget(Widget widget) {
        TWidget tw = (TWidget)widget;
        Widget prev = getWidget();
        if (prev != null) {
            remove(prev);
            add(tw);
        } else {
            add(tw);
        }
    }
    
    @Override
    public TWidget getWidget(int index) {
        TLayoutItem item = getItem(index);
        if (item instanceof TWidgetItem) {
            return item.asWidget();
        }
        return null;
    }
    
    @Override
    public TLayoutItem getItem(int index) {
        return items.get(index);
    }
    
    @Override
    public void add(Widget widget) {
        add(new TWidgetItem(widget));
        if (parent != null) {
            layout();
        }
    }
    
    @Override
    public void add(LayoutItem item) {
        if (items.add((TLayoutItem) item)) {
            ((TLayoutItem)item).layout = this;
            if (item instanceof TWidgetItem) {
                ((TWidgetItem) item).asWidget().parentLayout = this;
            }
        }
    }

    @Override
    public void remove(Widget widget) {
        for (TLayoutItem item : items) {
            if (item instanceof TWidgetItem) {
                TWidget tw = item.asWidget();
                if (tw == widget) {
                    remove(item);
                    break;
                }
            }
        }
    }
    
    @Override
    public void remove(LayoutItem item) {
        items.remove(item);
    }
    
    @Override
    public void remove(int index) {
        items.remove(index);
    }
    
    @Override
    public void removeAll() {
        items.clear();
    }
    
    @Override
    public int indexOf(Widget widget) {
        for (int i=0; i<items.size(); i++) {
            TLayoutItem item = items.get(i);
            if (item instanceof TWidgetItem) {
                if (item.asWidget() == widget) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    @Override
    public int indexOf(LayoutItem item) {
        return items.indexOf(item);
    }

    @Override
    public ArrayList<Widget> widgets() {
        ArrayList<Widget> widgets = new ArrayList<>();
        for (TLayoutItem item : items) {
            if (item instanceof TWidgetItem) {
                widgets.add(item.asWidget());
            }
        }
        return widgets;
    }
    
    @Override
    public ArrayList<LayoutItem> items() {
        return List.map(List.copy(items), i -> (TLayoutItem)i);
    }
    
    @Override
    public int count() {
        return items.size();
    }
    
    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public Margin margin() {
        return new Margin(margin);
    }

    @Override
    public void setMargin(Margin margin) {
        this.margin = new Margin(margin);
    }
    
    @Override
    public void setMargin(int left, int right, int top, int bottom) {
        margin = new Margin(left,right,top,bottom);
    }

    @Override
    public int spacing() {
        return spacing;
    }

    @Override
    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    
    // ------------------------------------------------------------------------
    
    void layout() {
        
    }

}
