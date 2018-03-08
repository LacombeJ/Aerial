package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.SpacerItem;

public class TSpacerItem extends TLayoutItem implements SpacerItem {

    private Align align = Align.VERTICAL;
    
    private TSizePolicy sizePolicy = new TSizePolicy();
    
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
        return sizePolicy.minWidth;
    }

    @Override
    public int minHeight() {
        return sizePolicy.minHeight;
    }

    @Override
    public int maxWidth() {
        return sizePolicy.maxWidth;
    }

    @Override
    public int maxHeight() {
        return sizePolicy.maxHeight;
    }
    
    @Override
    public int preferredWidth() {
        return sizePolicy.prefWidth;
    }

    @Override
    public int preferredHeight() {
        return sizePolicy.prefHeight;
    }
    
    @Override
    public void setMinSize(int width, int height) {
        sizePolicy.minWidth = width;
        sizePolicy.minHeight = height;
    }
    
    @Override
    public void setMaxSize(int width, int height) {
        sizePolicy.maxWidth = width;
        sizePolicy.maxHeight = height;
    }
    
    @Override
    public void setPreferredSize(int width, int height) {
        sizePolicy.prefWidth = width;
        sizePolicy.prefHeight = height;
    }
    
}
