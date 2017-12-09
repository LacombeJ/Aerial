package jonl.aui;

public class MouseMotionEvent {

    public final static int MOVED   = 0;
    public final static int ENTER   = 1;
    public final static int HOVER   = 2;
    public final static int EXIT    = 3;
    
    public final int type;
    public final int x;
    public final int y;
    public final int prevX;
    public final int prevY;
    
    public MouseMotionEvent(int type, int x, int y, int prevX, int prevY) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.prevX = prevX;
        this.prevY = prevY;
    }
    
}
