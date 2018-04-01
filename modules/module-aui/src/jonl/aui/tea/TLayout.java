package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Layout;
import jonl.aui.LayoutItem;
import jonl.aui.Margin;
import jonl.aui.Widget;
import jonl.aui.tea.TLayoutManager.SizePreference;
import jonl.jutils.func.List;
import jonl.jutils.func.Wrapper;

public abstract class TLayout implements Layout {

    protected TWidget parent = null;
    
    private ArrayList<TLayoutItem> items = new ArrayList<>();
    
    private Margin margin = new Margin(9,9,9,9);
    private int spacing = 6;
    
    private TSizeHint sizeHint;
    
    public TLayout() {
        
    }
    
    public abstract void layout();
    
    public abstract TSizeHint calculateSizeHint();
    
    TSizeHint sizeHint() {
        if (sizeHint == null) {
            invalidateSizeHint();
        }
        return sizeHint;
    }
    
    TSizeHint getSizeHint() {
        return sizeHint;
    }
    
    void validateSizeHint(TSizeHint hint) {
        sizeHint = hint;
    }
    
    TManager manager() {
        return parent.manager();
    }
    
    // ------------------------------------------------------------------------
    
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
    }
    
    @Override
    public void add(LayoutItem item) {
        if (items.add((TLayoutItem) item)) {
            ((TLayoutItem)item).layout = this;
            if (item instanceof TWidgetItem) {
                TWidget widget = ((TWidgetItem) item).asWidget();
                widget.parentLayout = this;
            }
            if (parent != null) {
                parent.invalidateLayout();
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
    
    protected void invalidateLayout() {
        TLayoutManager.invalidateLayout(this);
    }
    
    protected void invalidateSizeHint() {
        TLayoutManager.invalidateSizeHint(this);
    }
    
    protected void setPosition(TWidget w, int x, int y) {
        TLayoutManager.setPosition(w, x, y);
    }
    
    protected void setSize(TWidget w, int width, int height) {
        TLayoutManager.setSize(w, width, height);
    }
    
    protected void setPositionAndSize(TWidget w, int x, int y, int width, int height) {
        TLayoutManager.setPositionAndSize(w, x, y, width, height);
    }
    
    protected int freeWidth(TWidget widget) {
        return TLayoutManager.freeWidth(widget);
    }
    
    protected int freeHeight(TWidget widget) {
        return TLayoutManager.freeHeight(widget);
    }
    
    protected int freeWidth(TLayoutItem item) {
        return TLayoutManager.freeWidth(item);
    }
    
    protected int freeHeight(TLayoutItem item) {
        return TLayoutManager.freeHeight(item);
    }

    protected int freeWidth(ArrayList<TLayoutItem> items) {
        return TLayoutManager.freeWidth(items);
    }
    
    protected int freeHeight(ArrayList<TLayoutItem> items) {
        return TLayoutManager.freeHeight(items);
    }
    
    protected SizePreference widthPref(TLayoutItem item) {
        return TLayoutManager.getWidthPreference(item);
    }
    
    protected SizePreference heightPref(TLayoutItem item) {
        return TLayoutManager.getHeightPreference(item);
    }
    
    protected SizePreference widthPref(TWidget widget) {
        return TLayoutManager.getWidthPreference(widget);
    }
    
    protected SizePreference heightPref(TWidget widget) {
        return TLayoutManager.getHeightPreference(widget);
    }
    
    protected SizePreference[] widthPrefs() {
        return TLayoutManager.getWidthPreferences(this);
    }
    
    protected SizePreference[] heightPrefs() {
        return TLayoutManager.getHeightPreferences(this);
    }
    
    protected int freeAllocate(SizePreference pref) {
        return TLayoutManager.freeAllocate(pref);
    }
    
    protected int freeAllocate(SizePreference[] prefs) {
        return TLayoutManager.freeAllocate(prefs);
    }
    
    protected int freeMaxAllocate(SizePreference[] prefs) {
        return TLayoutManager.freeMaxAllocate(prefs);
    }
    
    protected int allocate(SizePreference pref, int dimension) {
        return TLayoutManager.allocate(pref, dimension);
    }
    
    /**
     * Allocates sizes based on preferences such that added sizes are within their bounds and the
     * bounds of the given dimension<br>
     */
    protected int[] allocate(SizePreference[] prefs, int dimension, Wrapper<Integer> extra) {
        return TLayoutManager.allocate(prefs, dimension, extra);
    }
    
}
