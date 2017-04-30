package jonl.aui.sui;

import jonl.aui.Layout;
import jonl.aui.SingleSlot;

class ScrollLayout implements Layout<SingleSlot> {
    
    
    
    ScrollLayout() {
        
    }
    
    @Override
    public void layout(SingleSlot container) {
        SWidget w = (SWidget) container.getWidget();
        if (w!=null) {
            w.setPositionAndRequestFire(w.getX(),w.getY());
            w.setSizeAndRequestFire(w.getWidth(),w.getHeight());
        }
    }

}
