package jonl.aui.sui;

import jonl.aui.Align;
import jonl.aui.Layout;
import jonl.aui.MultiSlot;

public class ListLayout implements Layout<MultiSlot> {
    
    Align type;
    
    public ListLayout(Align type) {
        this.type = type;
    }
    
    public ListLayout() {
        this(Align.VERTICAL);
    }
    
    @Override
    public void layout(MultiSlot container) {
        if (type==Align.HORIZONTAL) {
            horizontal(container);
        } else {
            vertical(container);
        }
    }
    
    void horizontal(MultiSlot container) {
        int width = container.getWidth();
        int height = container.getHeight();
        int size = (width/container.getChildrenCount());
        int x = 0;
        int restWidth = width;
        for (int i=0; i<container.getChildrenCount(); i++) {
            if (i==container.getChildrenCount()-1) {
                size = restWidth;
            }
            SWidget w = (SWidget) container.getChild(i);
            w.setPositionAndRequestFire(x,0);
            w.setSizeAndRequestFire(size,height);
            x += size;
            restWidth -= size;
        }
    }
    
    void vertical(MultiSlot container) {
        if (container.hasChildren()) {
            int width = container.getWidth();
            int height = container.getHeight();
            int size = (height/container.getChildrenCount());
            int y = 0;
            int restHeight = height;
            for (int i=0; i<container.getChildrenCount(); i++) {
                if (i==container.getChildrenCount()-1) {
                    size = restHeight;
                }
                SWidget w = (SWidget) container.getChild(i);
                w.setPositionAndRequestFire(0,y);
                w.setSizeAndRequestFire(width,size);
                y += size;
                restHeight -= size;
            }
        }
    }

}
