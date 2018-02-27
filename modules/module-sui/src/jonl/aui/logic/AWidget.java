package jonl.aui.logic;

import java.util.ArrayList;
import java.util.List;

import jonl.aui.Action;
import jonl.aui.Container;
import jonl.aui.Graphics;
import jonl.aui.Int2ChangedListener;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseButtonListener;
import jonl.aui.MouseMotionEvent;
import jonl.aui.MouseMotionListener;
import jonl.aui.Painter;
import jonl.aui.Signal;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Tuple2i;

public abstract class AWidget implements Widget {

    private int x;
    private int y;
    private int width;
    private int height;
    
    private boolean inClickState = false;
    
    protected Container parent;
    
    List<Painter>               painters                            = new ArrayList<>();
    
    List<Int2ChangedListener>   positionChangedListeners            = new ArrayList<>();
    List<Int2ChangedListener>   sizeChangedListeners                = new ArrayList<>();
    List<MouseMotionListener>   mouseMotionListeners                = new ArrayList<>();
    List<MouseButtonListener>   mouseButtonListeners                = new ArrayList<>();
    List<Action>                actions                             = new ArrayList<>();
    
    List<MouseMotionListener>   globalMouseMotionListeners          = new ArrayList<>();
    List<MouseButtonListener>   globalMouseButtonListeners          = new ArrayList<>();
    
    @Override
    public Container getParent() {
        return parent;
    }
    
    @Override
    public Widget getRoot() {
        if (parent==null) {
            return this;
        }
        return parent.getRoot();
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    protected void setXAndRequestFire(int x) {
        int prevX = this.x;
        this.x = x;
        if (prevX!=x) {
            firePositionChanged(x,y,prevX,y);
        }
    }
    
    protected void setYAndRequestFire(int y) {
        int prevY = this.y;
        this.y = y;
        if (prevY!=y) {
            firePositionChanged(x,y,x,prevY);
        }
    }
    
    protected void setPositionAndRequestFire(int x, int y) {
        int prevX = this.x;
        int prevY = this.y;
        this.x = x;
        this.y = y;
        if (prevX!=x || prevY!=y) {
            firePositionChanged(x,y,prevX,prevY);
        }
    }
    
    protected void setWidthAndRequestFire(int width) {
        int prevWidth = this.width;
        this.width = width;
        if (prevWidth!=width) {
            fireSizeChanged(width,height,prevWidth,height);
        }
    }
    
    protected void setHeightAndRequestFire(int height) {
        int prevHeight = this.height;
        this.height = height;
        if (prevHeight!=height) {
            fireSizeChanged(width,height,width,prevHeight);
        }
    }
    
    protected void setSizeAndRequestFire(int width, int height) {
        int prevWidth = this.width;
        int prevHeight = this.height;
        this.width = width;
        this.height = height;
        if (prevWidth!=width || prevHeight!=height) {
            fireSizeChanged(width,height,prevWidth,prevHeight);
        }
    }
    
    protected void setXWithNoRequest(int x) {
        this.x = x;
    }
    
    protected void setYWithNoRequest(int y) {
        this.y = y;
    }
    
    protected void setWidthWithNoRequest(int width) {
        this.width = width;
    }
    
    protected void setHeightWithNoRequest(int height) {
        this.height = height;
    }
    
    @Override
    public int getWindowX() {
        if (this instanceof Window) {
            return 0;
        }
        if (parent!=null) {
            return x + parent.getWindowX();
        }
        return x;
    }

    @Override
    public int getWindowY() {
        if (this instanceof Window) {
            return 0;
        }
        if (parent!=null) {
            return y + parent.getWindowY();
        }
        return y;
    }

    @Override
    public int getScreenX() {
        if (parent!=null) {
            return x + parent.getScreenX();
        }
        return x;
    }

    @Override
    public int getScreenY() {
        if (parent!=null) {
            return y + parent.getScreenY();
        }
        return y;
    }
    
    
    private Tuple2i fromParentSpace(int x, int y) {
        return new Tuple2i(x + getX(), y + getY());
    }
    private Tuple2i toParentSpace(int x, int y) {
        return new Tuple2i(x - getX(), y - getY());
    }
    
    @Override
    public Tuple2i fromGlobalSpace(int x, int y) {
        if (this instanceof Window) {
            return new Tuple2i(x,y);
        }
        if (parent!=null) {
            Tuple2i c = toParentSpace(x,y);
            return parent.fromGlobalSpace(c.x, c.y);
        }
        return null;
    }
    
    @Override
    public Tuple2i toGlobalSpace(int x, int y) {
        if (this instanceof Window) {
            return new Tuple2i(x,y);
        }
        if (parent!=null) {
            Tuple2i c = fromParentSpace(x,y);
            return parent.toGlobalSpace(c.x, c.y);
        }
        return null;
    }
    
    /** @return if point is within local coordinate bounds */
    @Override
    public boolean isWithin(int x, int y) {
        return x>=0 && x<width && y>=0 && y<height;
    }
    
    // Signal handling
    //--------------------------------------------------------------------
    
    public <T> void emit(Signal<T> signal, Callback<T> cb) {
        signal.emit(cb);
    }
    
    public <T> void connect(Signal<T> signal, T slot) {
        signal.connect(slot);
    }
    
    //--------------------------------------------------------------------
    
    //Signals
    //--------------------------------------------------------------------
    
    private final Signal<Action> action= new Signal<>();
    public Signal<Action> action() { return action; }
    
    private final Signal<Painter> signalPaint = new Signal<>();
    @Override public Signal<Painter> paint() { return signalPaint; }
    
    private final Signal<Int2ChangedListener> signalPositionChanged = new Signal<>();
    @Override public Signal<Int2ChangedListener> positionChanged() { return signalPositionChanged; }
    
    private final Signal<Int2ChangedListener> signalSizeChanged = new Signal<>();
    @Override public Signal<Int2ChangedListener> sizeChanged() { return signalSizeChanged; }

    private final Signal<MouseMotionListener> signalMouseMoved= new Signal<>();
    @Override public Signal<MouseMotionListener> mouseMoved() { return signalMouseMoved; }
    
    private final Signal<MouseButtonListener> signalMouseButton= new Signal<>();
    @Override public Signal<MouseButtonListener> mouseButton() { return signalMouseButton; }
    
    private final Signal<MouseMotionListener> signalGlobalMouseMoved= new Signal<>();
    @Override public Signal<MouseMotionListener> globalMouseMoved() { return signalGlobalMouseMoved; }
    
    private final Signal<MouseButtonListener> signalGlobalMouseButton= new Signal<>();
    @Override public Signal<MouseButtonListener> globalMouseButton() { return signalGlobalMouseButton; }
    
    //--------------------------------------------------------------------
    
    //--------------------------------------------------------------------
    
    protected void paint(Graphics g) {
        paint().emit( (p) -> p.paint(g) );
    }
    
    protected void firePositionChanged(int x, int y, int prevX, int prevY) {
        positionChanged().emit( (pc) -> pc.valueChanged(x,y,prevX,prevY) );
    }
    
    protected void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        sizeChanged().emit( (sc) -> sc.valueChanged(width,height,prevWidth,prevHeight) );
    }
    
