package jonl.aui.sui;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Container;
import jonl.aui.MultiSlot;
import jonl.aui.Widget;

public abstract class AbstractMultiSlot extends SContainer implements MultiSlot {

    List<SWidget> children = new ArrayList<>();
    
    @Override
    public boolean contains(Widget w) {
        return children.contains(w);
    }
    
    @Override
    public void add(Widget w) {
        SWidget sw = (SWidget)w;
        Container c = w.getParent();
        if (c!=null) {
            c.remove(w);
        }
        children.add(sw);
        sw.parent = this;
    }
    
    @Override
    public void remove(Widget w) {
        if (children.remove(w)) {
            SWidget sw = (SWidget)w;
            sw.parent = null;
        }
    }
    
    @Override
    public SWidget remove(int i) {
        SWidget sw = children.remove(i);
        if (sw!=null) {
            sw.parent = null;
        }
        return sw;
    }
    
    @Override
    public SWidget getChild(int i) {
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
    public SWidget[] getChildren() {
        return children.toArray(new SWidget[0]);
    }
    
    
}
