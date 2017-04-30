package jonl.aui.sui;

import jonl.aui.Container;
import jonl.aui.SingleSlot;
import jonl.aui.Widget;

public abstract class AbstractSingleSlot extends SContainer implements SingleSlot {

    SWidget widget;
    
    @Override
    public boolean contains(Widget w) {
        return (widget!=null && widget.equals(w));
    }

    @Override
    public void remove(Widget w) {
        if (widget!=null) {
            if (widget.equals(w)) {
                SWidget sw = (SWidget)w;
                sw.parent = null;
                widget = null;
            }
        }
    }

    @Override
    public int getChildrenCount() {
        return widget!=null ? 1 : 0;
    }

    @Override
    public boolean hasChildren() {
        return widget!=null;
    }
    
    @Override
    public SWidget[] getChildren() {
        return widget!=null ? new SWidget[]{widget} : new SWidget[0];
    }
    
    @Override
    public SWidget getWidget() {
        return widget;
    }
    
    @Override
    public void setWidget(Widget w) {
        SWidget sw = (SWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        widget = sw;
        sw.parent = this;
    }
    
}
