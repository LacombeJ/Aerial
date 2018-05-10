package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.ListLayout;
import jonl.aui.Margin;
import jonl.aui.tea.TLayoutManager.SizePreference;
import jonl.jutils.func.Wrapper;

public class TListLayout extends TLayout implements ListLayout {
    
    private Align align;
    
    private boolean stack = false;
    
    public TListLayout(Align align) {
        this.align = align;
    }
    
    public TListLayout() {
        this(Align.VERTICAL);
    }
    
    public TListLayout(Align align, Margin margin, int spacing) {
        this.align = align;
        this.margin = margin;
        this.spacing = spacing;
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
    
    public boolean stack() {
        return stack;
    }
    
    public void setStack(boolean enable) {
        stack = enable;
    }
    
    public void layout() {
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
                SizePreference[] prefs = getWidthPreferences();
                sizes = allocate(prefs, width, extraDimension);
            } else {
                SizePreference[] prefs = getHeightPreferences();
                sizes = allocate(prefs, height, extraDimension);
            }
            
            int extraSpacing = stack ? 0 : extraDimension.x / (numChildren+1);
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
                    int wHeight = allocate(getHeightPreference(item), height);
                    int wX = start + (spacing()+extraSpacing)*i;
                    int wY = sy + (height-wHeight)/2;
                    if (item instanceof TWidgetItem) {
                        setPositionAndSize(item.asWidget(), wX, wY, wWidth, wHeight);
                    }
                    start += wWidth;
                } else {
                    int wWidth = allocate(getWidthPreference(item), width);
                    int wHeight = sizes[i];
                    int wX = sx + (width-wWidth)/2;
                    int wY = start + (spacing()+extraSpacing)*i;
                    if (item instanceof TWidgetItem) {
                        setPositionAndSize(item.asWidget(), wX, wY, wWidth, wHeight);
                    }
                    start += wHeight;
                }
                // ----------------------------------------------
            }
        }
    }
    
    @Override
    public TSizeHint calculateSizeHint() {
        if (align == Align.HORIZONTAL) {
            int width = freeAllocate(getWidthPreferences());
            int height = freeMaxAllocate(getHeightPreferences());
            width += margin().width() + spacing()*(count()-1);
            height += margin().height();
            return new TSizeHint(width, height);
        } else {
            int height = freeAllocate(getHeightPreferences());
            int width = freeMaxAllocate(getWidthPreferences());
            height += margin().height() + spacing()*(count()-1);
            width += margin().width();
            return new TSizeHint(width, height);
        }
    }
    
}
