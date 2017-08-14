package jonl.aui.logic;

import jonl.aui.Container;
import jonl.aui.SingleSlot;
import jonl.aui.Widget;

public abstract class ASingleSlot extends AContainer implements SingleSlot {

    AWidget widget;
    
    @Override
    public boolean contains(Widget w) {
        return (widget!=null && widget.equals(w));
    }

    @Override
    public void remove(Widget w) {
        if (widget!=null) {
            if (widget.equals(w)) {
                AWidget sw = (AWidget)w;
                sw.parent = null;
                widget = null;
                layout();
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
    public AWidget[] getChildren() {
        return widget!=null ? new AWidget[]{widget} : new AWidget[0];
    }
    
    @Override
    public AWidget getWidget() {
        return widget;
    }
    
    @Override
    public void setWidget(Widget w) {
        AWidget sw = (AWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        widget = sw;
        sw.parent = this;
        layout();
    }
    
}
