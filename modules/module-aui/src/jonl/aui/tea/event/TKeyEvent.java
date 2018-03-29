package jonl.aui.tea.event;

public class TKeyEvent extends TEvent {

    public final static int NO_MOD = 0x00000000;
    public final static int SHIFT_MOD = 0x10000000;
    public final static int CTRL_MOD = 0x20000000;
    public final static int ALT_MOD = 0x40000000;
    
    public final int key;
    public final int modifier;

    // TODO make keys not dependent on Input class
    
    /**
     * 
     * @param type
     * @param button
     * @param x
     * @param y
     * @param globalX
     * @param globalY
     * @param dx
     * @param dy
     */
    public TKeyEvent(TEventType type, int key, int modifier) {
        super(type);
        this.key = key;
        this.modifier = modifier;
    }
    
}
