package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.tea.event.TScrollEvent;
import jonl.jutils.func.Callback;

public class TScrollBar extends TSlider {

    private Signal<Callback<Integer>> scrolled = new Signal<>();
    
    public TScrollBar(Align align) {
        super(align);
        
    }
    
    public Signal<Callback<Integer>> scrolled() { return scrolled; }
    
    @Override
    protected boolean handleScroll(TScrollEvent scroll) {
        scrolled.emit((cb)->cb.f(scroll.sy));
        return true;
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
