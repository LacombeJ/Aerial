package ax.tea.event;

public class TMoveEvent extends TEvent {
    
    public final int x;
    public final int y;
    public final int oldX;
    public final int oldY;
    
    public TMoveEvent(TEventType type, int x, int y, int oldX, int oldY) {
        super(type);
        this.x = x;
        this.y = y;
        this.oldX = oldX;
        this.oldY = oldY;
    }
    
}
