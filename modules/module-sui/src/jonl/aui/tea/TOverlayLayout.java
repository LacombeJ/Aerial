package jonl.aui.tea;

import jonl.aui.LayoutItem;

public class TOverlayLayout extends TLayout {

    @Override
    public void layout() {
        
        for (LayoutItem layoutItem : items()) {
            TOverlayWidgetItem item = (TOverlayWidgetItem)layoutItem;
            
            TLayoutManager.setPositionAndSize(item.widget(), item.x, item.y, item.width, item.height);
        }
        
    }

    @Override
    TSizeHint calculateSizeHint() {
        return new TSizeHint();
    }
    
}
