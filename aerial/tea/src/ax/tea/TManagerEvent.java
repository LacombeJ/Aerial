package ax.tea;

import java.util.ArrayList;

import ax.commons.time.Time;
import ax.tea.event.TEvent;
import ax.tea.event.TEventType;
import ax.tea.event.TFocusEvent;
import ax.tea.event.TKeyEvent;
import ax.tea.event.TMouseEvent;
import ax.tea.event.TMoveEvent;
import ax.tea.event.TResizeEvent;
import ax.tea.event.TScrollEvent;
import ax.tea.spatial.TPoint;

/**
 * 
 * @author Jonathan
 *
 */
public class TManagerEvent {

    // TODO simplify this code to make it less confusing and more readable.
    // Also define things such as the types of events that are withheld and sent
    // with mouse focus or even make parameters that widgets can toggle such
    // as whether it should receive events of mouse exiting window if the widget
    // is focused.
    
    // TODO send mouse motion events even when the mouse does not move (in cases where
    // a widget has been scrolled, etc)
    
    /*
     * How Event Managing works:
     * 
     * - The event manager will receive fire requests from the root widget and will decide
     *   which widgets to send events to.
     * - The deciding policy is usually the same for events, the manager will try to send events
     *   to widgets of the lowest level (without any children)
     * - If children fail to handle the event (return false) the event moves up to the parent and so forth.
     * - There are some exceptions that will be described below.
     * - Widgets may also request to be exempt from some of these policies to always or never receive
     *   certain types of events.
     * - These should be handled and described here.
     * 
     * This allows widgets to have control over their input (without conflicts with other widgets) and it also
     * makes integration with other elements not under the EventManager easier (such as having a game under the overlay of the UI)
     * 
     */
    
    /*//TODO implement the following:
     * Mouse focus widget:
     * - A widget can request the ability to take all mouse events with the parameters
     *   - Pass to children : whether to also pass these events to children like normal
     *   - Other widgets: List of widgets that events should also be passed to
     *   
     * Keyboard focus widget:
     * - A widget can request keyboard focus where only it will receive keyboard input
     *   
     */
    
    private final long DOUBLE_CLICK_SPEED_MS = 500;
    
    private TWidget keyboardFocusWidget = null;
    
    private TWidget mouseFocusWidget = null;
    private TPoint mouseFocusPoint = null;
    private TWidget mouseFocusRootWidget = null;
    
    public TManagerEvent() {
        
    }
    
    boolean hasKeyFocus(TWidget widget) {
        return widget == keyboardFocusWidget;
    }
    
    boolean grabKeyFocus(TWidget widget) {
        internalBindKeyFocus(widget);
        return true;
    }
    
    void releaseKeyFocus(TWidget widget) {
        internalFreeKeyFocus(widget);
    }
    
    private void internalBindKeyFocus(TWidget widget) {
        if (widget!=keyboardFocusWidget) {
            keyboardFocusWidget = widget;
            sendEvent(widget, new TFocusEvent(TEventType.FocusGained));
        }
    }
    
    private void internalFreeKeyFocus(TWidget widget) {
        if (widget != null && widget == keyboardFocusWidget) {
            sendEvent(widget, new TFocusEvent(TEventType.FocusLost));
            keyboardFocusWidget = null;
        } else {
            TWidget focusWidget = keyboardFocusWidget;
            if (keyboardFocusWidget != null) {
                keyboardFocusWidget = null;
                sendEvent(focusWidget, new TFocusEvent(TEventType.FocusLost));
            }
        }
    }
    
    private void internalBindFocus(TWidget widget, TMouseEvent event) {
        if (widget != null) {
            mouseFocusWidget = widget;
            mouseFocusPoint = new TPoint(event.globalX, event.globalY);
            mouseFocusRootWidget = widget.root();
        }
    }
    
