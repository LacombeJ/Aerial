package jonl.aui.tea;

import java.util.ArrayList;

import jonl.aui.Graphics;
import jonl.aui.Signal;
import jonl.aui.SizePolicy;
import jonl.aui.Widget;
import jonl.aui.Window;
import jonl.aui.tea.call.TCaller;
import jonl.aui.tea.event.TEvent;
import jonl.aui.tea.event.TKeyEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.aui.tea.event.TScrollEvent;
import jonl.aui.tea.graphics.TStyle;
import jonl.aui.tea.graphics.TWidgetInfo;
import jonl.aui.tea.spatial.TSize;
import jonl.jutils.func.Callback;
import jonl.jutils.func.List;

public abstract class TWidget implements Widget {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    
    private TSize min = new TSize();
    private TSize max = new TSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
    private SizePolicy sizePolicy = new SizePolicy(SizePolicy.PREFERRED, SizePolicy.PREFERRED);
    private TSizeHint sizeHint = new TSizeHint();
    
    protected TLayout parentLayout = null;
    private TLayout layout = null;
    
    private boolean enabled = true;
    
    private final Signal<Callback<Graphics>> paint = new Signal<>();
    
    private TUIManager ui = null;
    
    private TWidgetInfo info = new TWidgetInfo();
    private TCaller caller = new TCaller();
    
    // Variables used by event handler
    private TEventHandler event = new TEventHandler();
    
    public TWidget() {
        ui = TUIManager.instance();
        
        info.put("this", this);
        
        caller().implement("SET_INFO", (args) -> {
            String key = args.get(0);
            String value = args.get(1);
            info.put(key, value);
            return true;
        });
    }
    
    {
        
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
        return min.width;
    }

    @Override
    public int minHeight() {
        return min.height;
    }

    @Override
    public void setMinSize(int width, int height) {
        min.width = width;
        min.height = height;
    }

    @Override
    public int maxWidth() {
        return max.width;
    }

    @Override
    public int maxHeight() {
        return max.height;
    }
    
    @Override
    public SizePolicy sizePolicy() {
        return sizePolicy;
    }
    
    @Override
    public void setSizePolicy(SizePolicy policy) {
        sizePolicy = policy;
    }

