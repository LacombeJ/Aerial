package jonl.aui.logic;

import jonl.aui.Border;
import jonl.aui.DoubleSlot;
import jonl.aui.Layout;

public class FixSplitLayout implements Layout<DoubleSlot> {
    
    Border type;
    int fix;
    
    public FixSplitLayout(Border type, int fix) {
        setSplit(type,fix);
    }
    
    public FixSplitLayout() {
        this(Border.TOP,32);
    }
    
    @Override
    public void layout(DoubleSlot container) {
        int width = container.getWidth();
        int height = container.getHeight();
        AWidget w0 = (AWidget) container.getWidgetOne();
        AWidget w1 = (AWidget) container.getWidgetTwo();
        switch(type) {
        case TOP:
            if (w0!=null) {
                w0.setPositionAndRequestFire(0,height-fix);
                w0.setSizeAndRequestFire(width,fix);
            }
            if (w1!=null) {
                w1.setPositionAndRequestFire(0,0);
                w1.setSizeAndRequestFire(width,height-fix);
            }
            break;
        case BOTTOM:
            if (w0!=null) {
                w0.setPositionAndRequestFire(0,0);
                w0.setSizeAndRequestFire(width,fix);
            }
            if (w1!=null) {
                w1.setPositionAndRequestFire(0,fix);
                w1.setSizeAndRequestFire(width,height-fix);
            }
            break;
        case LEFT:
            if (w0!=null) {
                w0.setPositionAndRequestFire(0,0);
                w0.setSizeAndRequestFire(fix,height);
            }
            if (w1!=null) {
                w1.setPositionAndRequestFire(fix,0);
                w1.setSizeAndRequestFire(width-fix,height);
            }
            break;
        case RIGHT:
            if (w0!=null) {
                w0.setPositionAndRequestFire(width-fix,0);
                w0.setSizeAndRequestFire(fix,height);
            }
            if (w1!=null) {
                w1.setPositionAndRequestFire(0,0);
                w1.setSizeAndRequestFire(width-fix,height);
            }
            break;
        }
    }
    
    public void setSplit(Border type, int fix) {
        setType(type);
        setFix(fix);
    }
    
    public void setType(Border type) {
        this.type = type;
    }
    
    public void setFix(int fix) {
        this.fix = fix;
    }

}
