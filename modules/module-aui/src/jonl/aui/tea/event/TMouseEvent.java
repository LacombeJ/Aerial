package jonl.aui.tea.event;

public class TMouseEvent extends TEvent {

    public final int button;
    public final int x;
    public final int y;
    public final int globalX;
    public final int globalY;
    public final int dx;
    public final int dy;
    
    public TMouseEvent(TEventType type, int button, int x, int y, int globalX, int globalY, int dx, int dy) {
        super(type);
        this.button = button;
        this.x = x;
        this.y = y;
        this.globalX = globalX;
        this.globalY = globalY;
        this.dx = dx;
        this.dy = dy;
    }
    
}
