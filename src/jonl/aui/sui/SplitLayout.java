package jonl.aui.sui;

import jonl.aui.Align;
import jonl.aui.DoubleSlot;
import jonl.aui.Layout;

class SplitLayout implements Layout<DoubleSlot> {
    
    Align align;
    double ratio;
    
    int border = 8;
    
    SplitLayout(Align align, double ratio) {
        this.align = align;
        this.ratio = ratio;
    }
    
    SplitLayout() {
        this(Align.HORIZONTAL,0.5);
    }
    
    @Override
    public void layout(DoubleSlot container) {
        int width = container.getWidth();
        int height = container.getHeight();
        SWidget w1 = (SWidget) container.getWidgetOne();
        SWidget w2 = (SWidget) container.getWidgetTwo();
        int b1 = (int) Math.round(border/2.0);
        int b2 = border - b1;
        switch (align) {
        case HORIZONTAL:
            int medh = (int) Math.round(ratio * width);
            int width1 = medh - b1;
            int width2 = width - (medh + b2);
            w1.setPositionAndRequestFire(0,0);
            w1.setSizeAndRequestFire(width1,height);
            w2.setPositionAndRequestFire(width-width2,0);
            w2.setSizeAndRequestFire(width2,height);
            break;
        case VERTICAL:
            int medv = (int) Math.round(ratio * height);
            int height1 = medv - b1;
            int height2 = height - (medv + b2);
            w1.setPositionAndRequestFire(0,0);
            w1.setSizeAndRequestFire(width,height1);
            w2.setPositionAndRequestFire(0,height-height2);
            w2.setSizeAndRequestFire(width,height2);
            break;
        }
    }

}
