package jonl.aui.logic;

import java.util.ArrayList;

import jonl.aui.Container;
import jonl.aui.DoubleSlot;
import jonl.aui.Widget;

public abstract class ADoubleSlot extends AContainer implements DoubleSlot {

    AWidget widget1;
    AWidget widget2;
    
    @Override
    public AWidget getWidgetOne() {
        return widget1;
    }
    
    private AWidget removeFromParent(Widget w) {
        AWidget sw = (AWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        return sw;
    }
    
    @Override
    public void setWidgetOne(Widget w) {
        AWidget sw = removeFromParent(w);
        widget1 = sw;
        sw.parent = this;
        layout();
    }
    
    @Override
    public AWidget getWidgetTwo() {
        return widget2;
    }
    
    @Override
    public void setWidgetTwo(Widget w) {
        AWidget sw = removeFromParent(w);
        widget2 = sw;
        sw.parent = this;
        layout();
    }
    
    @Override
    public boolean contains(Widget w) {
        return (widget1!=null && widget1.equals(w))
                || (widget2!=null && widget2.equals(w));
    }

    private AWidget removeWidget(AWidget widget, AWidget w) {
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
        widget1 = removeWidget(widget1,(AWidget) w);
        widget2 = removeWidget(widget2,(AWidget) w);
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
    public AWidget[] getChildren() {
        ArrayList<AWidget> w = new ArrayList<>();
        if (widget1!=null) w.add(widget1);
        if (widget2!=null) w.add(widget2);
        return w.toArray(new AWidget[0]);
    }
    
    
}
