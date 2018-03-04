package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Layout;
import jonl.aui.Margin;
import jonl.aui.Widget;

public abstract class TLayout implements Layout {

    TWidget parent = null;
    
    private ArrayList<TWidget> widgets = new ArrayList<>();
    
    private Margin margin = new Margin(9,9,9,9);
    private int spacing = 6;
    
    public TLayout() {
        
    }
    
    @Override
    public Widget parent() {
        return parent;
    }

    @Override
    public Widget get() {
        if (widgets.size()>0) {
            return widgets.get(0);
        }
        return null;
    }
    
    @Override
    public Widget get(int index) {
        return widgets.get(index);
    }
    
    @Override
    public void set(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (widgets.size()>0) {
            widgets.remove(0);
        }
        widgets.add(0, tw);
        tw.parentLayout = this;
    }
    
    @Override
    public void add(Widget widget) {
        TWidget tw = (TWidget)widget;
        if (widgets.add(tw)) {
            tw.parentLayout = this;
        }
    }

    @Override
    public void remove(Widget widget) {
        widgets.remove(widget);
    }
    
    @Override
    public void removeAll() {
        widgets.clear();
    }

    @Override
    public Widget[] widgets() {
        return widgets.toArray(new Widget[0]);
    }

    @Override
    public boolean contains(Widget widget) {
        return widgets.contains(widget);
    }
    
    @Override
    public boolean isEmpty() {
        return widgets.size()==0;
    }
    
    @Override
    public int size() {
        return widgets.size();
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
