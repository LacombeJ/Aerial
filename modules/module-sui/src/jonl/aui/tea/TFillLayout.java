package jonl.aui.tea;

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
    void layout() {
        if (!isEmpty()) {
            TLayoutItem item = getItem(0);
            
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            
            int sx = margin().left;
            int sy = margin().top;
            
            if (item instanceof TWidgetItem) {
                int wWidth = TLayoutManager.allocate(TLayoutManager.getWidthPreference(item), width);
                int wHeight = TLayoutManager.allocate(TLayoutManager.getHeightPreference(item), height);
                TLayoutManager.setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
            }
        }
    }
    
    @Override
    TSizeHint calculateSizeHint() {
        TLayoutItem item = getItem(0);
        
        int width = TLayoutManager.freeAllocate(TLayoutManager.getWidthPreference(item));
        int height = TLayoutManager.freeAllocate(TLayoutManager.getHeightPreference(item));
        
        width += margin().width();
        width += margin().height();
        
        return new TSizeHint(width, height);
    }

}
