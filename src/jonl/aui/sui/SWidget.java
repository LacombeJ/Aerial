package jonl.aui.sui;

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
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.jutils.func.Tuple2i;

public abstract class SWidget implements Widget {

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

    void setXAndRequestFire(int x) {
        int prevX = this.x;
        this.x = x;
        if (prevX!=x) {
            firePositionChanged(x,y,prevX,y);
        }
    }
    
    void setYAndRequestFire(int y) {
        int prevY = this.y;
        this.y = y;
        if (prevY!=y) {
            firePositionChanged(x,y,x,prevY);
        }
    }
    
    void setPositionAndRequestFire(int x, int y) {
        int prevX = this.x;
        int prevY = this.y;
        this.x = x;
        this.y = y;
        if (prevX!=x || prevY!=y) {
            firePositionChanged(x,y,prevX,prevY);
        }
    }
    
    void setWidthAndRequestFire(int width) {
        int prevWidth = this.width;
        this.width = width;
        if (prevWidth!=width) {
            fireSizeChanged(width,height,prevWidth,height);
        }
    }
    
    void setHeightAndRequestFire(int height) {
        int prevHeight = this.height;
        this.height = height;
        if (prevHeight!=height) {
            fireSizeChanged(width,height,width,prevHeight);
        }
    }
    
    void setSizeAndRequestFire(int width, int height) {
        int prevWidth = this.width;
        int prevHeight = this.height;
        this.width = width;
        this.height = height;
        if (prevWidth!=width || prevHeight!=height) {
            fireSizeChanged(width,height,prevWidth,prevHeight);
        }
    }
    
    void setXWithNoRequest(int x) {
        this.x = x;
    }
    
    void setYWithNoRequest(int y) {
        this.y = y;
    }
    
    void setWidthWithNoRequest(int width) {
        this.width = width;
    }
    
    void setHeightWithNoRequest(int height) {
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
    
    //Painters
    @Override
    public void addPainter(Painter p) {
        painters.add(p);
    }
    @Override
    public void removePainter(Painter p) {
        painters.remove(p);
    }
    @Override
    public Painter[] getPainters() {
        return painters.toArray(new Painter[0]);
    }
    void paint(Graphics g) {
        for (Painter p : painters) {
            p.paint(g);
        }
    }
    
    
    
    //Position changed events
    //--------------------------------------------------------------------
    @Override
    public void addPositionChangedListener(Int2ChangedListener pc) {
        positionChangedListeners.add(pc);
    }
    @Override
    public void removePositionChangedListener(Int2ChangedListener pc) {
        positionChangedListeners.remove(pc);
    }
    @Override
    public Int2ChangedListener[] getPositionChangedListeners() {
        return positionChangedListeners.toArray(new Int2ChangedListener[0]);
    }
    void firePositionChanged(int x, int y, int prevX, int prevY) {
        for (Int2ChangedListener pc : positionChangedListeners) {
            pc.valueChanged(x,y,prevX,prevY);
        }
    }
    
    //Size changed events
    //--------------------------------------------------------------------
    @Override
    public void addSizeChangedListener(Int2ChangedListener sc) {
        sizeChangedListeners.add(sc);
    }
    @Override
    public void removeSizeChangedListener(Int2ChangedListener sc) {
        sizeChangedListeners.remove(sc);
    }
    @Override
    public Int2ChangedListener[] getSizeChangedListeners() {
        return sizeChangedListeners.toArray(new Int2ChangedListener[0]);
    }    
    void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        for (Int2ChangedListener sc : sizeChangedListeners) {
            sc.valueChanged(width,height,prevWidth,prevHeight);
        }
    }
    
    //Mouse Motion events
    //--------------------------------------------------------------------
    @Override
    public void addMouseMotionListener(MouseMotionListener m) {
        mouseMotionListeners.add(m);
    }
    @Override
    public void removeMouseMotionListener(MouseMotionListener m) {
        mouseMotionListeners.remove(m);
    }
    @Override
    public MouseMotionListener[] getMouseMotionListener() {
        return mouseMotionListeners.toArray(new MouseMotionListener[0]);
    }
    
