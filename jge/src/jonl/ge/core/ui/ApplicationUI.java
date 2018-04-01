package jonl.ge.core.ui;

import jonl.aui.Widget;
import jonl.aui.tea.TCursor;
import jonl.aui.tea.TGraphics;
import jonl.aui.tea.TInput;
import jonl.aui.tea.TLayout;
import jonl.aui.tea.TLayoutItem;
import jonl.aui.tea.TManager;
import jonl.aui.tea.TManagerEvent;
import jonl.aui.tea.TSizeHint;
import jonl.aui.tea.TWidget;
import jonl.aui.tea.TWidgetItem;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TKeyEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TScrollEvent;
import jonl.jgl.GL;
import jonl.jgl.Input;
import jonl.jgl.Window;
import jonl.vmath.Matrix4;

public class ApplicationUI extends TWidget {

    Window window;
    TInput input;
    GL gl;
    
    UILayout layout;
    TWidget widget;
    TGraphics graphics;
    
    private TManager manager;
    
    private Matrix4 ortho;
    
    public ApplicationUI() {
        super();
        manager = new TManager();
        layout = new UILayout();
        setWidgetLayout(layout);
        
    }
    
    public void load(Window window) {
        this.window = window;
        
        input = new TInput(window.getInput(), () -> window.getHeight());
        gl = window.getGraphicsLibrary();
        
        graphics = new TGraphics(gl,() -> window.getHeight());
        ortho = Matrix4.orthographic(0,window.getWidth(),window.getHeight(),0,-1,1);
        graphics.setOrtho(ortho);
        
    }
    
    public void update() {
        layout.update(window.getX(), window.getY(), window.getWidth(), window.getHeight());
        
        invalidateLayout();
        
        graphics.beginGL();
        gl.glViewport(0,0,window.getWidth(),window.getHeight());
        ortho = Matrix4.orthographic(0,width,height,0,-1,1);
        graphics.setOrtho(ortho);

        graphics.paint(this);
        graphics.endGL();
        
        handleInput();
    }
    
    @Override
    protected TManager _root_manager() {
        return manager;
    }
    
    public TWidget widget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        if (widget != null) {
            this.widget = (TWidget) widget;
            widgetLayout().setWidget(widget);
        } else {
            if (!widgetLayout().isEmpty()) {
                widgetLayout().remove(0);
            }
        }
        
    }
    
    @Override
    public int windowX() {
        return 0;
    }

    @Override
    public int windowY() {
        return 0;
    }
    
    @Override
    public void setCursor(TCursor cursor) {
        switch (cursor) {
        case ARROW:
            window.setCursor(Window.ARROW_CURSOR);
            break;
        case IBEAM:
            window.setCursor(Window.IBEAM_CURSOR);
            break;
        case CROSSHAIR:
            window.setCursor(Window.CROSSHAIR_CURSOR);
            break;
        case HAND:
            window.setCursor(Window.HAND_CURSOR);
            break;
        case HRESIZE:
            window.setCursor(Window.HRESIZE_CURSOR);
            break;
        case VRESIZE:
            window.setCursor(Window.VRESIZE_CURSOR);
            break;
        }
    }
    
    private void handleInput() {
        int dx = (int) input.getDX();
        int dy = (int) input.getDY();
        int x = (int) input.getX();
        int y = (int) input.getY();
        
        // Mouse button events
        for (int i=Input.MB_LEFT; i<Input.MB_COUNT; i++) {
            if (input.isButtonPressed(i)) {
                manager.event().fireMouseButtonPressed(this, new TMouseEvent(TEventType.MouseButtonPress, i, x, y, x, y, dx, dy));
            }
            if (input.isButtonReleased(i)) {
                manager.event().fireMouseButtonReleased(this, new TMouseEvent(TEventType.MouseButtonRelease, i, x, y, x, y, dx, dy));
            }
        }
        
        // Mouse motion events
        if (dx!=0 || dy!=0) {
            int prevX = x - dx;
            int prevY = y - dy;
            boolean inNow = TManagerEvent.within(this,x,y);
            boolean inBefore = TManagerEvent.within(this,prevX,prevY);
            if (inNow && !inBefore) {
                manager.event().fireMouseEnter(this, new TMouseEvent(TEventType.MouseEnter, -1, x, y, x, y, dx, dy));
            }
            if (!inNow && inBefore) {
                manager.event().fireMouseExit(this, new TMouseEvent(TEventType.MouseExit, -1, x, y, x, y, dx, dy));
            }
            manager.event().fireMouseMove(this, new TMouseEvent(TEventType.MouseMove, -1, x, y, x, y, dx, dy));
        }
        
        // Mouse scroll wheel events
        int sx = (int) input.getScrollX();
        int sy = (int) input.getScrollY();
        if (sx!=0 || sy!=0) {
            manager.event().fireScroll(this, new TScrollEvent(TEventType.Scroll, sx, sy, x, y, x, y, dx, dy));
        }
        
        // Key events
        for (int i=Input.K_0; i<Input.K_COUNT; i++) {
            int mod = TKeyEvent.NO_MOD;
            if (input.isKeyDown(Input.K_LSHIFT) || input.isKeyDown(Input.K_RSHIFT)) {
                mod |= TKeyEvent.SHIFT_MOD;
            }
            if (input.isKeyDown(Input.K_LCONTROL) || input.isKeyDown(Input.K_RCONTROL)) {
                mod |= TKeyEvent.CTRL_MOD;
            }
            if (input.isKeyDown(Input.K_LALT) || input.isKeyDown(Input.K_RALT)) {
                mod |= TKeyEvent.ALT_MOD;
            }
            if (input.isKeyPressed(i)) {
                manager.event().fireKeyPressed(this, new TKeyEvent(TEventType.KeyPress, i, mod));
            }
            if (input.isKeyReleased(i)) {
                manager.event().fireKeyReleased(this, new TKeyEvent(TEventType.KeyRelease, i, mod));
            }
        }
    }
    
    
    static class UILayout extends TLayout {
        public UILayout() {
            super();
            setMargin(0, 0, 0, 0);
        }
        
        void update(int x, int y, int width, int height) {
            setSize(parent, width, height);
        }
        
        @Override
        public void layout() {
            if (!isEmpty()) {
                TLayoutItem item = getItem(0);
                
                int width = parent.width() - margin().left - margin().right;
                int height = parent.height() - margin().bottom - margin().top;
                
                int sx = margin().left;
                int sy = margin().top;
                
                if (item instanceof TWidgetItem) {
                    int wWidth = allocate(widthPref(item), width);
                    int wHeight = allocate(heightPref(item), height);
                    setPositionAndSize(item.asWidget(), sx, sy, wWidth, wHeight);
                }
            }
        }
        
        @Override
        public TSizeHint calculateSizeHint() {
            TLayoutItem item = getItem(0);
            
            int width = freeAllocate(widthPref(item));
            int height = freeAllocate(heightPref(item));
            
            width += margin().width();
            width += margin().height();
            
            return new TSizeHint(width, height);
        }
    }
    
}
