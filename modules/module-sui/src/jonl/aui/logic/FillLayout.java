package jonl.aui.logic;

import jonl.aui.Layout;
import jonl.aui.SingleSlot;

public class FillLayout implements Layout<SingleSlot> {

    @Override
    public void layout(SingleSlot container) {
        AWidget w = (AWidget) container.getWidget();
        if (w!=null) {
            w.setPositionAndRequestFire(0,0);
            w.setSizeAndRequestFire(container.getWidth(),container.getHeight());
        }
    }

}