    void fireMouseMoved(MouseMotionEvent e) {
        for (MouseMotionListener m : mouseMotionListeners) {
            m.occur(e);
        }
    }
    void fireMouseEnter(MouseMotionEvent e) {
        for (MouseMotionListener m : mouseMotionListeners) {
            m.occur(e);
        }
    }
    void fireMouseHover(MouseMotionEvent e) {
        for (MouseMotionListener m : mouseMotionListeners) {
            m.occur(e);
        }      
    }
    void fireMouseExit(MouseMotionEvent e) {
        for (MouseMotionListener m : mouseMotionListeners) {
            m.occur(e);
        }
        inClickState = false;
    }
    
    //Mouse Button events
    //--------------------------------------------------------------------
    @Override
    public void addMouseButtonListener(MouseButtonListener m) {
        mouseButtonListeners.add(m);
    }
    @Override
    public void removeMouseButtonListener(MouseButtonListener m) {
        mouseButtonListeners.remove(m);
    }
    @Override
    public MouseButtonListener[] getMouseButtonListeners() {
        return mouseButtonListeners.toArray(new MouseButtonListener[0]);
    }
    void fireMousePressed(MouseButtonEvent e) {
        for (MouseButtonListener m : mouseButtonListeners) {
            m.occur(e);
        }
        inClickState = true;
    }
    void fireMouseDown(MouseButtonEvent e) {
        for (MouseButtonListener m : mouseButtonListeners) {
            m.occur(e);
        }
    }
    void fireMouseReleased(MouseButtonEvent e) {
        for (MouseButtonListener m : mouseButtonListeners) {
            m.occur(e);
        }
        if (inClickState) {
            fireMouseClicked(new MouseButtonEvent(MouseButtonEvent.CLICKED,e.button,e.x,e.y));
            inClickState = false;
        }
    }
    void fireMouseClicked(MouseButtonEvent e) {
        for (MouseButtonListener m : mouseButtonListeners) {
            m.occur(e);
        }
    }
    
    
    //Global Mouse Motion events
    //--------------------------------------------------------------------
    @Override
    public void addGlobalMouseMotionListener(MouseMotionListener m) {
        globalMouseMotionListeners.add(m);
    }
    @Override
    public void removeGlobalMouseMotionListener(MouseMotionListener m) {
        globalMouseMotionListeners.remove(m);
    }
    @Override
    public MouseMotionListener[] getGlobalMouseMotionListeners() {
        return globalMouseMotionListeners.toArray(new MouseMotionListener[0]);
    }
    void fireGlobalMouseMoved(MouseMotionEvent e) {
        for (MouseMotionListener m : globalMouseMotionListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseEnter(MouseMotionEvent e) {
        for (MouseMotionListener m : globalMouseMotionListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseHover(MouseMotionEvent e) {
        for (MouseMotionListener m : globalMouseMotionListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseExit(MouseMotionEvent e) {
        for (MouseMotionListener m : globalMouseMotionListeners) {
            m.occur(e);
        }
    }
    
    
    //Global Mouse Button events
    //--------------------------------------------------------------------
    @Override
    public void addGlobalMouseButtonListener(MouseButtonListener m) {
        globalMouseButtonListeners.add(m);
    }
    @Override
    public void removeGlobalMouseButtonListener(MouseButtonListener m) {
        globalMouseButtonListeners.remove(m);
    }
    @Override
    public MouseButtonListener[] getGlobalMouseButtonListeners() {
        return globalMouseButtonListeners.toArray(new MouseButtonListener[0]);
    }
    void fireGlobalMousePressed(MouseButtonEvent e) {
        for (MouseButtonListener m : globalMouseButtonListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseDown(MouseButtonEvent e) {
        for (MouseButtonListener m : globalMouseButtonListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseReleased(MouseButtonEvent e) {
        for (MouseButtonListener m : globalMouseButtonListeners) {
            m.occur(e);
        }
    }
    void fireGlobalMouseClicked(MouseButtonEvent e) {
        for (MouseButtonListener m : globalMouseButtonListeners) {
            m.occur(e);
        }
    }
    
    
    //Actions
    protected void addWidgetAction(Action action) {
        actions.add(action);
    }
    protected void removeWidgetAction(Action action) {
        actions.remove(action);
    }
    protected Action[] getWidgetActions() {
        return actions.toArray(new Action[0]);
    }
    protected void fireWidgetActions() {
        for (Action a : actions) {
            a.perform();
        }
    }
    
}
