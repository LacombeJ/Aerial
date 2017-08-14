package jonl.aui.sui;

import jonl.aui.Container;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.Widget;

public abstract class SContainer extends SWidget implements Container {
    
    int[] getBox() {
        return new int[]{ 0, 0, getWidth(), getHeight() };
    }
    
    boolean isWithinBox(int x, int y) {
        int[] box = getBox();
        return (x>=box[0] && x<=box[2] && y>=box[1] && y<=box[3]);
    }
    
    @Override
    void fireSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.fireSizeChanged(width,height,prevWidth,prevHeight);
        layout();
    }
    
    @Override
    void fireMouseExit(MouseMotionEvent e) {
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
                SWidget sw = (SWidget)w;
                sw.fireMouseExit(new MouseMotionEvent(MouseMotionEvent.EXIT,X,Y,PX,PY));
            }
        }
    }
    
    @Override
    void fireMouseHover(MouseMotionEvent e) {
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
                SWidget sw = (SWidget)w;
                sw.fireMouseHover(new MouseMotionEvent(MouseMotionEvent.HOVER,X,Y,prevX,prevY));
            }
        }
    }
    
    @Override
    void fireMouseMoved(MouseMotionEvent e) {
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
            SWidget sw = (SWidget)w;
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
    void fireMousePressed(MouseButtonEvent e) {
        super.fireMousePressed(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                SWidget sw = (SWidget)w;
                sw.fireMousePressed(new MouseButtonEvent(MouseButtonEvent.PRESSED,e.button,X,Y));
            }
        }
    }
    
    @Override
    void fireMouseDown(MouseButtonEvent e) {
        super.fireMouseDown(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                SWidget sw = (SWidget)w;
                sw.fireMouseDown(new MouseButtonEvent(MouseButtonEvent.DOWN,e.button,X,Y));
            }
        }
    }
    
    @Override
    void fireMouseReleased(MouseButtonEvent e) {
        super.fireMouseReleased(e);
        for (Widget w : getChildren()) {
            int wx = w.getX();
            int wy = w.getY();
            int X = e.x - wx;
            int Y = e.y - wy;
            boolean inside = w.isWithin(X,Y) && isWithinBox(e.x,e.y);
            if (inside) {
                SWidget sw = (SWidget)w;
                sw.fireMouseReleased(new MouseButtonEvent(MouseButtonEvent.RELEASED,e.button,X,Y));
            }
        }
    }
    
    @Override
    void fireGlobalMousePressed(MouseButtonEvent e) {
        super.fireGlobalMousePressed(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMousePressed(e);
        }
    }
    
    @Override
    void fireGlobalMouseDown(MouseButtonEvent e) {
        super.fireGlobalMouseDown(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseDown(e);
        }
    }
    
    @Override
    void fireGlobalMouseReleased(MouseButtonEvent e) {
        super.fireGlobalMouseReleased(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseReleased(e);
        }
    }
    
    @Override
    void fireGlobalMouseClicked(MouseButtonEvent e) {
        super.fireGlobalMouseClicked(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseClicked(e);
        }
    }
    
    @Override
    void fireGlobalMouseMoved(MouseMotionEvent e) {
        super.fireGlobalMouseMoved(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseMoved(e);
        }
    }
    
    @Override
    void fireGlobalMouseEnter(MouseMotionEvent e) {
        super.fireGlobalMouseEnter(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseEnter(e);
        }
    }
    
    @Override
    void fireGlobalMouseHover(MouseMotionEvent e) {
        super.fireGlobalMouseHover(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseHover(e);
        }
    }
    
    @Override
    void fireGlobalMouseExit(MouseMotionEvent e) {
        super.fireGlobalMouseExit(e);
        for (Widget w : getChildren()) {
            SWidget sw = (SWidget)w;
            sw.fireGlobalMouseExit(e);
        }
    }
    
}
