package ax.aui;

import ax.commons.func.Callback;
import ax.commons.func.Callback0D;

public interface Dial extends Widget {
    
    int value();
    void setValue(int value);
    
    int min();
    void setMin(int min);
    
    int max();
    void setMax(int max);
    
    /** Emitted when dial is moved */
    Signal<Callback<Integer>> moved();
    
    /** Emitted when value is changed */
    Signal<Callback<Integer>> changed();
    
    /** Emitted when dial is pressed */
    Signal<Callback0D> pressed();
    
    /** Emitted when dial is released */
    Signal<Callback0D> released();
    
}
