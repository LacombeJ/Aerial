package ax.aui.tea.event;

public class TScrollEvent extends TEvent {

    public final int sx;
    public final int sy;
    public final int x;
    public final int y;
    public final int globalX;
    public final int globalY;
    public final int dx;
    public final int dy;
    
    public TScrollEvent(TEventType type, int sx, int sy, int x, int y, int globalX, int globalY, int dx, int dy) {
        super(type);
        this.sx = sx;
        this.sy = sy;
        this.x = x;
        this.y = y;
        this.globalX = globalX;
        this.globalY = globalY;
        this.dx = dx;
        this.dy = dy;
    }
    
}
