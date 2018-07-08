package ax.tea;

public class TFrameLayout extends TLayout {

    TFrameLayout() {
        super();
        setMargin(0, 0, 0, 0);
    }
    
    @Override
    public void layout() {
        if (!isEmpty()) {
            TFrame frame = (TFrame) parent;
            
            int width = parent.width() - frame.insets().left - frame.insets().right;
            int height = parent.height() - frame.insets().bottom - frame.insets().top;
            
            int sx = frame.insets().left;
            int sy = frame.insets().top;
            
            if (frame.widget() != null) {
                TLayoutItem item = getItem(indexOf(frame.widget()));
                int wWidth = allocate(getWidthPreference(item), width);
                int wHeight = allocate(getHeightPreference(item), height);
                setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
            }
            
            if (frame.frameBar() != null)
            {
                TLayoutItem barItem = getItem(indexOf(frame.frameBar()));
                setPositionAndSize(barItem.asWidget(), sx, 0, parent.width, frame.insets().top);
            }
        }
    }
    
    @Override
    public TSizeHint calculateSizeHint() {
        TSizeHint hint = new TSizeHint();
        
        return hint;
    }
    
}