    private void internalFreeFocus(TWidget widget, TMouseEvent event) {
        if (widget != null && widget == mouseFocusWidget) {
            
            int x = event.globalX;
            int y = event.globalY;
            int dx = event.globalX - mouseFocusPoint.x;
            int dy = event.globalY - mouseFocusPoint.y;
            
            // Fire mouse exist for the mouseFocusWidget if mouse isn't within widget
            // This is to get the correct inNow and inBefore
            // values when widget loses focus and internalFreeFocus is called. For example,
            // if a slider button requested focus and was moved the mouse focus point
            // called in internalBindFocus may fail on inBefore where we would want it
            // to succeed.
            TPoint eFocusPos = relative(mouseFocusRootWidget, mouseFocusWidget, x, y);
            if (eFocusPos != null) {
                if (!within(mouseFocusWidget, eFocusPos.x, eFocusPos.y)) {
                    TMouseEvent eventFocus = new TMouseEvent(TEventType.MouseExit, -1, eFocusPos.x, eFocusPos.y, x, y, dx, dy);
                    sendEvent(mouseFocusWidget, eventFocus);
                }
            }
            
            mouseFocusWidget = null;
            
            // This is here to send mouse motion events after mouse focus has been freed
            if (dx!=0 || dy!=0) {
                int prevX = mouseFocusPoint.x;
                int prevY = mouseFocusPoint.y;
                boolean inNow = TManagerEvent.within(mouseFocusRootWidget,x,y);
                boolean inBefore = TManagerEvent.within(mouseFocusRootWidget,prevX,prevY);
                if (inNow && !inBefore) {
                    fireMouseEnter(mouseFocusRootWidget, new TMouseEvent(TEventType.MouseEnter, -1, x, y, x, y, dx, dy));
                }
                if (!inNow && inBefore) {
                    fireMouseExit(mouseFocusRootWidget, new TMouseEvent(TEventType.MouseExit, -1, x, y, x, y, dx, dy));
                }
                fireMouseMove(mouseFocusRootWidget, new TMouseEvent(TEventType.MouseMove, -1, x, y, x, y, dx, dy));
            }
        }
        
        mouseFocusPoint = null;
        mouseFocusRootWidget = null;
    }
    
    /**
     * Checks if double click has been performed and sends an event if it has
     */
    private void checkDoubleClick(TWidget widget, TMouseEvent e, boolean wasInClickState) {
        if (wasInClickState) {
            sendEvent(widget, event(e, TEventType.MouseButtonClick));
            if (widget.event().timeSinceLastClick.elapsed() < DOUBLE_CLICK_SPEED_MS) {
                sendEvent(widget, event(e, TEventType.MouseButtonDoubleClick));
            }
            widget.event().timeSinceLastClick = new Time();
        }
    }
    
