package jonl.aui.tea;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayDeque;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TWindowEvent.Move;
import jonl.aui.tea.TWindowEvent.Resize;
import jonl.aui.tea.TWindowEvent.SetDecorated;
import jonl.aui.tea.TWindowEvent.SetResizable;
import jonl.aui.tea.TWindowEvent.SetVisible;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TKeyEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TScrollEvent;
import jonl.aui.tea.spatial.TPoint;
import jonl.jgl.Closer;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.Loader;
import jonl.jgl.Window;
import jonl.jgl.Window.Insets;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.lwjgl.GLFWWindow;
import jonl.jutils.parallel.SequentialProcessor;
import jonl.vmath.Mathi;
import jonl.vmath.Matrix4;

class TWindowManager {

    // ------------------------------------------------------------------------
    
    private GLFWWindow glWindow;
    private GraphicsLibrary gl;
    private Input input;
    
    private String title = "Window";
    private boolean visible;
    private boolean resizable;
    private boolean decorated = true;
    
    private boolean usePolicyWidth = true;
    private boolean usePolicyHeight = true;
    private boolean isXAligned = true;
    private boolean isYAligned = true;
    private HAlign halign = HAlign.CENTER;
    private VAlign valign = VAlign.MIDDLE;
    
    private final int displayWidth;
    private final int displayHeight;
    
    private Matrix4 ortho;
    
    private Loader loader = () -> {};
    private Closer closer = () -> {};
    
    private Object lock = new Object();
    
    // ------------------------------------------------------------------------
    
    private TWindow window;
    
    private TGraphics graphics;
    
    private ArrayDeque<TWindowEvent> windowEventQueue = new ArrayDeque<>();
    
    TWindowManager(TWindow window) {
        this.window = window;
        
        GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = g.getScreenDevices();
        
        displayWidth = devices[0].getDisplayMode().getWidth();
        displayHeight = devices[0].getDisplayMode().getHeight();
    }
    
    private TPoint getPositionAlignment(HAlign halign, VAlign valign) {
        int x = window.x;
        int y = window.y;
        
        double w = displayWidth;
        double h = displayHeight;
        
        switch (halign) {
        case LEFT:      x = 0;                                  break;
        case CENTER:    x = (int) ((w/2) - (window.width/2));   break;
        case RIGHT:     x = (int) (w - (window.width/2));       break;
        }
        switch(valign) {
        case TOP:       y = 0;                                  break;
        case MIDDLE:    y = (int) ((h/2) - (window.height/2));  break;
        case BOTTOM:    y = (int) (h - (window.height/2));      break;
        }
        
        return new TPoint(x,y);
    }
    
