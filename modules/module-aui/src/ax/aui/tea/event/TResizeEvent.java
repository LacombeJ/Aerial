package ax.aui.tea.event;

public class TResizeEvent extends TEvent {
    
    public final int width;
    public final int height;
    public final int oldWidth;
    public final int oldHeight;
    
    public TResizeEvent(TEventType type, int width, int height, int oldWidth, int oldHeight) {
        super(type);
        this.width = width;
        this.height = height;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }
    
}
