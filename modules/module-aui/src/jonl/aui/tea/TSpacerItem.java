package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.SizePolicy;
import jonl.aui.SpacerItem;
import jonl.aui.tea.spatial.TSize;

public class TSpacerItem extends TLayoutItem implements SpacerItem {

    private Align align = Align.VERTICAL;
    
    private TSize min = new TSize();
    private TSize max = new TSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private TSize hint = new TSize();
    private SizePolicy sizePolicy = new SizePolicy(SizePolicy.EXPANDING, SizePolicy.EXPANDING);
    
    public TSpacerItem() {
        
    }
    
    public TSpacerItem(Align align) {
        this.align = align;
    }
    
    @Override
    public Align align() {
        return align;
    }
    
    @Override
    public void setAlign(Align align) {
        this.align = align;
    }
    
    @Override
    public int minWidth() {
        return min.width;
    }

    @Override
    public int minHeight() {
        return min.height;
    }

    @Override
    public int maxWidth() {
        return max.width;
    }

    @Override
    public int maxHeight() {
        return max.height;
    }
    
    @Override
    public SizePolicy sizePolicy() {
        return sizePolicy;
    }
    
    @Override
    public int hintWidth() {
        return hint.width;
    }
    
    @Override
    public int hintHeight() {
        return hint.height;
    }
    
    @Override
    public void setMinSize(int width, int height) {
        min.width = width;
        min.height = height;
    }
    
    @Override
    public void setMaxSize(int width, int height) {
        max.width = width;
        max.height = height;
    }
    
    @Override
    public void setSizePolicy(SizePolicy policy) {
        sizePolicy = policy;
    }
    
    @Override
    public void setHint(int width, int height) {
        hint.width = width;
        hint.height = height;
    }
    
}
