package ax.aui.tea.event;

public class TKeyEvent extends TEvent {

    public final static int NO_MOD = 0x00000000;
    public final static int SHIFT_MOD = 0x10000000;
    public final static int CTRL_MOD = 0x20000000;
    public final static int ALT_MOD = 0x40000000;
    
    public final int key;
    public final int modifier;
    public final char character;
    public boolean charValid;

    // TODO make keys not dependent on Input class

    public TKeyEvent(TEventType type, int key, int modifier, char character, boolean charValid) {
        super(type);
        this.key = key;
        this.modifier = modifier;
        this.character = character;
        this.charValid = charValid;
    }
    
}
