package jonl.aui.tea;

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
                int wWidth = TManagerLayout.allocate(TManagerLayout.getWidthPreference(item), width);
                int wHeight = TManagerLayout.allocate(TManagerLayout.getHeightPreference(item), height);
                manager().layout().setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
            }
            
            if (frame.frameBar() != null)
            {
                TLayoutItem barItem = getItem(indexOf(frame.frameBar()));
                manager().layout().setPositionAndSize(barItem.asWidget(), sx, 0, parent.width, frame.insets().top);
            }
        }
    }
    
    @Override
    TSizeHint calculateSizeHint() {
        TSizeHint hint = new TSizeHint();
        
        return hint;
    }
    
}
