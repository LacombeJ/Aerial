package jonl.jgl;

public class InputEvent {

    public static final int KEY_PRESSED     = 0;
    public static final int KEY_RELEASED    = 1;
    public static final int KEY_DOWN        = 2;
    
    public static final int BUTTON_PRESSED  = 3;
    public static final int BUTTON_RELEASED = 4;
    public static final int BUTTON_DOWN     = 5;
    
    public final int type;
    public final int code;
    
    /**
     * Type is either {@link #KEY_PRESSED}, {@link #KEY_RELEASED}, {@link #KEY_DOWN},
     * {@link #BUTTON_PRESSED}, {@link #BUTTON_RELEASED}, {@link #BUTTON_DOWN}
     * <p>
     * Code is a key code or mouse button code from {@link jonl.jgl.Input Input}
     * @param type input type
     * @param code key or mouse button code
     */
    public InputEvent(int type, int code) {
        this.type = type;
        this.code = code;
    }
    
}
