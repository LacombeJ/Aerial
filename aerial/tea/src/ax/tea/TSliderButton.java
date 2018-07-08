package ax.tea;

import ax.aui.Align;
import ax.graphics.Input;
import ax.math.vector.Mathi;
import ax.tea.event.TMouseEvent;
import ax.tea.graphics.ButtonRenderer;

public class  TSliderButton extends TButton {
    
    private TSlider slider;
    private int sliderWidth;
    private int sliderHeight;
    
    private float opos = 0;
    private int ovalue = 0;
    private boolean inAdjustState = false;
    
    TSliderButton(int width, int height) {
        super();
        sliderWidth = width;
        sliderHeight = height;
        setMouseFocusSupport(true);
    }
    
    void setSlider(TSlider slider) {
        this.slider = slider;
    }
    
    void setSliderWidth(int width) {
        if (sliderWidth != width) {
            sliderWidth = width;
            slider.invalidateLayout();
        }
        
    }
    
    void setSliderHeight(int height) {
        if (sliderHeight != height) {
            sliderHeight = height;
            slider.invalidateLayout();
        }
    }
    
    @Override
    protected TSizeHint sizeHint() {
        TSizeHint hint = new TSizeHint();
        hint.width = sliderWidth;
        hint.height = sliderHeight;
        return hint;
    }
    
    @Override
    protected void paint(TGraphics g) {
        ButtonRenderer.paint(this,"Slider.Button",g,info());
        paint().emit(cb->cb.f(g));
    }
    
    @Override
    protected boolean handleMouseButtonPress(TMouseEvent event) {
        super.handleMouseButtonPress(event);
        if (event.button==Input.MB_LEFT) {
            inAdjustState = true;
            opos = slider.align==Align.HORIZONTAL ? event.globalX : event.globalY;
            ovalue = slider.value;
            slider.pressed().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseButtonRelease(TMouseEvent event) {
        super.handleMouseButtonRelease(event);
        if (event.button==Input.MB_LEFT) {
            inAdjustState = false;
            slider.released().emit(cb->cb.f());
            return true;
        }
        return false;
    }
    
    @Override
    protected boolean handleMouseMove(TMouseEvent event) {
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
            return true;
        }
        return false;
    }
    
}