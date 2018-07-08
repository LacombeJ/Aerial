package ax.tea;

import ax.aui.Align;
import ax.aui.SizePolicy;
import ax.aui.Spacer;
import ax.tea.spatial.TSize;

public class TSpacer extends TLayoutItem implements Spacer {
    
    private TSize min = new TSize();
    private TSize max = new TSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private TSize hint = new TSize();
    private SizePolicy sizePolicy = new SizePolicy(SizePolicy.EXPANDING, SizePolicy.EXPANDING);
    
    public TSpacer() {
        
    }
    
    public TSpacer(int width, int height) {
        hint.width = width;
        hint.height = height;
        sizePolicy = new SizePolicy(SizePolicy.FIXED, SizePolicy.FIXED);
    }
    
    public TSpacer(Align align) {
        if (align==Align.HORIZONTAL) {
            sizePolicy = new SizePolicy(SizePolicy.EXPANDING, SizePolicy.FIXED);
        } else {
            sizePolicy = new SizePolicy(SizePolicy.FIXED, SizePolicy.EXPANDING);
        }
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
    public void setSizeConstraint(int width, int height) {
        setMinSize(width,height);
        setMaxSize(width,height);
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
