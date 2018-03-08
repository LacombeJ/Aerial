package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.Signal;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.aui.tea.event.TEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.aui.tea.graphics.TStyle;
import jonl.jutils.func.Callback;
import jonl.jutils.func.List;
import jonl.jutils.time.Time;

public abstract class TWidget implements Widget {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    private boolean useWidgetSizePolicy = true;
    private TSizePolicy defaultSizePolicy = new TSizePolicy();
    
    protected TLayout parentLayout = null;
    private TLayout layout = null;
    
    private boolean enabled = true;
    
    private final Signal<Callback<Graphics>> paint = new Signal<>();
    
    TStyle style = null;
    
    // Variables used by event handler
    boolean eventInClickState = false;
    Time eventTimeSinceLastClick = new Time();
    boolean eventMouseFocusSupport = false;
    
    public TWidget() {
        TUIManager.instance().enroll(this);
    }
    
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
    public int minWidth() {
        return sizePolicy().minWidth;
    }

    @Override
    public int minHeight() {
        return sizePolicy().minHeight;
    }

    @Override
    public void setMinSize(int width, int height) {
        useWidgetSizePolicy = false;
        defaultSizePolicy.minWidth = width;
        defaultSizePolicy.minHeight = height;
    }

    @Override
    public int maxWidth() {
        return sizePolicy().maxWidth;
    }

    @Override
    public int maxHeight() {
        return sizePolicy().maxHeight;
    }

    @Override
    public void setMaxSize(int width, int height) {
        useWidgetSizePolicy = false;
        defaultSizePolicy.maxWidth = width;
        defaultSizePolicy.maxHeight = height;
    }

    @Override
    public int preferredWidth() {
        return sizePolicy().prefWidth;
    }

    @Override
    public int preferredHeight() {
        return sizePolicy().prefHeight;
    }

    @Override
    public void setPreferredSize(int width, int height) {
        useWidgetSizePolicy = false;
        defaultSizePolicy.prefWidth = width;
        defaultSizePolicy.prefHeight = height;
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
    
    protected TStyle style() {
        return style;
    }
    
    protected TLayout widgetLayout() {
        return layout;
    }
    
    protected void setWidgetLayout(TLayout layout) {
        this.layout = layout;
        layout.parent = this;
    }
    
    protected final TSizePolicy sizePolicy() {
        if (useWidgetSizePolicy) {
            return getSizePolicy();
        }
        return defaultSizePolicy;
    }
    
    protected TSizePolicy getSizePolicy() {
        return defaultSizePolicy;
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
        return getChildrenCount() != 0;
    }
    
    boolean hasChild(TWidget child) {
        return getChildren().contains(child);
    }
    
    int getChildrenCount() {
        return getChildren().size();
    }
    
    TWidget getChild(int index) {
        return getChildren().get(index);
    }
    
    ArrayList<TWidget> getChildren() {
        if (layout != null) {
            return List.map(layout.widgets(), c -> (TWidget)c);
        }
        return new ArrayList<>();
    }
    
    void layoutChildren() {
        if (layout != null) {
            layout.layout();
        }
    }
    
    // ------------------------------------------------------------------------

}
