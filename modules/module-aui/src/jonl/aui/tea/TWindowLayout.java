package jonl.aui.tea;

import jonl.aui.LayoutItem;
import jonl.vmath.Mathi;

/**
 * This layout is a fill layout that limits the window size based on size hints
 * 
 * @author Jonathan
 *
 */
public class TWindowLayout extends TLayout {
    
    public TWindowLayout() {
        super();
        setMargin(0, 0, 0, 0);
    }
    
    @Override
    public void layout() {
        if (!isEmpty()) {
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            
            int sx = margin().left;
            int sy = margin().top;
            
            for (LayoutItem layoutItem : items()) {
                TLayoutItem item = (TLayoutItem)layoutItem;
                if (item instanceof TWidgetItem) {
                    int wWidth = allocate(getWidthPreference(item), width);
                    int wHeight = allocate(getHeightPreference(item), height);
                    setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
                }
            }
        }
    }
    
    @Override
    public TSizeHint calculateSizeHint() {
        int maxWidth = 0;
        int maxHeight = 0;
        
        for (LayoutItem layoutItem : items()) {
            TLayoutItem item = (TLayoutItem)layoutItem;
            
            int width = freeAllocate(getWidthPreference(item));
            int height = freeAllocate(getHeightPreference(item));
            
            width += margin().width();
            width += margin().height();
            
            maxWidth = Mathi.max(maxWidth, width);
            maxHeight = Mathi.max(maxHeight,height);
            
        }
        
        return new TSizeHint(maxWidth, maxHeight);
    }
    
    @Override
    void validateSizeHint(TSizeHint hint) {
        super.validateSizeHint(hint);
        
        TWindow window = (TWindow) parent;
        if (window.window()!=null) {
            int minWidth = window.minWidth();
            int minHeight = window.minHeight();
            
            int prefWidth = Math.max(minWidth, hint.width);
            int prefHeight = Math.max(minHeight, hint.height);
            // Using Integer.MAX_VALUE for glfw max size limits doesn't work. Using GLFW_DONT_CARE=-1 instead
            window.window().setSizeLimits(prefWidth, prefHeight, -1, -1);
        }
        
    }
    
}
