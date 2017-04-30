package jonl.aui.sui;

import jonl.aui.Layout;
import jonl.aui.SingleSlot;

public class BoxLayout implements Layout<SingleSlot> {

    double top;
    double bottom;
    double left;
    double right;
    
    public BoxLayout(double top, double bottom, double left, double right) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void layout(SingleSlot container) {
        SWidget w = (SWidget) container.getWidget();
        int width = container.getWidth();
        int height = container.getHeight();
        int T = (int) (top);
        int B = (int) (bottom);
        int L = (int) (left);
        int R = (int) (right);
        if (w!=null) {
            w.setPositionAndRequestFire(L,B);
            w.setSizeAndRequestFire(width-L-R,height-B-T);
        }
    }

}