    void setPosition(HAlign halign, VAlign valign) {
        synchronized (lock) {
            TPoint p = getPositionAlignment(halign, valign);
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(p.x,p.y));
            } else {
                window.x = p.x;
                window.y = p.y;
                isXAligned = true;
                isYAligned = true;
                this.halign = halign;
                this.valign = valign;
            }
        }
    }
    
    void create() {
        SequentialProcessor sp = new SequentialProcessor();
        sp.setLockCount(1);
        sp.add(()->{
            synchronized (lock) {
                int minWidth = window.minWidth();
                int minHeight = window.minHeight();
                TSizeHint sizeHint = window.sizeHint();
                
                window.width = Math.max(minWidth, window.width);
                window.height = Math.max(minHeight, window.height);
                
                if (usePolicyWidth) {
                    window.width = Mathi.max(sizeHint.width, window.width, 1);
                }
                if (usePolicyHeight) {
                    window.height = Mathi.max(sizeHint.height, window.height, 1);
                }
                
                glWindow = new GLFWWindow(title,window.width,window.height,visible,false,resizable,decorated,4,false);
                
                
                TPoint aligned = getPositionAlignment(halign, valign);
                if (isXAligned) {
                    window.x = aligned.x;
                }
                if (isYAligned) {
                    window.y = aligned.y;
                }
                
                // We include insets here because the position of the gl window doesn't include the GLFW window frame isnets
                Insets insets = glWindow.getInsets();
                glWindow.setPosition(insets.left+window.x,insets.top+window.y);
                
                // Using Integer.MAX_VALUE for glfw max size limits doesn't work. Using GLFW_DONT_CARE=-1 instead
                int prefWidth = Math.max(minWidth, sizeHint.width);
                int prefHeight = Math.max(minHeight, sizeHint.height);
                glWindow.setSizeLimits(prefWidth, prefHeight, -1, -1);
            }
            gl = glWindow.getGraphicsLibrary();
            input = new TInput(glWindow.getInput(),()->window.height);
            
            
            glWindow.addCursorListener((enter)->{
                if (enter) {
                    
                }
            });
            
            glWindow.addPositionListener((x,y,prevX,prevY)->{
                TLayoutManager.setPosition(window, x, y);
                //TEventManager.firePositionChanged(window, new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
            });
            
            glWindow.addSizeListener((width,height,prevWidth,prevHeight)->{
                TLayoutManager.setSize(window, width, height);
                //TEventManager.fireSizeChanged(window, new TResizeEvent(TEventType.Resize, width,height,prevWidth,prevHeight));
                if (graphics!=null) {
                    ortho = Matrix4.orthographic(0,width,height,0,-1,1);
                    graphics.setOrtho(ortho);
                }
            });
            
            // Counts down and frees the thread that created this thread
            sp.countDown();
            
            glWindow.setLoader(()->{
                graphics = new TGraphics(gl,()->window.height);
                ortho = Matrix4.orthographic(0,glWindow.getWidth(),glWindow.getHeight(),0,-1,1);
                graphics.setOrtho(ortho);
                loader.load();
            });
            glWindow.setRunner(()->{
                while (glWindow.isRunning()) {
                    refresh();
                    
                    handleInput();
                    
                    synchronized (lock) {
                        while (!windowEventQueue.isEmpty()) {
                            TWindowEvent we = windowEventQueue.pollFirst();
                            if (we instanceof Move) {
                                Move move = (Move) we;
                                glWindow.setPosition(move.x,move.y);
                                window.x = move.x;
                                window.y = move.y;
                            } else if (we instanceof Resize) {
                                Resize resize = (Resize) we;
                                glWindow.setSize(resize.width,resize.height);
                                window.width = resize.width;
                                window.height = resize.height;
                            } else if (we instanceof SetVisible) {
                                SetVisible visible = (SetVisible) we;
                                glWindow.setVisible(visible.visible);
                            } else if (we instanceof SetResizable) {
                                //GLFW does not support toggling window resizing after creation
                            } else if (we instanceof SetDecorated) {
                               // GLFW does not support toggling window decoration after creation
                            }
                        }
                    }
                }
            });
            glWindow.setCloser(()->{
                closer.close();
            });
            glWindow.start();
        });
        sp.run();
    }
    
    private void refresh() {
        gl.glViewport(0,0,window.width,window.height);
        gl.glScissor(0,0,window.width,window.height);
        gl.glClearColor(1,1,1,1);
        gl.glClear(Mask.COLOR_BUFFER_BIT);
        
        //TODO swap this with something for new layout manager
        //This is here because for TFrames (internally undecorated windows) , the GLFW
        //size changed is not called on creation.
        TLayoutManager.invalidateLayout(window.widgetLayout());
        //window.layoutDirtyChildren();
        
        graphics.paint(window);
    }
    
    String title() {
        return title;
    }
    void setTitle(String title) {
        this.title = title;
    }
    
    void setX(int x) {
        window.x = x;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(x, window.y));
            } else {
                isXAligned = false;
            }
        }
    }
    
    void setY(int y) {
        window.y = y;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(window.x, y));
            } else {
                isYAligned = false;
            }
        }
    }
    
    void setWidth(int width) {
        window.width = width;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Resize(width,window.height));
            } else {
                usePolicyWidth = false;
            }
        }
    }
    
    void setHeight(int height) {
        window.height = height;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Resize(window.width,height));
            } else {
                usePolicyHeight = false;
            }
        }
    }
    
    void setVisible(boolean visible) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new SetVisible(visible));
            } else {
                this.visible = visible;
            }
        }
    }
    
    void setResizable(boolean resizable) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new SetResizable(resizable));
            } else {
                this.resizable = resizable;
            }
        }
    }
    
    void setDecorated(boolean decorated) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new SetDecorated(decorated));
            } else {
                this.decorated = decorated;
            }
        }
    }
    
    jonl.jgl.Window window() {
        return glWindow;
    }
    
    GraphicsLibrary getGL() {
        return gl;
    }
    
    Input input() {
        return input;
    }
    
    void setLoader(Loader loader) {
        this.loader = loader;
    }
    
    void setCloser(Closer closer) {
        this.closer = closer;
    }
    
    private void handleInput() {
        int dx = (int) input.getDX();
        int dy = (int) input.getDY();
        int x = (int) input.getX();
        int y = (int) input.getY();
        
        // Mouse button events
        for (int i=Input.MB_LEFT; i<Input.MB_COUNT; i++) {
            if (input.isButtonPressed(i)) {
                TEventManager.fireMouseButtonPressed(window, new TMouseEvent(TEventType.MouseButtonPress, i, x, y, x, y, dx, dy));
            }
            if (input.isButtonReleased(i)) {
                TEventManager.fireMouseButtonReleased(window, new TMouseEvent(TEventType.MouseButtonRelease, i, x, y, x, y, dx, dy));
            }
        }
        
        // Mouse motion events
        if (dx!=0 || dy!=0) {
            int prevX = x - dx;
            int prevY = y - dy;
            boolean inNow = TEventManager.within(window,x,y);
            boolean inBefore = TEventManager.within(window,prevX,prevY);
            if (inNow && !inBefore) {
                TEventManager.fireMouseEnter(window, new TMouseEvent(TEventType.MouseEnter, -1, x, y, x, y, dx, dy));
            }
            if (!inNow && inBefore) {
                TEventManager.fireMouseExit(window, new TMouseEvent(TEventType.MouseExit, -1, x, y, x, y, dx, dy));
            }
            TEventManager.fireMouseMove(window, new TMouseEvent(TEventType.MouseMove, -1, x, y, x, y, dx, dy));
        }
        
        // Mouse scroll wheel events
        int sx = (int) input.getScrollX();
        int sy = (int) input.getScrollY();
        if (sx!=0 || sy!=0) {
            TEventManager.fireScroll(window, new TScrollEvent(TEventType.Scroll, sx, sy, x, y, x, y, dx, dy));
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
                TEventManager.fireKeyPressed(window, new TKeyEvent(TEventType.KeyPress, i, mod));
            }
            if (input.isKeyReleased(i)) {
                TEventManager.fireKeyReleased(window, new TKeyEvent(TEventType.KeyRelease, i, mod));
            }
        }
    }
    
    void setCursor(TCursor cursor) {
        switch (cursor) {
        case ARROW:
            glWindow.setCursor(Window.ARROW_CURSOR);
            break;
        case IBEAM:
            glWindow.setCursor(Window.IBEAM_CURSOR);
            break;
        case CROSSHAIR:
            glWindow.setCursor(Window.CROSSHAIR_CURSOR);
            break;
        case HAND:
            glWindow.setCursor(Window.HAND_CURSOR);
            break;
        case HRESIZE:
            glWindow.setCursor(Window.HRESIZE_CURSOR);
            break;
        case VRESIZE:
            glWindow.setCursor(Window.VRESIZE_CURSOR);
            break;
        }
    }
    
}
