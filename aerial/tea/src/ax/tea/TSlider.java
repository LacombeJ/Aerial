package ax.tea;

import ax.aui.Align;
import ax.aui.Signal;
import ax.aui.SizePolicy;
import ax.aui.Slider;
import ax.commons.func.Callback;
import ax.commons.func.Callback0D;
import ax.math.vector.Mathi;
import ax.tea.graphics.SliderRenderer;

public class TSlider extends TWidget implements Slider {

    Align align = Align.HORIZONTAL;
    TSliderButton button;
    
    int minValue = 0;
    int maxValue = 100;
    int value;
    
    private final Signal<Callback<Integer>> moved = new Signal<>();
    private final Signal<Callback<Integer>> changed = new Signal<>();
    private final Signal<Callback0D> pressed = new Signal<>();
    private final Signal<Callback0D> released = new Signal<>();
    
    public TSlider(Align align, TSliderButton button, TSliderLayout layout, int min, int max) {
        super();
        
        setMouseFocusSupport(true);
        
        minValue = min;
        maxValue = max;
        this.align = align;
        this.button = button;
        this.button.setSlider(this);
        layout.add(button);
        
        setWidgetLayout(layout);
        
        if (this.align == Align.HORIZONTAL) {
            this.setSizePolicy(new SizePolicy(SizePolicy.PREFERRED, SizePolicy.FIXED));
        } else {
            this.setSizePolicy(new SizePolicy(SizePolicy.FIXED, SizePolicy.PREFERRED));
        }
    }
    
    public TSlider(Align align, int min, int max) {
        this(align, new TSliderButton(20,20), new TSliderLayout(),min,max);
    }
    
    public TSlider(Align align, TSliderButton button) {
        this(align, button, new TSliderLayout(),0,100);
    }
    
    public TSlider(Align align) {
        this(align, new TSliderButton(20,20), new TSliderLayout(),0,100);
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
        SliderRenderer.paint(this,g,info());
        paint().emit(cb->cb.f(g));
    }

}
