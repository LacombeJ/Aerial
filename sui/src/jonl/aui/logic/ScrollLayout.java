package jonl.aui.logic;

import jonl.aui.Layout;
import jonl.aui.SingleSlot;

public class ScrollLayout implements Layout<SingleSlot> {
    
    
    
    ScrollLayout() {
        
    }
    
    @Override
    public void layout(SingleSlot container) {
        AWidget w = (AWidget) container.getWidget();
        if (w!=null) {
            w.setPositionAndRequestFire(w.getX(),w.getY());
            w.setSizeAndRequestFire(w.getWidth(),w.getHeight());
        }
    }

}
