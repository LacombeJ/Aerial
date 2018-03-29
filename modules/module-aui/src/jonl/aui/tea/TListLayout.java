package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ListLayout;
import jonl.aui.tea.TManagerLayout.SizePreference;
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
        //TODO should we invalidateLayout / sizeHint here?
        layout();
    }
    
    void layout() {
        if (!isEmpty()) {
            
            int numChildren = count();
            
            int width = parent.width() - margin().left - margin().right;
            int height = parent.height() - margin().bottom - margin().top;
            int sx = margin().left;
            int sy = margin().top;
            
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                width -= (numChildren-1) * spacing();
            } else {
                height -= (numChildren-1) * spacing();
            }
            // ----------------------------------------------
            
            Wrapper<Integer> extraDimension = new Wrapper<>(0);
            int[] sizes = null;
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                SizePreference[] prefs = TManagerLayout.getWidthPreferences(this);
                sizes = TManagerLayout.allocate(prefs, width, extraDimension);
            } else {
                SizePreference[] prefs = TManagerLayout.getHeightPreferences(this);
                sizes = TManagerLayout.allocate(prefs, height, extraDimension);
            }
            
            int extraSpacing = extraDimension.x / (numChildren+1);
            // ----------------------------------------------
            
            int start = 0;
            // ----------------------------------------------
            if (align == Align.HORIZONTAL) {
                start = sx + extraSpacing;
            } else {
                start = sy + extraSpacing;
            }
            // ----------------------------------------------
            for (int i=0; i<numChildren; i++) {
                TLayoutItem item = getItem(i);
                // ----------------------------------------------
                if (align == Align.HORIZONTAL) {
                    int wWidth = sizes[i];
                    int wHeight = TManagerLayout.allocate(TManagerLayout.getHeightPreference(item), height);
                    int wX = start + (spacing()+extraSpacing)*i;
                    int wY = sy + (height-wHeight)/2;
                    if (item instanceof TWidgetItem) {
                        manager().layout().setPositionAndSize(item.asWidget(), wX, wY, wWidth, wHeight);
                    }
                    start += wWidth;
                } else {
                    int wWidth = TManagerLayout.allocate(TManagerLayout.getWidthPreference(item), width);
                    int wHeight = sizes[i];
                    int wX = sx + (width-wWidth)/2;
                    int wY = start + (spacing()+extraSpacing)*i;
                    if (item instanceof TWidgetItem) {
                        manager().layout().setPositionAndSize(item.asWidget(), wX, wY, wWidth, wHeight);
                    }
                    start += wHeight;
                }
                // ----------------------------------------------
            }
        }
    }
    
    @Override
    TSizeHint calculateSizeHint() {
        if (align == Align.HORIZONTAL) {
            int width = TManagerLayout.freeAllocate(TManagerLayout.getWidthPreferences(this));
            int height = TManagerLayout.freeMaxAllocate(TManagerLayout.getHeightPreferences(this));
            width += margin().width() + spacing()*(count()-1);
            height += margin().height();
            return new TSizeHint(width, height);
        } else {
            int height = TManagerLayout.freeAllocate(TManagerLayout.getHeightPreferences(this));
            int width = TManagerLayout.freeMaxAllocate(TManagerLayout.getWidthPreferences(this));
            height += margin().height() + spacing()*(count()-1);
            width += margin().width();
            return new TSizeHint(width, height);
        }
    }
    
}
