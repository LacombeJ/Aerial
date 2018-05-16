package jonl.aui.tea;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TKeyEvent;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TScrollEvent;
import jonl.aui.tea.spatial.TPoint;
import jonl.jgl.Closer;
import jonl.jgl.GL;
import jonl.jgl.Input;
import jonl.jgl.Loader;
import jonl.jgl.Window;
import jonl.jgl.Window.Insets;
import jonl.jgl.lwjgl.GLFWWindow;
import jonl.jutils.parallel.SequentialProcessor;
import jonl.vmath.Mathi;
import jonl.vmath.Matrix4;

class TWindowManager {

    // ------------------------------------------------------------------------
    
    private GLFWWindow glWindow;
    private GL gl;
    private TInput input;
    
    private BufferedImage icon;
    
    private String title = "Window";
    private boolean visible;
    private boolean resizable;
    private boolean decorated = true;
    private boolean floating = false;
    
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
    
    private ArrayDeque<Loader> loaders = new ArrayDeque<>();
    private ArrayDeque<Closer> closers = new ArrayDeque<>();
    
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
                windowEventQueue.addLast(new TWindowEvent.Move(p.x,p.y));
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
                
                int prefWidth = Math.max(minWidth, sizeHint.width);
                int prefHeight = Math.max(minHeight, sizeHint.height);
                
                window.width = Mathi.max(minWidth, window.width, prefWidth);
                window.height = Mathi.max(minHeight, window.height, prefHeight);
                
                if (usePolicyWidth) {
                    window.width = Mathi.max(sizeHint.width, window.width, 1);
                }
                if (usePolicyHeight) {
                    window.height = Mathi.max(sizeHint.height, window.height, 1);
                }
                
                glWindow = new GLFWWindow(title,window.width,window.height,visible,false,resizable,decorated,floating,4,Window.WINDOW,false);
                
                // Using Integer.MAX_VALUE for glfw max size limits doesn't work. Using GLFW_DONT_CARE=-1 instead
                glWindow.setSizeLimits(prefWidth, prefHeight, -1, -1);
                
                TPoint aligned = getPositionAlignment(halign, valign);
                if (isXAligned) {
                    window.x = aligned.x;
                }
                if (isYAligned) {
                    window.y = aligned.y;
                }
                
                window.x = Mathi.max(0, window.x);
                window.y = Mathi.max(0, window.y);
                
                // We include insets here because the position of the gl window doesn't include the GLFW window frame isnets
                Insets insets = glWindow.getInsets();
                glWindow.setPosition(insets.left+window.x,insets.top+window.y);
                
