package jonl.aui.sui;

import java.util.ArrayList;

import jonl.aui.Container;
import jonl.aui.DoubleSlot;
import jonl.aui.Widget;

public abstract class AbstractDoubleSlot extends SContainer implements DoubleSlot {

    SWidget widget1;
    SWidget widget2;
    
    @Override
    public SWidget getWidgetOne() {
        return widget1;
    }
    
    private SWidget removeFromParent(Widget w) {
        SWidget sw = (SWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        return sw;
    }
    
    @Override
    public void setWidgetOne(Widget w) {
        SWidget sw = removeFromParent(w);
        widget1 = sw;
        sw.parent = this;
        layout();
    }
    
    @Override
    public SWidget getWidgetTwo() {
        return widget2;
    }
    
    @Override
    public void setWidgetTwo(Widget w) {
        SWidget sw = removeFromParent(w);
        widget2 = sw;
        sw.parent = this;
        layout();
    }
    
    @Override
    public boolean contains(Widget w) {
        return (widget1!=null && widget1.equals(w))
                || (widget2!=null && widget2.equals(w));
    }

    private SWidget removeWidget(SWidget widget, SWidget w) {
        if (widget!=null) {
            if (widget.equals(w)) {
                w.parent = null;
                return null;
            }
        }
        return widget;
    }
    
    @Override
    public void remove(Widget w) {
        widget1 = removeWidget(widget1,(SWidget) w);
        widget2 = removeWidget(widget2,(SWidget) w);
    }

    @Override
    public int getChildrenCount() {
        int count = 0;
        if (widget1!=null) count++;
        if (widget2!=null) count++;
        return count;
    }

    @Override
    public boolean hasChildren() {
        return widget1!=null || widget2!=null;
    }
    
    @Override
    public SWidget[] getChildren() {
        ArrayList<SWidget> w = new ArrayList<>();
        if (widget1!=null) w.add(widget1);
        if (widget2!=null) w.add(widget2);
        return w.toArray(new SWidget[0]);
    }
    
    
}
