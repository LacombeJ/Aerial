package jonl.aui.tea;

public class TFillLayout extends TLayout {
    
    @Override
    void layout() {
        if (!isEmpty()) {
            TLayoutItem item = getItem(0);
            
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            
            int sx = margin().left;
            int sy = margin().bottom;
            
            int wWidth = TLayoutManager.allocate(TLayoutManager.getWidthPreference(item), width);
            int wHeight = TLayoutManager.allocate(TLayoutManager.getHeightPreference(item), height);
            if (item instanceof TWidgetItem) {
                TLayoutManager.setPositionAndRequestFire(item.asWidget(), sx, sy);
                TLayoutManager.setSizeAndRequestFire(item.asWidget(), wWidth, wHeight);
            }
        }
    }
    
}
