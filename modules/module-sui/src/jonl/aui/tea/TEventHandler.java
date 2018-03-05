package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.tea.event.TEvent;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.jutils.func.Tuple2i;
import jonl.jutils.time.Time;

class TEventHandler {

    private static final long DOUBLE_CLICK_SPEED_MS = 500;
    
    private static TWidget keyboardFocusWidget = null;
    
    private static TWidget mouseFocusWidget = null;
    
    static boolean hasFocus(TWidget widget) {
        return widget == keyboardFocusWidget;
    }
    
    static boolean requestFocus(TWidget widget) {
        keyboardFocusWidget = widget;
        return true;
    }
    
    private static void checkClick(TWidget widget, TMouseEvent e, boolean wasInClickState) {
        if (wasInClickState) {
            sendEvent(widget, event(e, TEventType.MouseButtonClick));
            if (widget.eventTimeSinceLastClick.elapsed() < DOUBLE_CLICK_SPEED_MS) {
                sendEvent(widget, event(e, TEventType.MouseButtonClick));
            }
            widget.eventTimeSinceLastClick = new Time();
        }
    }
    
    private static boolean checkMouseFocusWidget(TWidget widget, TMouseEvent e, boolean released) {
        if (mouseFocusWidget != null) {
            boolean wasInClickState = mouseFocusWidget.eventInClickState;
            Tuple2i eFocusPos = relative(widget, mouseFocusWidget, e.x, e.y);
            if (eFocusPos != null) {
                TMouseEvent eventFocus = event(e, eFocusPos.x, eFocusPos.y);
                if (released) {
                    mouseFocusWidget.eventInClickState = false;
                    checkClick(mouseFocusWidget, eventFocus, wasInClickState);
                }
                sendEvent(mouseFocusWidget, eventFocus);
            }
            if (released) {
                mouseFocusWidget = null;
            }
            return true;
        }
        return false;
    }
    
    
    private static boolean sendMouseEventAndHandleMouseFocus(TWidget widget, TMouseEvent e) {
        if (widget.eventMouseFocusSupport) {
            mouseFocusWidget = widget;
        }
        return sendEvent(widget, e);
    }
    static boolean fireMouseButtonPressed(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        widget.eventInClickState = true;
        ArrayList<TWidget> children = widget.getChildren();
        if (children.size()==0) {
            return sendMouseEventAndHandleMouseFocus(widget, e);
        }
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireMouseButtonPressed(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendMouseEventAndHandleMouseFocus(widget,e);
    }
    
    private static boolean sendMouseEventAndHandleClickAndMouseFocus(TWidget widget, TMouseEvent e, boolean wasInClickState) {
        if (widget.eventMouseFocusSupport) {
            mouseFocusWidget = null;
        }
        checkClick(widget, e, wasInClickState);
        return sendEvent(widget, e);
    }
    static boolean fireMouseButtonReleased(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,true)) {
            return false;
        }
        boolean wasInClickState = widget.eventInClickState;
        widget.eventInClickState = false;
        ArrayList<TWidget> children = widget.getChildren();
        if (children.size()==0) {
            return sendMouseEventAndHandleClickAndMouseFocus(widget, e, wasInClickState);
        }
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireMouseButtonReleased(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendMouseEventAndHandleClickAndMouseFocus(widget, e, wasInClickState);
    }
    
    static boolean fireMouseEnter(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        ArrayList<TWidget> children = widget.getChildren();
        if (children.size()==0) {
            return sendEvent(widget, e);
        }
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireMouseEnter(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    static boolean fireMouseExit(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        widget.eventInClickState = false;
        ArrayList<TWidget> children = widget.getChildren();
        if (children.size()==0) {
            return sendEvent(widget, e);
        }
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            int prevX = x - e.dx;
            int prevY = y - e.dy;
            if (within(child,prevX,prevY)) {
                if (fireMouseExit(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    static boolean fireMouseMove(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        ArrayList<TWidget> children = widget.getChildren();
        if (children.size()==0) {
            return sendEvent(widget, e);
        }
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            int prevX = x - e.dx;
            int prevY = y - e.dy;
            boolean inNow = within(child,x,y);
            boolean inBefore = within(child,prevX,prevY);
            if (inNow && !inBefore) {
                fireMouseEnter(child,event(e, TEventType.MouseEnter, x, y));
            }
            if (!inNow && inBefore) {
                fireMouseExit(child,event(e, TEventType.MouseExit, x, y));
            }
            if (inNow && inBefore) {
                if (fireMouseMove(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    static void firePositionChanged(TWidget w, TMoveEvent e) {
        sendEvent(w,e);
    }
    
    static void fireSizeChanged(TWidget w, TResizeEvent e) {
        w.layoutChildren();
        sendEvent(w,e);
    }
    
    
    
    /**
     * Sends event to widget directly
     * @param widget
     * @param e
     * @return true if event was handled or false if should be handled by parent
     */
    static boolean sendEvent(TWidget widget, TEvent event) {
        return widget.event(event);
    }
    
    
    // ------------------------------------------------------------------------
    
    static final Tuple2i relative(TWidget ancestor, TWidget target, int x, int y) {
        if (ancestor.hasChild(target)) {
            int rx = x - target.x;
            int ry = y - target.y;
            return new Tuple2i(rx,ry);
        }
        if (ancestor.hasChildren()) {
            for (TWidget child : ancestor.getChildren()) {
                int rx = x - child.x;
                int ry = y - child.y;
                Tuple2i r = relative(child,target,rx,ry);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }
    
    static final boolean within(TWidget widget, int x, int y) {
        return x>=0 && x<widget.width && y>=0 && y<widget.height;
    }
    
    static final TMouseEvent event(TMouseEvent e, TEventType t) {
        return new TMouseEvent(t, e.button, e.x, e.y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
    static final TMouseEvent event(TMouseEvent e, int x, int y) {
        return new TMouseEvent(e.type, e.button, x, y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
    static final TMouseEvent event(TMouseEvent e, TEventType t, int x, int y) {
        return new TMouseEvent(t, e.button, x, y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
}