                if (icon!=null) {
                    glWindow.setIcon(icon);
                }
                
            }
            gl = glWindow.getGraphicsLibrary();
            input = new TInput(glWindow.getInput(),()->window.height);
            
            
            glWindow.addCursorListener((enter)->{
                if (enter) {
                    
                }
            });
            
            glWindow.addPositionListener((x,y,prevX,prevY)->{
                TLayoutManager.setPosition(window, x, y);
            });
            
            glWindow.addSizeListener((width,height,prevWidth,prevHeight)->{
                TLayoutManager.setSize(window, width, height);
                if (graphics!=null) {
                    ortho = Matrix4.orthographic(0,width,height,0,-1,1);
                    graphics.setOrtho(ortho);
                }
            });
            
            // Counts down and frees the thread that created this thread
            sp.countDown();
            
            glWindow.setLoader(()->{
                graphics = new TGraphics(gl,()->window.height);
                graphics.beginGL();
                ortho = Matrix4.orthographic(0,glWindow.getWidth(),glWindow.getHeight(),0,-1,1);
                graphics.setOrtho(ortho);
                loader.load();
            });
            glWindow.setRunner(()->{
                while (glWindow.isRunning()) {
                    
                    while (!loaders.isEmpty()) {
                        Loader loader = loaders.pollFirst();
                        loader.load();
                    }
                    
                    refresh();
                    
                    handleInput();
                    
                    synchronized (lock) {
                        while (!windowEventQueue.isEmpty()) {
                            TWindowEvent we = windowEventQueue.pollFirst();
                            if (we instanceof TWindowEvent.Move) {
                                TWindowEvent.Move move = (TWindowEvent.Move) we;
                                glWindow.setPosition(move.x,move.y);
                                window.x = move.x;
                                window.y = move.y;
                            } else if (we instanceof TWindowEvent.Resize) {
                                TWindowEvent.Resize resize = (TWindowEvent.Resize) we;
                                glWindow.setSize(resize.width,resize.height);
                                window.width = resize.width;
                                window.height = resize.height;
                            } else if (we instanceof TWindowEvent.SetVisible) {
                                TWindowEvent.SetVisible visible = (TWindowEvent.SetVisible) we;
                                glWindow.setVisible(visible.visible);
                            } else if (we instanceof TWindowEvent.SetResizable) {
                                //GLFW does not support toggling window resizing after creation
                            } else if (we instanceof TWindowEvent.SetDecorated) {
                               // GLFW does not support toggling window decoration after creation
                            } else if (we instanceof TWindowEvent.SetSizeLimits) {
                                TWindowEvent.SetSizeLimits sl = (TWindowEvent.SetSizeLimits) we;
                                glWindow.setSizeLimits(sl.minWidth,sl.minHeight,sl.maxWidth,sl.maxheight);
                             }
                        }
                    }
                }
            });
            glWindow.setCloser(()->{
                closer.close();
                while (!closers.isEmpty()) {
                    Closer closer = closers.pollFirst();
                    closer.close();
                }
                
            });
            glWindow.start();
        });
        sp.run();
    }
    
    private void refresh() {
        gl.glViewport(0,0,window.width,window.height);
        gl.glScissor(0,0,window.width,window.height);
        gl.glClearColor(1,1,1,1);
        gl.glClear(GL.COLOR_BUFFER_BIT);
        
        //TODO swap this with something for new layout manager
        //This is here because for TFrames (internally undecorated windows) , the GLFW
        //size changed is not called on creation.
        window.invalidateLayout();
        
        window.manager().update().update(window);
        
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
                windowEventQueue.addLast(new TWindowEvent.Move(x, window.y));
            } else {
                isXAligned = false;
            }
        }
    }
    
    void setY(int y) {
        window.y = y;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.Move(window.x, y));
            } else {
                isYAligned = false;
            }
        }
    }
    
    void setWidth(int width) {
        window.width = width;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.Resize(width,window.height));
            } else {
                usePolicyWidth = false;
            }
        }
    }
    
    void setHeight(int height) {
        window.height = height;
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.Resize(window.width,height));
            } else {
                usePolicyHeight = false;
            }
        }
    }
    
    void setVisible(boolean visible) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.SetVisible(visible));
            } else {
                this.visible = visible;
            }
        }
    }
    
    void setResizable(boolean resizable) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.SetResizable(resizable));
            } else {
                this.resizable = resizable;
            }
        }
    }
    
    void setDecorated(boolean decorated) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.SetDecorated(decorated));
            } else {
                this.decorated = decorated;
            }
        }
    }
    
    void setFloating(boolean floating) {
        this.floating = floating;
    }
    
    void setIcon(BufferedImage icon) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.SetIcon(icon));
            } else {
                this.icon = icon;
            }
        }
    }
    
    void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new TWindowEvent.SetSizeLimits(minWidth,minHeight,maxWidth,maxHeight));
            }
        }
    }
    
    jonl.jgl.Window window() {
        return glWindow;
    }
    
    GL getGL() {
        return gl;
    }
    
    Input input() {
        return input;
    }
    
    void addLoader(Loader loader) {
        synchronized (lock) {
            if (glWindow==null) {
                Loader prev = this.loader;
                this.loader = () -> {
                    prev.load();
                    loader.load();
                };
            } else {
                loaders.add(loader);
            }
        }
    }
    
    void addCloser(Closer closer) {
        synchronized (lock) {
            if (glWindow==null) {
                Closer prev = this.closer;
                this.closer = () -> {
                    prev.close();
                    closer.close();
                };
            } else {
                closers.add(closer);
            }
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
                window.manager().event().fireMouseButtonPressed(window, new TMouseEvent(TEventType.MouseButtonPress, i, x, y, x, y, dx, dy));
            }
            if (input.isButtonReleased(i)) {
                window.manager().event().fireMouseButtonReleased(window, new TMouseEvent(TEventType.MouseButtonRelease, i, x, y, x, y, dx, dy));
            }
        }
        
        // Mouse motion events
        if (dx!=0 || dy!=0) {
            int prevX = x - dx;
            int prevY = y - dy;
            boolean inNow = TManagerEvent.within(window,x,y);
            boolean inBefore = TManagerEvent.within(window,prevX,prevY);
            if (inNow && !inBefore) {
                window.manager().event().fireMouseEnter(window, new TMouseEvent(TEventType.MouseEnter, -1, x, y, x, y, dx, dy));
            }
            if (!inNow && inBefore) {
                window.manager().event().fireMouseExit(window, new TMouseEvent(TEventType.MouseExit, -1, x, y, x, y, dx, dy));
            }
            window.manager().event().fireMouseMove(window, new TMouseEvent(TEventType.MouseMove, -1, x, y, x, y, dx, dy));
        }
        
        // Mouse scroll wheel events
        int sx = (int) input.getScrollX();
        int sy = (int) input.getScrollY();
        if (sx!=0 || sy!=0) {
            window.manager().event().fireScroll(window, new TScrollEvent(TEventType.Scroll, sx, sy, x, y, x, y, dx, dy));
        }
        
        // Key events
        for (int i=Input.K_0; i<Input.K_COUNT; i++) {
            
            int mod = TKeyEvent.NO_MOD;
            boolean shift = input.isKeyDown(Input.K_LSHIFT) || input.isKeyDown(Input.K_RSHIFT);
            boolean control = input.isKeyDown(Input.K_LCONTROL) || input.isKeyDown(Input.K_RCONTROL);
            boolean alt = input.isKeyDown(Input.K_LALT) || input.isKeyDown(Input.K_RALT);
            
            if (shift) {
                mod |= TKeyEvent.SHIFT_MOD;
            }
            if (control) {
                mod |= TKeyEvent.CTRL_MOD;
            }
            if (alt) {
                mod |= TKeyEvent.ALT_MOD;
            }
            
            char c = input.getChar(i, shift);
            boolean valid = (c!=0 && !control && !alt);
            
            if (input.isKeyPressed(i)) {
                window.manager().event().fireKeyPressed(window, new TKeyEvent(TEventType.KeyPress, i, mod, c, valid));
            }
            if (input.isKeyReleased(i)) {
                window.manager().event().fireKeyReleased(window, new TKeyEvent(TEventType.KeyRelease, i, mod, c, valid));
            }
            if (input.isKeyRepeated(i)) {
                window.manager().event().fireKeyRepeat(window, new TKeyEvent(TEventType.KeyRepeat, i, mod, c, valid));
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
    
    static abstract class TWindowEvent {
        
        public static class Move extends TWindowEvent {
            int x, y;
            Move(int x, int y) {
                this.x = x;
                this.y = y;
            }
        }
        
        static class Resize extends TWindowEvent {
            int width, height;
            Resize(int width, int height) {
                this.width = width;
                this.height = height;
            }
        }
        
        static class SetVisible extends TWindowEvent {
            boolean visible;
            SetVisible(boolean visible) {
                this.visible = visible;
            }
        }
        
        static class SetResizable extends TWindowEvent {
            boolean resizable;
            SetResizable(boolean resizable) {
                this.resizable = resizable;
            }
        }
        
        static class SetDecorated extends TWindowEvent {
            boolean decorated;
            SetDecorated(boolean decorated) {
                this.decorated = decorated;
            }
        }
        
        static class SetIcon extends TWindowEvent {
            BufferedImage icon;
            SetIcon(BufferedImage icon) {
                this.icon = icon;
            }
        }
        
        static class SetSizeLimits extends TWindowEvent {
            int minWidth, minHeight, maxWidth, maxheight;
            SetSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
                this.minWidth = minWidth;
                this.minHeight = minHeight;
                this.maxWidth = maxWidth;
                this.maxheight = maxHeight;
            }
        }
        
    }
    
}
