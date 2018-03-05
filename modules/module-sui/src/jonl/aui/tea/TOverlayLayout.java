package jonl.aui.tea;

import jonl.aui.LayoutItem;

public class TOverlayLayout extends TLayout {

    @Override
    public void layout() {
        
        for (LayoutItem layoutItem : items()) {
            TOverlayWidgetItem item = (TOverlayWidgetItem)layoutItem;
            
            TLayoutManager.setPositionAndRequestFire(item.widget(), item.x, item.y);
            TLayoutManager.setSizeAndRequestFire(item.widget(), item.width, item.height);
        }
        
    }
    
}