    @Override
    public void setMaxSize(int width, int height) {
        max.width = width;
        max.height = height;
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
    
    @Override
    public Object call(String call) {
        return caller.call(call);
    }
    
    // ------------------------------------------------------------------------
    
    
    
    // T Widget Methods
    
    // ------------------------------------------------------------------------
    
    protected TManager _root_manager() {
        return null;
    }
    
    protected TManager manager() {
        return root()._root_manager();
    }
    
    protected TStyle style() {
        return ui.getStyle();
    }
    
    protected TLayout widgetLayout() {
        return layout;
    }
    
    protected void setWidgetLayout(TLayout layout) {
        this.layout = layout;
        layout.parent = this;
    }
    
    protected TSizeHint sizeHint() {
        if (layout != null) {
            return layout.sizeHint();
        }
        return sizeHint;
    }

    protected boolean hasFocus() {
        return manager().event().hasKeyFocus(this);
    }
    
    protected boolean grabKeyFocus() {
        return manager().event().grabKeyFocus(this);
    }
    
    protected void releaseKeyFocus() {
        manager().event().releaseKeyFocus(this);
    }
    
    protected void setMouseFocusSupport(boolean enable) {
        event.mouseFocusSupport = enable;
    }
    
    /**
     * If set to true, this widget will get mouse move events while mouse is within bounds.
     * Normally it is set to false and the widget will only get events not handled by children.
     */
    protected void setMouseMotionBounds(boolean enable) {
        event.mouseMotionBounds = enable;
    }
    
    protected void setCursor(TCursor cursor) {
        if (parent() != null) {
            parent().setCursor(cursor);
        }
    }
    
    protected void paint(TGraphics g) {
        style().widget().paint(this, info, g);
        paint().emit(cb->cb.f(g));
    }
    
    protected TWidgetInfo info() {
        return info;
    }
    
    protected TCaller caller() {
        return caller;
    }
    
    // TODO add returns here
    
    protected boolean handleMouseButtonClick(TMouseEvent event) { return false; }
    protected boolean handleMouseButtonDoubleClick(TMouseEvent event) { return false; }
    protected boolean handleMouseButtonDown(TMouseEvent event) { return false; }
    protected boolean handleMouseButtonPress(TMouseEvent event) { return false; }
    protected boolean handleMouseButtonRelease(TMouseEvent event) { return false; }
    protected boolean handleMouseEnter(TMouseEvent event) { return false; }
    protected boolean handleMouseExit(TMouseEvent event) { return false; }
    protected boolean handleMouseMove(TMouseEvent event) { return false; }
    protected boolean handleScroll(TScrollEvent event) { return false; }
    
    protected boolean handleKeyPress(TKeyEvent event) { return false; }
    protected boolean handleKeyRelease(TKeyEvent event) { return false; }
    
    protected boolean handleMove(TMoveEvent event) { return false; }
    protected boolean handleResize(TResizeEvent event) { return false; }
    
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
    
    TEventHandler event() {
        return event;
    }
    
    boolean event(TEvent event) {
        
        // TODO add returns to handle functions
        
        // Ignore mouse/keyboard events when disabled
        if (!enabled()) {
            
            switch(event.type) {
            
            case MouseButtonClick:          return false;
            case MouseButtonDoubleClick:    return false;
            case MouseButtonPress:          return false;
            case MouseButtonRelease:        return false;
            case MouseEnter:                return false;
            case MouseExit:                 return false;
            case MouseMove:                 return false;
            case Scroll:                    return false;
            case KeyPress:                    return false;
            case KeyRelease:                    return false;
            
            default:                        break;
            
            }
        }
        
        switch (event.type) {
        
        case MouseButtonClick:          return handleMouseButtonClick((TMouseEvent)event);
        case MouseButtonDoubleClick:    return handleMouseButtonDoubleClick((TMouseEvent)event);
        case MouseButtonPress:          return handleMouseButtonPress((TMouseEvent)event);
        case MouseButtonRelease:        return handleMouseButtonRelease((TMouseEvent)event);
        case MouseEnter:                return handleMouseEnter((TMouseEvent)event);
        case MouseExit:                 return handleMouseExit((TMouseEvent)event);
        case MouseMove:                 return handleMouseMove((TMouseEvent)event);
        case Scroll:                    return handleScroll((TScrollEvent)event);
        case KeyPress:                  return handleKeyPress((TKeyEvent)event);
        case KeyRelease:                return handleKeyRelease((TKeyEvent)event);
        
        case Move:                      return handleMove((TMoveEvent)event);
        case Resize:                    return handleResize((TResizeEvent)event);
        
        }
        
        return false;
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
    
    // ------------------------------------------------------------------------
    
    /**
     * @return this widget as a layout item in respect to it's parent layout or null if this widget
     * does not have a parent layout
     */
    protected TLayoutItem asLayoutItem() {
        if (parentLayout != null) {
            int index = parentLayout.indexOf(this);
            if (index != -1) {
                return parentLayout.getItem(index);
            }
        }
        return null;
    }
    
    // ------------------------------------------------------------------------
    
    /**
     * Call whenever layout should change
     */
    void invalidateLayout() {
        if (manager() != null && layout != null) {
            manager().layout().invalidateLayout(layout);
        }
    }
    
    /**
     * Call whenever min, max, policy, or size hint is invalidated
     */
    void invalidateSizeHint() {
        if (manager() != null && parentLayout != null) {
            manager().layout().invalidateSizeHint(parentLayout);
        }
    }
    
    // ------------------------------------------------------------------------

}
