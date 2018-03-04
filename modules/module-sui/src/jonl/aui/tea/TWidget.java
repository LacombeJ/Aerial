package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.Layout;
import jonl.aui.Signal;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.aui.tea.event.TEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.jutils.func.Callback;
import jonl.jutils.time.Time;

public class TWidget implements Widget {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    protected TLayout parentLayout = null;
    private TLayout layout = null;
    
    private boolean enabled = true;
    
    private final Signal<Callback<Graphics>> paint = new Signal<>();
    
    // Variables used by event handler
    boolean eventInClickState = false;
    Time eventTimeSinceLastClick = new Time();
    boolean eventMouseFocusSupport = false;
    
    
    // AUI Widget Methods
    
    // ------------------------------------------------------------------------
    
    @Override
    public int x() {
        return x;
    }
    
    @Override
    public int y() {
        return y;
    }
    
    @Override
    public int width() {
        return width;
    }
    
    @Override
    public int height() {
        return height;
    }
    
    @Override
    public int windowX() {
        if (this instanceof Window) {
            return 0;
        }
        if (parent()!=null) {
            return x + parent().windowX();
        }
        return x;
    }

    @Override
    public int windowY() {
        if (this instanceof Window) {
            return 0;
        }
        if (parent()!=null) {
            return y + parent().windowY();
        }
        return y;
    }
    
    @Override
    public boolean enabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean enable) {
        enabled = enable;
    }
    
    @Override
    public Signal<Callback<Graphics>> paint() {
        return paint;
    }
    
    // ------------------------------------------------------------------------
    
    
    
    // T Widget Methods
    
    // ------------------------------------------------------------------------
    
    protected Layout widgetLayout() {
        return layout;
    }
    
    protected void setWidgetLayout(TLayout layout) {
        this.layout = layout;
        layout.parent = this;
    }
    
    protected boolean hasFocus() {
        return TEventHandler.hasFocus(this);
    }
    
    protected boolean requestFocus() {
        return TEventHandler.requestFocus(this);
    }
    
    protected void setMouseFocusSupport(boolean enable) {
        eventMouseFocusSupport = enable;
    }
    
    protected void paint(TGraphics g) {
        paint().emit(cb->cb.f(g));
    }
    
    protected void handleMouseButtonClick(TMouseEvent event) { }
    protected void handleMouseButtonDoubleClick(TMouseEvent event) { }
    protected void handleMouseButtonDown(TMouseEvent event) { }
    protected void handleMouseButtonPress(TMouseEvent event) { }
    protected void handleMouseButtonRelease(TMouseEvent event) { }
    protected void handleMouseEnter(TMouseEvent event) { }
    protected void handleMouseExit(TMouseEvent event) { }
    protected void handleMouseMove(TMouseEvent event) { }
    
    protected void handleMove(TMoveEvent event) { }
    protected void handleResize(TResizeEvent event) { }
    
    // ------------------------------------------------------------------------
    
    
    
    // T Package Methods
    
    // ------------------------------------------------------------------------
    
    TWidget root() {
        if (parent()!=null) {
            return parent().root();
        }
        return this;
    }
    
    TWidget parent() {
        if (parentLayout!=null) {
            return (TWidget) parentLayout.parent();
        }
        return null;
    }
    
    // ------------------------------------------------------------------------
    
    boolean event(TEvent event) {
        
        // Ignore mouse/keyboard events when disabled
        if (!enabled()) {
            
            switch(event.type) {
            
            case MouseButtonClick:          return false;
            case MouseButtonDoubleClick:    return false;
            case MouseButtonDown:           return false;
            case MouseButtonPress:          return false;
            case MouseButtonRelease:        return false;
            case MouseEnter:                return false;
            case MouseExit:                 return false;
            case MouseMove:                 return false;
            
            default:                        break;
            
            }
        }
        
        switch (event.type) {
        
        case MouseButtonClick:          handleMouseButtonClick((TMouseEvent)event);         break;
        case MouseButtonDoubleClick:    handleMouseButtonDoubleClick((TMouseEvent)event);   break;
        case MouseButtonDown:           handleMouseButtonDown((TMouseEvent)event);          break;
        case MouseButtonPress:          handleMouseButtonPress((TMouseEvent)event);         break;
        case MouseButtonRelease:        handleMouseButtonRelease((TMouseEvent)event);       break;
        case MouseEnter:                handleMouseEnter((TMouseEvent)event);               break;
        case MouseExit:                 handleMouseExit((TMouseEvent)event);                break;
        case MouseMove:                 handleMouseMove((TMouseEvent)event);                break;
        
        case Move:                      handleMove((TMoveEvent)event);                      break;
        case Resize:                    handleResize((TResizeEvent)event);                  break;
        
        }
        
        return true;
    }
    
    // ------------------------------------------------------------------------
    
    boolean hasChildren() {
        if (layout != null) {
            return !layout.isEmpty();
        }
        return false;
    }
    
    boolean hasChild(TWidget child) {
        if (layout != null) {
            return layout.contains(child);
        }
        return false;
    }
    
    int getChildrenCount() {
        if (layout != null) {
            return layout.size();
        }
        return 0;
    }
    
    TWidget getChild(int index) {
        if (layout != null) {
            return (TWidget) layout.get(index);
        }
        return null;
    }
    
    ArrayList<TWidget> getChildren() {
        ArrayList<TWidget> children = new ArrayList<>();
        if (layout != null) {
            for (Widget widget : layout.widgets()) {
                children.add((TWidget) widget);
            }
        }
        return children;
    }
    
    void layoutChildren() {
        if (layout != null) {
            layout.layout();
        }
    }
    
    // ------------------------------------------------------------------------

}
