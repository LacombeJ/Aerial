package jonl.aui.tea;

import jonl.jutils.io.Console;

public class TFrameLayout extends TLayout {

    TFrameLayout() {
        super();
        setMargin(0, 0, 0, 0);
    }
    
    @Override
    void layout() {
        if (!isEmpty()) {
            TFrame frame = (TFrame) parent;
            
            int width = parent.width() - frame.insets().left - frame.insets().right;
            int height = parent.height() - frame.insets().bottom - frame.insets().top;
            
            int sx = frame.insets().left;
            int sy = frame.insets().top;
            
            if (frame.widget() != null) {
                TLayoutItem item = getItem(indexOf(frame.widget()));
                int wWidth = TLayoutManager.allocate(TLayoutManager.getWidthPreference(item), width);
                int wHeight = TLayoutManager.allocate(TLayoutManager.getHeightPreference(item), height);
                TLayoutManager.setPositionAndRequestFire(item.asWidget(), sx, sy);
                TLayoutManager.setSizeAndRequestFire(item.asWidget(), wWidth, wHeight);
            }
            
            if (frame.frameBar() != null)
            {
                TLayoutItem barItem = getItem(indexOf(frame.frameBar()));
                TLayoutManager.setPositionAndRequestFire(barItem.asWidget(), sx, 0);
                TLayoutManager.setSizeAndRequestFire(barItem.asWidget(), parent.width, frame.insets().top);
            }
        }
    }
    
}
