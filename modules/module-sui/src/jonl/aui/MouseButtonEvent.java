package jonl.aui;

public class MouseButtonEvent {

    public final static int PRESSED     = 0;
    public final static int DOWN        = 1;
    public final static int RELEASED    = 2;
    public final static int CLICKED     = 3;
    
    public final int type;
    public final int button;
    public final int x;
    public final int y;
    
    public MouseButtonEvent(int type, int button, int x, int y) {
        this.type = type;
        this.button = button;
        this.x = x;
        this.y = y;
    }
    
}
