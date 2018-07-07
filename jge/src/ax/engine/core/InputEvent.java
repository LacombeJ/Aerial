package ax.engine.core;

public class InputEvent {

    public static final int KEY_PRESSED     = ax.graphics.InputEvent.KEY_PRESSED;
    public static final int KEY_RELEASED    = ax.graphics.InputEvent.KEY_RELEASED;
    public static final int KEY_DOWN        = ax.graphics.InputEvent.KEY_DOWN;
    
    public static final int BUTTON_PRESSED  = ax.graphics.InputEvent.BUTTON_PRESSED;
    public static final int BUTTON_RELEASED = ax.graphics.InputEvent.BUTTON_RELEASED;
    public static final int BUTTON_DOWN     = ax.graphics.InputEvent.BUTTON_DOWN;
    
    public final int type;
    public final int code;
    
    /**
     * Type is either {@link #KEY_PRESSED}, {@link #KEY_RELEASED}, {@link #KEY_DOWN},
     * {@link #BUTTON_PRESSED}, {@link #BUTTON_RELEASED}, {@link #BUTTON_DOWN}
     * <p>
     * Code is a key code or mouse button code from {@link ax.graphics.Input Input}
     * @param type input type
     * @param code key or mouse button code
     */
    public InputEvent(int type, int code) {
        this.type = type;
        this.code = code;
    }
    
}