    /**
     * Returns true if a widget has mouse focus.
     * If a widget has mouse focus, this function sends an event directly to the widget with the
     * mouse focus. The widget in the parameter is only used to obtain the relative position
     * of the mouseFocusWidget to the given widget.
     * 
     * @param widget
     * @param e
     * @param released
     * @return
     */
    private boolean checkMouseFocusWidget(TWidget widget, TMouseEvent e, boolean released) {
        if (mouseFocusWidget != null) {
            boolean wasInClickState = mouseFocusWidget.event().inClickState;
            TPoint eFocusPos = relative(widget, mouseFocusWidget, e.x, e.y);
            if (eFocusPos != null) {
                TMouseEvent eventFocus = event(e, eFocusPos.x, eFocusPos.y);
                if (released) {
                    mouseFocusWidget.event().inClickState = false;
                    checkDoubleClick(mouseFocusWidget, eventFocus, wasInClickState);
                }
                // Right now we are omitting mouse exit events for focused widgets
                if (eventFocus.type!=TEventType.MouseExit) {
                    sendEvent(mouseFocusWidget, eventFocus);
                }
            }
            if (released) {
                internalFreeFocus(mouseFocusWidget, e);
            }
            return true;
        }
        return false;
    }
    
    
    private boolean sendMouseEventAndHandleMouseFocus(TWidget widget, TMouseEvent e) {
        if (widget.event().mouseFocusSupport) {
            internalBindFocus(widget, e);
        }
        return sendEvent(widget, e);
    }
    private TWidget foundWidget = null;
    public boolean fireMouseButtonPressed(TWidget widget, TMouseEvent e) {
        foundWidget = null;
        boolean fired = fireMouseButtonPressedInternal(widget,e);
        if (foundWidget==null) {
            internalFreeKeyFocus(null);
        } else if (foundWidget != keyboardFocusWidget) {
            internalBindKeyFocus(foundWidget);
        }
        return fired;
    }
    private boolean fireMouseButtonPressedInternal(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        widget.event().inClickState = true;
        ArrayList<TWidget> children = widget.getChildren();
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireMouseButtonPressedInternal(child,event(e, x, y))) {
                    if (foundWidget == null) {
                        foundWidget = child;
                    }
                    return true;
                }
            }
        }
        return sendMouseEventAndHandleMouseFocus(widget,e);
    }
    
    private boolean sendMouseEventAndHandleClickAndMouseFocus(TWidget widget, TMouseEvent e, boolean wasInClickState) {
        if (widget.event().mouseFocusSupport) {
            internalFreeFocus(mouseFocusWidget, e);
        }
        checkDoubleClick(widget, e, wasInClickState);
        return sendEvent(widget, e);
    }
    public boolean fireMouseButtonReleased(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,true)) {
            return false;
        }
        boolean wasInClickState = widget.event().inClickState;
        widget.event().inClickState = false;
        ArrayList<TWidget> children = widget.getChildren();
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
    
    public boolean fireMouseEnter(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        ArrayList<TWidget> children = widget.getChildren();
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireMouseEnter(child,event(e, x, y))) {
                    sendEvent(widget,e); //We want parents to receive events also
                    return true;
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    public boolean fireMouseExit(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        widget.event().inClickState = false;
        ArrayList<TWidget> children = widget.getChildren();
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            int prevX = x - e.dx;
            int prevY = y - e.dy;
            if (within(child,prevX,prevY)) {
                if (fireMouseExit(child,event(e, x, y))) {
                    sendEvent(widget,e); //We want parents to receive events also
                    return true;
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    public boolean fireMouseMove(TWidget widget, TMouseEvent e) {
        if (checkMouseFocusWidget(widget,e,false)) {
            return false;
        }
        ArrayList<TWidget> children = widget.getChildren();
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
                    if (!widget.event().mouseMotionBounds) {
                        return true;
                    }
                }
            }
        }
        return sendEvent(widget,e);
    }
    
    public boolean fireScroll(TWidget widget, TScrollEvent e) {
        ArrayList<TWidget> children = widget.getChildren();
        for (TWidget child : children) {
            int x = e.x - child.x;
            int y = e.y - child.y;
            if (within(child,x,y)) {
                if (fireScroll(child,event(e, x, y))) {
                    return true;
                }
            }
        }
        return sendEvent(widget, e);
    }
    
    /**
     * Sends a key event to the widget that has focus or to the given widget if there
     * is no keyboard focus widget.
     * 
     * @param widget
     * @param e
     * @return
     */
    public boolean fireKeyPressed(TWidget widget, TKeyEvent e) {
        TWidget w = (keyboardFocusWidget==null) ? widget : keyboardFocusWidget;
        return sendEvent(w, e);
    }
    
    public boolean fireKeyReleased(TWidget widget, TKeyEvent e) {
        TWidget w = (keyboardFocusWidget==null) ? widget : keyboardFocusWidget;
        return sendEvent(w, e);
    }
    
    public boolean fireKeyRepeat(TWidget widget, TKeyEvent e) {
        TWidget w = (keyboardFocusWidget==null) ? widget : keyboardFocusWidget;
        return sendEvent(w, e);
    }
    
    public void firePositionChanged(TWidget w, TMoveEvent e) {
        sendEvent(w,e);
    }
    
    public void fireSizeChanged(TWidget w, TResizeEvent e) {
        sendEvent(w,e);
    }
    
    
    
    /**
     * Sends event to widget directly
     * @param widget
     * @param e
     * @return true if event was handled or false if should be handled by parent
     */
    private boolean sendEvent(TWidget widget, TEvent event) {
        return widget.event(event);
    }
    
    
    // ------------------------------------------------------------------------
    
    public static final TPoint relative(TWidget ancestor, TWidget target, int x, int y) {
        if (ancestor==target) {
            return new TPoint(x,y);
        }
        if (ancestor.hasChild(target)) {
            int rx = x - target.x;
            int ry = y - target.y;
            return new TPoint(rx,ry);
        }
        if (ancestor.hasChildren()) {
            for (TWidget child : ancestor.getChildren()) {
                int rx = x - child.x;
                int ry = y - child.y;
                TPoint r = relative(child,target,rx,ry);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }
    
    public static final boolean within(TWidget widget, int x, int y) {
        return x>=0 && x<widget.width && y>=0 && y<widget.height;
    }
    
    public static final TMouseEvent event(TMouseEvent e, TEventType t) {
        return new TMouseEvent(t, e.button, e.x, e.y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
    public static final TMouseEvent event(TMouseEvent e, int x, int y) {
        return new TMouseEvent(e.type, e.button, x, y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
    public static final TMouseEvent event(TMouseEvent e, TEventType t, int x, int y) {
        return new TMouseEvent(t, e.button, x, y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
    public static final TScrollEvent event(TScrollEvent e, int x, int y) {
        return new TScrollEvent(e.type, e.sx, e.sy, x, y, e.globalX, e.globalY, e.dx, e.dy);
    }
    
}
