package jonl.aui.logic;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Container;
import jonl.aui.MultiSlot;
import jonl.aui.Widget;

public abstract class AMultiSlot extends AContainer implements MultiSlot {

    List<AWidget> children = new ArrayList<>();
    
    @Override
    public boolean contains(Widget w) {
        return children.contains(w);
    }
    
    @Override
    public void add(Widget w) {
        AWidget sw = (AWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        children.add(sw);
        sw.parent = this;
        layout();
    }
    
    @Override
    public void remove(Widget w) {
        if (children.remove(w)) {
            AWidget sw = (AWidget)w;
            sw.parent = null;
            layout();
        }
    }
    
    @Override
    public AWidget remove(int i) {
        AWidget sw = children.remove(i);
        if (sw!=null) {
            sw.parent = null;
            layout();
        }
        return sw;
    }
    
    @Override
    public AWidget getChild(int i) {
        if (!hasChildren()) return null;
        return children.get(i);
    }
    
    @Override
    public int getChildrenCount() {
        return children.size();
    }
    
    @Override
    public boolean hasChildren() {
        return children.size()!=0;
    }
    
    @Override
    public AWidget[] getChildren() {
        return children.toArray(new AWidget[0]);
    }
    
    
}
