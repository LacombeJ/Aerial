package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.tea.event.TScrollEvent;
import jonl.aui.tea.graphics.ButtonRenderer;
import jonl.aui.tea.graphics.WidgetRenderer;
import jonl.jutils.func.Callback;

public class TScrollBar extends TSlider {

    private Signal<Callback<Integer>> scrolled = new Signal<>();
    
    public TScrollBar(Align align) {
        super(align, new TScrollButton(20,20));
        
    }
    
    public Signal<Callback<Integer>> scrolled() { return scrolled; }
    
    @Override
    protected boolean handleScroll(TScrollEvent scroll) {
        scrolled.emit((cb)->cb.f(scroll.sy));
        return true;
    }
    
    protected void paint(TGraphics g) {
        WidgetRenderer.paint(this,"ScrollPanel.Bar",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    void setBarSize(int size) {
        if (align==Align.HORIZONTAL) {
            button.setSliderWidth(size);
        } else {
            button.setSliderHeight(size);
        }
    }
    
    static class TScrollButton extends TSliderButton {

        TScrollButton(int width, int height) {
            super(width, height);
        }
        
        @Override
        protected void paint(TGraphics g) {
            ButtonRenderer.paint(this,"ScrollPanel.Button",g,info());
            paint().emit(cb->cb.f(g));
        }
        
    }
    
}
