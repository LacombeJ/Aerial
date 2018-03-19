package jonl.aui;

import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;

public interface Slider {

    int value();
    void setValue(int value);
    
    int min();
    void setMin(int min);
    
    int max();
    void setMax(int max);
    
    /** Emitted when slider is moved */
    Signal<Callback<Integer>> moved();
    
    /** Emitted when value is changed */
    Signal<Callback<Integer>> changed();
    
    /** Emitted when slider button is pressed */
    Signal<Callback0D> pressed();
    
    /** Emitted when slider button is released */
    Signal<Callback0D> released();
    
}
