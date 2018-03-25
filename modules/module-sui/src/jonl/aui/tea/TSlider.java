package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.Slider;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.graphics.TColor;
import jonl.jgl.Input;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Mathf;
import jonl.vmath.Mathi;

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
        
        if (this.align == Align.HORIZONTAL) {
            this.setSizePolicy(new SizePolicy(SizePolicy.PREFERRED, SizePolicy.FIXED));
        } else {
            this.setSizePolicy(new SizePolicy(SizePolicy.FIXED, SizePolicy.PREFERRED));
        }
        
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
        if (this.align != align) {
            this.align = align;
            invalidateLayout();
            invalidateSizeHint();
        }
    }
    
    
    @Override
    public int value() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = Mathi.clamp(value, minValue, maxValue);
        invalidateLayout();
    }

    @Override
    public int min() {
        return minValue;
    }

    @Override
    public void setMin(int min) {
        if (this.minValue != min) {
            minValue = min;
            this.value = Mathi.clamp(value, minValue, maxValue);
            invalidateLayout();
        }
    }

    @Override
    public int max() {
        return maxValue;
    }

    @Override
    public void setMax(int max) {
        if (this.maxValue != max) {
            maxValue = max;
            this.value = Mathi.clamp(value, minValue, maxValue);
            invalidateLayout();
        }
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
        private int dim = 20;
        
        private float opos = 0;
        private int ovalue = 0;
        private boolean inAdjustState = false;
        
        TSliderButton(TSlider slider) {
            super();
            setMouseFocusSupport(true);
            this.slider = slider;
        }
        
        @Override
        protected TSizeHint sizeHint() {
            TSizeHint hint = new TSizeHint();
            hint.width = dim;
            hint.height = dim;
            return hint;
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
                
                slider.value = Mathi.clamp(slider.value, slider.minValue, slider.maxValue);
                
                if (slider.value!=oldValue) {
                    slider.invalidateLayout();
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
                int y = slider.height/2 - button.height/2;
                TLayoutManager.setPositionAndSize(button, x, y, button.sizeHint().width, button.sizeHint().height);
            } else {
                int x = slider.width/2 - button.width/2;
                int y = (int) (Mathf.alpha(slider.value, slider.minValue, slider.maxValue) * (slider.height - button.height));
                TLayoutManager.setPositionAndSize(button, x, y, button.sizeHint().width, button.sizeHint().height);
            }
        }
        
        @Override
        TSizeHint calculateSizeHint() {
            TSlider slider = (TSlider)parent;
            TButton button = slider.button;
            if (slider.align == Align.HORIZONTAL) {
                return new TSizeHint(button.sizeHint().width*3, button.sizeHint().height);
            } else {
                return new TSizeHint(button.sizeHint().width, button.sizeHint().height*3);
            }
        }
        
    }

}
