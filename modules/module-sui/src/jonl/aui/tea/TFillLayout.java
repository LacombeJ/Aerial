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
                int wWidth = TManagerLayout.allocate(TManagerLayout.getWidthPreference(item), width);
                int wHeight = TManagerLayout.allocate(TManagerLayout.getHeightPreference(item), height);
                manager().layout().setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
            }
        }
    }
    
    @Override
    TSizeHint calculateSizeHint() {
        TLayoutItem item = getItem(0);
        
        int width = TManagerLayout.freeAllocate(TManagerLayout.getWidthPreference(item));
        int height = TManagerLayout.freeAllocate(TManagerLayout.getHeightPreference(item));
        
        width += margin().width();
        width += margin().height();
        
        return new TSizeHint(width, height);
    }

}
