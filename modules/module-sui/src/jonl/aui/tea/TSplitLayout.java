package jonl.aui.tea;

public class TSplitLayout extends TLayout {

    @Override
    public void layout() {
        TSplitPanel container = (TSplitPanel) parent;
        
        int width = container.width();
        int height = container.height();
        TWidget w1 = container.widgetOne();
        TWidget w2 = container.widgetTwo();
        int b1 = (int) Math.round(spacing()/2.0);
        int b2 = spacing() - b1;
        switch (container.align()) {
        case HORIZONTAL:
            int medh = (int) Math.round(container.ratio() * width);
            int width1 = medh - b1;
            int width2 = width - (medh + b2);
            if (w1!=null) {
                TLayoutManager.setPositionAndRequestFire(w1, 0, 0);
                TLayoutManager.setSizeAndRequestFire(w1, width1, height);
            }
            if (w2!=null) {
                TLayoutManager.setPositionAndRequestFire(w2, width-width2, 0);
                TLayoutManager.setSizeAndRequestFire(w2, width2, height);
            }
            break;
        case VERTICAL:
            int medv = (int) Math.round(container.ratio() * height);
            int height1 = medv - b1;
            int height2 = height - (medv + b2);
            if (w1!=null) {
                TLayoutManager.setPositionAndRequestFire(w1, 0, 0);
                TLayoutManager.setSizeAndRequestFire(w1, width, height1);
            }
            if (w2!=null) {
                TLayoutManager.setPositionAndRequestFire(w2, 0, height-height2);
                TLayoutManager.setSizeAndRequestFire(w2, width, height2);
            }
            break;
        }
    }
    
}
