package jonl.aui.tea;

import jonl.aui.Align;

public class TScrollBar extends TSlider {

    public TScrollBar(Align align) {
        super(align);
        
    }
    
    @Override
    protected void paint(TGraphics g) {
        
        float barDim = 20f;
        
        if (align==Align.HORIZONTAL) {
            g.renderRect(0, height()-barDim, width(), barDim, style().dark());
        } else {
            g.renderRect(width()-barDim, 0, barDim, height(), style().dark());
        }
        
        paint().emit(cb->cb.f(g));
        
    }
    
    void setBarSize(int size) {
        if (align==Align.HORIZONTAL) {
            button.setSliderWidth(size);
        } else {
            button.setSliderHeight(size);
        }
    }
    
}
