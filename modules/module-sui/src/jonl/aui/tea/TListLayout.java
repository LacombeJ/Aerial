package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ListLayout;
import jonl.aui.tea.TLayoutManager.SizePreference;
import jonl.jutils.func.Wrapper;

public class TListLayout extends TLayout implements ListLayout {
    
    private Align align;
    
    public TListLayout(Align type) {
        this.align = type;
    }
    
    public TListLayout() {
        this(Align.VERTICAL);
    }
    
    @Override
    public Align align() {
        return align;
    }
    
    @Override
    public void setAlign(Align align) {
        this.align = align;
        layout();
    }
    
    void layout() {
        if (!isEmpty()) {
            int numChildren = count();
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            int sx = margin().left;
            int sy = margin().bottom;
            
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                width -= numChildren * spacing();
            } else {
                height -= numChildren * spacing();
            }
            // ----------------------------------------------
            
            Wrapper<Integer> newDimension = new Wrapper<>(0);
            Wrapper<Integer> totalDimension = new Wrapper<>(0);
            int[] sizes = null;
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                SizePreference[] prefs = TLayoutManager.getWidthPreferences(this);
                sizes = TLayoutManager.allocate(prefs, width, newDimension, totalDimension);
            } else {
                SizePreference[] prefs = TLayoutManager.getHeightPreferences(this);
                sizes = TLayoutManager.allocate(prefs, height, newDimension, totalDimension);
            }
            // ----------------------------------------------
            
            int start = 0;
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                start = sx;
            } else {
                start = sy;
            }
            // ----------------------------------------------
            for (int i=0; i<numChildren; i++) {
                TLayoutItem item = getItem(i);
                // ----------------------------------------------
                if (align == Align.HORIZONTAL) {
                    int wWidth = sizes[i];
                    int wHeight = TLayoutManager.allocate(TLayoutManager.getHeightPreference(item), height);
                    int wX = start + spacing()*i;
                    int wY = sy + (height-wHeight)/2;
                    if (item instanceof TWidgetItem) {
                        TLayoutManager.setPositionAndRequestFire(item.asWidget(), wX, wY);
                        TLayoutManager.setSizeAndRequestFire(item.asWidget(), wWidth, wHeight);
                    }
                    start += wWidth;
                } else {
                    int wWidth = TLayoutManager.allocate(TLayoutManager.getWidthPreference(item), width);
                    int wHeight = sizes[i];
                    int wX = sx + (width-wWidth)/2;
                    int wY = start + spacing()*i;
                    if (item instanceof TWidgetItem) {
                        TLayoutManager.setPositionAndRequestFire(item.asWidget(), wX, wY);
                        TLayoutManager.setSizeAndRequestFire(item.asWidget(), wWidth, wHeight);
                    }
                    start += wHeight;
                }
                // ----------------------------------------------
            }
        }
    }

}