    protected void fireMouseMoved(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    protected void fireMouseEnter(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    protected void fireMouseHover(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );    
    }
    protected void fireMouseExit(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
        inClickState = false;
    }
    
    protected void fireMousePressed(MouseButtonEvent e) {
        mouseButton().emit( (m) -> m.occur(e) );
        inClickState = true;
    }
    protected void fireMouseDown(MouseButtonEvent e) {
        mouseButton().emit( (m) -> m.occur(e) );
    }
    protected void fireMouseReleased(MouseButtonEvent e) {
        mouseButton().emit( (m) -> m.occur(e) );
        if (inClickState) {
            fireMouseClicked(new MouseButtonEvent(MouseButtonEvent.CLICKED,e.button,e.x,e.y));
            inClickState = false;
        }
    }
    protected void fireMouseClicked(MouseButtonEvent e) {
        mouseButton().emit( (m) -> m.occur(e) );
    }
    
    protected void fireGlobalMouseMoved(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseEnter(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseHover(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseExit(MouseMotionEvent e) {
        mouseMoved().emit( (m) -> m.occur(e) );
    }
    
    protected void fireGlobalMousePressed(MouseButtonEvent e) {
        globalMouseButton().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseDown(MouseButtonEvent e) {
        globalMouseButton().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseReleased(MouseButtonEvent e) {
        globalMouseButton().emit( (m) -> m.occur(e) );
    }
    protected void fireGlobalMouseClicked(MouseButtonEvent e) {
        globalMouseButton().emit( (m) -> m.occur(e) );
    }
    
    protected void fireWidgetActions() {
        action().emit( (a) -> a.perform() );
    }
    
}
