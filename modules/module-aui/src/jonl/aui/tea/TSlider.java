package jonl.aui.tea;

import jonl.aui.Align;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.Slider;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;
import jonl.vmath.Mathi;

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
    
    public TSlider(Align align, TSliderButton button, TSliderLayout layout) {
        super();
        
        setMouseFocusSupport(true);
        
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
    
    public TSlider(Align align) {
        this(align, new TSliderButton(20,20), new TSliderLayout());
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
        
        float trackerDim = 4f;
        float halfTracker = trackerDim * 0.5f;
        
        if (align==Align.HORIZONTAL) {
            g.renderRect(0, height()/2 - halfTracker, width(), trackerDim, style().dark());
        } else {
            g.renderRect(width()/2 - halfTracker, 0, trackerDim, height(), style().dark());
        }
        
        paint().emit(cb->cb.f(g));
    }

}
