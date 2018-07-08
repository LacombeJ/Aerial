package ax.tea;

/**
 * This layout is meant to contain one widget.
 * 
 * @author Jonathan
 *
 */
public class TFillLayout extends TLayout {
    
    public TFillLayout() {
        super();
        setMargin(0, 0, 0, 0);
    }
    
    @Override
    public void layout() {
        if (!isEmpty()) {
            TLayoutItem item = getItem(0);
            
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            
            int sx = margin().left;
            int sy = margin().top;
            
            if (item instanceof TWidgetItem) {
                int wWidth = allocate(getWidthPreference(item), width);
                int wHeight = allocate(getHeightPreference(item), height);
                setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
            }
        }
    }
    
    @Override
    public TSizeHint calculateSizeHint() {
        if (isEmpty()) return new TSizeHint();
        
        TLayoutItem item = getItem(0);
        
        int width = freeAllocate(getWidthPreference(item));
        int height = freeAllocate(getHeightPreference(item));
        
        width += margin().width();
        height += margin().height();
        
        return new TSizeHint(width, height);
    }

}
