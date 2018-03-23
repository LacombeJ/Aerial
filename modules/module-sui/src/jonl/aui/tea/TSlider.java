package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.Slider;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.TColor;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Mathf;

public class TSlider extends TWidget implements Slider {

    private Align align = Align.HORIZONTAL;
    private TSliderButton button;
    
    private int minValue = 0;
    private int maxValue = 100;
    private int value;
    
    private final Signal<Callback<Integer>> moved = new Signal<>();
    private final Signal<Callback<Integer>> changed = new Signal<>();
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    
    public TSlider(Align align) {
        super();
        setMouseFocusSupport(true);
        
        this.align = align;
        button = new TSliderButton(this);
        
        TSliderLayout layout = new TSliderLayout();
        layout.add(button);
        setWidgetLayout(layout);
    }
    
    public TSlider() {
        this(Align.HORIZONTAL);
    }
    
    @Override
    public Align align() {
        return align;
    }
    
    @Override
    public void setAlign(Align align) {
        this.align = align;
        invalidate();
    }
    
    
    @Override
    public int value() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = Mathf.clamp(value, minValue, maxValue);
        invalidate();
    }

    @Override
    public int min() {
        return minValue;
    }

    @Override
    public void setMin(int min) {
        minValue = min;
        setValue(value);
    }

    @Override
    public int max() {
        return maxValue;
    }

    @Override
    public void setMax(int max) {
        maxValue = max;
        setValue(value);
    }

    @Override
    public Signal<Callback<Integer>> moved() { return moved; }

    @Override
    public Signal<Callback<Integer>> changed() { return changed; }

    @Override
    public Signal<Callback0D> pressed() { return pressed; }

    @Override
    public Signal<Callback0D> released() { return released; }
    
    // ------------------------------------------------------------------------
    
    @Override
    protected TSizePolicy getSizePolicy() {
        //return style().dial().getSizePolicy(this,TWidgetInfo.widget());
        return new TSizePolicy();
    }
    
    @Override
    protected void paint(TGraphics g) {
        //style().dial().paint(this, TWidgetInfo.widget(), g);
        
        float sliderTrackWidth = 8f;
        float halfWidth = sliderTrackWidth * 0.5f;
        
        if (align==Align.HORIZONTAL) {
            g.renderRect(0, height()/2 - halfWidth, width(), halfWidth, TColor.WHITE);
        } else {
            g.renderRect(width()/2 - halfWidth, 0, halfWidth, height(), TColor.WHITE);
        }
        
        paint().emit(cb->cb.f(g));
    }
    
    
    
    static class TSliderButton extends TButton {
        
        private TSlider slider;
        
        private float opos = 0;
        private int ovalue = 0;
        private boolean inAdjustState = false;
        
        TSliderButton(TSlider slider) {
            super();
            setMouseFocusSupport(true);
            this.slider = slider;
        }
        
        @Override
        protected void handleMouseButtonPress(TMouseEvent event) {
            super.handleMouseButtonPress(event);
            if (event.button==Input.MB_LEFT) {
                inAdjustState = true;
                opos = slider.align==Align.HORIZONTAL ? event.globalX : event.globalY;
                ovalue = slider.value;
                slider.pressed().emit(cb->cb.f());
            }
        }
        
        @Override
        protected void handleMouseButtonRelease(TMouseEvent event) {
            super.handleMouseButtonRelease(event);
            if (event.button==Input.MB_LEFT) {
                inAdjustState = false;
                slider.released().emit(cb->cb.f());
            }
        }
        
        @Override
        protected void handleMouseMove(TMouseEvent event) {
            super.handleMouseMove(event);
            if (inAdjustState) {
                int newpos = event.globalX;
                int sliderDim = slider.width;
                int buttonDim = width;
                if (slider.align==Align.VERTICAL) {
                    newpos = event.globalY;
                    sliderDim = slider.height;
                    buttonDim = height;
                }
                
                int oldValue = slider.value;
                double percent = (newpos - opos) / (double) (sliderDim - buttonDim);
                int diff = (int) (percent * (slider.maxValue - slider.minValue));
                
                slider.value = ovalue + diff;
                
                slider.value = Mathf.clamp(slider.value, slider.minValue, slider.maxValue);
                
                if (slider.value!=oldValue) {
                    slider.invalidate();
                    slider.moved().emit(cb->cb.f(slider.value));
                    slider.changed().emit(cb->cb.f(slider.value));
                }
            }
        }
        
    }
    
    static class TSliderLayout extends TLayout {
        
        @Override
        void layout() {
            TSlider slider = (TSlider)parent;
            TButton button = slider.button;
            
            if (slider.align == Align.HORIZONTAL) {
                int x = (int) (Mathf.alpha(slider.value, slider.minValue, slider.maxValue) * (slider.width - button.width));
                TLayoutManager.setPositionAndRequestFire(button, x, 0);
                TLayoutManager.setSizeAndRequestFire(button, 20, slider.height);
            } else {
                int y = (int) (Mathf.alpha(slider.value, slider.minValue, slider.maxValue) * (slider.height - button.height));
                TLayoutManager.setPositionAndRequestFire(button, 0, y);
                TLayoutManager.setSizeAndRequestFire(button, slider.width, 20);
            }
        }
        
    }

}
