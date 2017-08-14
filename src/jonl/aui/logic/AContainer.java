package jonl.aui.logic;

import jonl.aui.Container;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.Widget;

public abstract class AContainer extends AWidget implements Container {
    
    protected int[] getBox() {
        return new int[]{ 0, 0, getWidth(), getHeight() };
    }
    
    protected boolean isWithinBox(int x, int y) {
        int[] box = getBox();
        return (x>=box[0] && x<=box[2] && y>=box[1] && y<=box[3]);
    }
    
    @Override
    protected void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.fireSizeChanged(width,height,prevWidth,prevHeight);
        layout();
    }
    
    @Override
    protected void fireMouseExit(MouseMotionEvent e) {
        super.fireMouseExit(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            int PX = e.prevX - wx;
            int PY = e.prevY - wy;
            boolean inBefore = w.isWithin(PX,PY) && isWithinBox(e.prevX,e.prevY);
            //No need to check if inNow (if mouse exit parent, mouse should exit child)
            if (inBefore) {
                AWidget sw = (AWidget)w;
                sw.fireMouseExit(new MouseMotionEvent(MouseMotionEvent.EXIT,X,Y,PX,PY));
            }
        }
    }
    
    @Override
    protected void fireMouseHover(MouseMotionEvent e) {
        super.fireMouseHover(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            int prevX = e.prevX - wx;
            int prevY = e.prevY - wy;
            boolean inNow = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inNow) {
                AWidget sw = (AWidget)w;
                sw.fireMouseHover(new MouseMotionEvent(MouseMotionEvent.HOVER,X,Y,prevX,prevY));
            }
        }
    }
    
    @Override
    protected void fireMouseMoved(MouseMotionEvent e) {
        super.fireMouseMoved(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            int PX = e.prevX - wx;
            int PY = e.prevY - wy;
            boolean inNow = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            boolean inBefore = w.isWithin(PX,PY) && isWithinBox(e.prevX,e.prevY);
            AWidget sw = (AWidget)w;
            if (!inBefore && inNow) {
                sw.fireMouseEnter(new MouseMotionEvent(MouseMotionEvent.ENTER,X,Y,PX,PY));
            }
            if (inBefore && !inNow) {
                sw.fireMouseExit(new MouseMotionEvent(MouseMotionEvent.EXIT,X,Y,PX,PY));
            }
            if (inBefore && inNow) {
                if (X!=PX | Y!=PY) {
                    sw.fireMouseMoved(new MouseMotionEvent(MouseMotionEvent.MOVED,X,Y,PX,PY));
                }
            }
        }
    }
    
    @Override
    protected void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                AWidget sw = (AWidget)w;
                sw.fireMousePressed(new MouseButtonEvent(MouseButtonEvent.PRESSED,e.button,X,Y));
            }
        }
    }
    
    @Override
    protected void fireMouseDown(MouseButtonEvent e) {
        super.fireMouseDown(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                AWidget sw = (AWidget)w;
                sw.fireMouseDown(new MouseButtonEvent(MouseButtonEvent.DOWN,e.button,X,Y));
            }
        }
    }
    
    @Override
    protected void fireMouseReleased(MouseButtonEvent e) {
        super.fireMouseReleased(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                AWidget sw = (AWidget)w;
                sw.fireMouseReleased(new MouseButtonEvent(MouseButtonEvent.RELEASED,e.button,X,Y));
            }
        }
    }
    
    @Override
    protected void fireGlobalMousePressed(MouseButtonEvent e) {
        super.fireGlobalMousePressed(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMousePressed(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseDown(MouseButtonEvent e) {
        super.fireGlobalMouseDown(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseDown(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseReleased(MouseButtonEvent e) {
        super.fireGlobalMouseReleased(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseReleased(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseClicked(MouseButtonEvent e) {
        super.fireGlobalMouseClicked(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseClicked(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseMoved(MouseMotionEvent e) {
        super.fireGlobalMouseMoved(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseMoved(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseEnter(MouseMotionEvent e) {
        super.fireGlobalMouseEnter(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseEnter(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseHover(MouseMotionEvent e) {
        super.fireGlobalMouseHover(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseHover(e);
        }
    }
    
    @Override
    protected void fireGlobalMouseExit(MouseMotionEvent e) {
        super.fireGlobalMouseExit(e);
        for (Widget w : getChildren()) {
            AWidget sw = (AWidget)w;
            sw.fireGlobalMouseExit(e);
        }
    }
    
}
