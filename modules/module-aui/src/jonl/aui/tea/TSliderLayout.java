package jonl.aui.tea;

import jonl.aui.Align;
import jonl.vmath.Mathf;

public class TSliderLayout extends TLayout {
    
    @Override
    public void layout() {
        TSlider slider = (TSlider)parent;
        TButton button = slider.button;
        
        if (slider.align == Align.HORIZONTAL) {
            int x = (int) (Mathf.alpha(slider.value, slider.minValue, slider.maxValue) * (slider.width - button.width));
            int y = slider.height/2 - button.height/2;
            setPositionAndSize(button, x, y, button.sizeHint().width, button.sizeHint().height);
        } else {
            int x = slider.width/2 - button.width/2;
            int y = (int) (Mathf.alpha(slider.value, slider.minValue, slider.maxValue) * (slider.height - button.height));
            setPositionAndSize(button, x, y, button.sizeHint().width, button.sizeHint().height);
        }
    }
    
    @Override
    public TSizeHint calculateSizeHint() {
        TSlider slider = (TSlider)parent;
        TButton button = slider.button;
        if (slider.align == Align.HORIZONTAL) {
            return new TSizeHint(button.sizeHint().width*3, button.sizeHint().height);
        } else {
            return new TSizeHint(button.sizeHint().width, button.sizeHint().height*3);
        }
    }
    
}
