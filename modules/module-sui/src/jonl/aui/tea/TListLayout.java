package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ListLayout;

public class TListLayout extends TLayout implements ListLayout {
    
    Align type;
    
    public TListLayout(Align type) {
        this.type = type;
    }
    
    public TListLayout() {
        this(Align.VERTICAL);
    }
    
    @Override
    public Align align() {
        return type;
    }
    
    @Override
    public void setAlign(Align align) {
        this.type = align;
        layout();
    }
    
    @Override
    void layout() {
        if (type==Align.HORIZONTAL) {
            horizontal(parent);
        } else {
            vertical(parent);
        }
    }
    
    void horizontal(TWidget container) {
        if (container.hasChildren()) {
            int width = container.width();
            int height = container.height();
            int size = (width/container.getChildrenCount());
            int x = 0;
            int restWidth = width;
            for (int i=0; i<container.getChildrenCount(); i++) {
                if (i==container.getChildrenCount()-1) {
                    size = restWidth;
                }
                TWidget w = container.getChild(i);
                TLayoutManager.setPositionAndRequestFire(w, x, 0);
                TLayoutManager.setSizeAndRequestFire(w, size, height);
                x += size;
                restWidth -= size;
            }
        }
    }
    
    void vertical(TWidget container) {
        if (container.hasChildren()) {
            int width = container.width();
            int height = container.height();
            int size = (height/container.getChildrenCount());
            int y = 0;
            int restHeight = height;
            for (int i=0; i<container.getChildrenCount(); i++) {
                if (i==container.getChildrenCount()-1) {
                    size = restHeight;
                }
                TWidget w = container.getChild(i);
                TLayoutManager.setPositionAndRequestFire(w, 0, y);
                TLayoutManager.setSizeAndRequestFire(w, width, size);
                y += size;
                restHeight -= size;
            }
        }
    }

}
