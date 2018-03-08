package jonl.aui.tea;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayDeque;

import jonl.aui.HAlign;
import jonl.aui.VAlign;
import jonl.aui.tea.TWindowEvent.Move;
import jonl.aui.tea.TWindowEvent.Resize;
import jonl.aui.tea.TWindowEvent.SetResizable;
import jonl.aui.tea.TWindowEvent.SetVisible;
import jonl.aui.tea.event.TEventType;
import jonl.aui.tea.event.TMouseEvent;
import jonl.aui.tea.event.TMoveEvent;
import jonl.aui.tea.event.TResizeEvent;
import jonl.jgl.Closer;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.Loader;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.Window.Insets;
import jonl.jgl.lwjgl.GLFWWindow;
import jonl.jutils.parallel.SequentialProcessor;
import jonl.vmath.Matrix4;

class TWindowManager {

    // ------------------------------------------------------------------------
    
    private GLFWWindow glWindow;
    private GraphicsLibrary gl;
    private Input input;
    
    private String title = "Window";
    private boolean visible;
    private boolean resizable;
    
    private boolean isAligned = true;
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
    
    void setPosition(HAlign halign, VAlign valign) {
        synchronized (lock) {
            int x = window.x;
            int y = window.y;
            
            double w = displayWidth;
            double h = displayHeight;
            
            switch (halign) {
            case LEFT:      x = 0;                                 break;
            case CENTER:    x = (int) ((w/2) - (window.width/2));    break;
            case RIGHT:     x = (int) (w - (window.width/2));        break;
            }
            switch(valign) {
            case TOP:       y = 0;                                 break;
            case MIDDLE:    y = (int) ((h/2) - (window.height/2));   break;
            case BOTTOM:    y = (int) (h - (window.height/2));       break;
            }
            
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(x,y));
            } else {
                window.x = x;
                window.y = y;
                isAligned = true;
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
                glWindow = new GLFWWindow(title,window.width,window.height,visible,false,resizable,true,4,false);
                
                Insets insets = glWindow.getInsets();
                if (isAligned) {
                    setPosition(halign,valign);
                } else {
                    glWindow.setPosition(insets.left+window.x,insets.top+window.y);
                }
            }
            gl = glWindow.getGraphicsLibrary();
            input = glWindow.getInput();
            
            
            glWindow.addCursorListener((enter)->{
                if (enter) {
                    
                }
            });
            
            glWindow.addPositionListener((x,y,prevX,prevY)->{
                window.x = x;
                window.y = y;
                TEventHandler.firePositionChanged(window, new TMoveEvent(TEventType.Move,x,y,prevX,prevY));
            });
            
            glWindow.addSizeListener((width,height,prevWidth,prevHeight)->{
                window.width = width;
                window.height = height;
                TEventHandler.fireSizeChanged(window, new TResizeEvent(TEventType.Resize, width,height,prevWidth,prevHeight));
                if (graphics!=null) {
                    ortho = Matrix4.orthographic(0,width,0,height,-1,1);
                    graphics.setOrtho(ortho);
                }
            });
            
            sp.countDown();
            glWindow.setLoader(()->{
                graphics = new TGraphics(gl);
                window.style.init(gl); //TODO remove
                ortho = Matrix4.orthographic(0,glWindow.getWidth(),0,glWindow.getHeight(),-1,1);
                graphics.setOrtho(ortho);
                loader.load();
            });
            glWindow.setRunner(()->{
                while (glWindow.isRunning()) {
                    gl.glViewport(0,0,window.width,window.height);
                    gl.glScissor(0,0,window.width,window.height);
                    gl.glClearColor(1,1,1,1);
                    gl.glClear(Mask.COLOR_BUFFER_BIT);
                    
                    graphics.paint(window);
                    
                    handleInput();
                    
                    synchronized (lock) {
                        while (!windowEventQueue.isEmpty()) {
                            TWindowEvent we = windowEventQueue.pollFirst();
                            if (we instanceof Move) {
                                Move move = (Move) we;
                                glWindow.setPosition(move.x,move.y);
                            } else if (we instanceof Resize) {
                                Resize resize = (Resize) we;
                                glWindow.setSize(resize.width,resize.height);
                            } else if (we instanceof SetVisible) {
                                SetVisible visible = (SetVisible) we;
                                glWindow.setVisible(visible.visible);
                            } else if (we instanceof SetResizable) {
                                //GLFW does not support window resizable after creation
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
    
    String title() {
        return title;
    }
    void setTitle(String title) {
        this.title = title;
    }
    
    void setX(int x) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(x,window.y));
            } else {
                window.x = x;
                isAligned = false;
            }
        }
    }
    
    void setY(int y) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Move(window.x,displayHeight - (y+window.width)));
            } else {
                window.y = y;
                isAligned = false;
            }
        }
    }
    
    void setWidth(int width) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Resize(width,window.height));
            } else {
                window.width = width;
            }
        }
    }
    
    void setHeight(int height) {
        synchronized (lock) {
            if (glWindow!=null) {
                windowEventQueue.addLast(new Resize(window.width,height));
            } else {
                window.height = height;
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
        for (int i=Input.MB_LEFT; i<Input.MB_LEFT+Input.MB_COUNT; i++) {
            if (input.isButtonPressed(i)) {
                TEventHandler.fireMouseButtonPressed(window, new TMouseEvent(TEventType.MouseButtonPress, i, x, y, x, y, dx, dy));
            }
            if (input.isButtonReleased(i)) {
                TEventHandler.fireMouseButtonReleased(window, new TMouseEvent(TEventType.MouseButtonRelease, i, x, y, x, y, dx, dy));
            }
        }
        
        if (dx!=0 || dy!=0) {
            int prevX = x - dx;
            int prevY = y - dy;
            boolean inNow = TEventHandler.within(window,x,y);
            boolean inBefore = TEventHandler.within(window,prevX,prevY);
            if (inNow && !inBefore) {
                TEventHandler.fireMouseEnter(window, new TMouseEvent(TEventType.MouseEnter, -1, x, y, x, y, dx, dy));
            }
            if (!inNow && inBefore) {
                TEventHandler.fireMouseExit(window, new TMouseEvent(TEventType.MouseExit, -1, x, y, x, y, dx, dy));
            }
            TEventHandler.fireMouseMove(window, new TMouseEvent(TEventType.MouseMove, -1, x, y, x, y, dx, dy));
        }
    }
    
}
