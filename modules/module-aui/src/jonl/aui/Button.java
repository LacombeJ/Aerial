package jonl.aui;

import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback0D;

public interface Button extends Widget {
    
    String text();
    void setText(String text);
    
    Icon icon();
    void setIcon(Icon icon);
    
    boolean checkable();
    void setCheckable(boolean checkable);
    
    boolean checked();
    void setChecked(boolean checked);
    
    void toggle();
    
    /** Emitted when button is pressed down */
    Signal<Callback0D> pressed();
    
    /** Emitted when button is released */
    Signal<Callback0D> released();
    
    /** Emitted when button is clicked (pressed and released) */
    Signal<Callback0D> clicked();
    
    /**
     * Emitted when button is toggled on/off<br>
     * boolean checked -> if button is checked after toggle
     */
    Signal<Callback<Boolean>> toggled();
    
}
