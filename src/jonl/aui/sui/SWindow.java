package jonl.aui.sui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayDeque;

import jonl.jutils.io.Console;
import jonl.jutils.parallel.SequentialProcessor;
import jonl.aui.HAlign;
import jonl.aui.Layout;
import jonl.aui.MouseButtonEvent;
import jonl.aui.MouseMotionEvent;
import jonl.aui.SingleSlot;
import jonl.aui.VAlign;
import jonl.aui.Widget;
import jonl.aui.sui.WindowEvent.Move;
import jonl.aui.sui.WindowEvent.Resize;
import jonl.aui.sui.WindowEvent.SetResizable;
import jonl.aui.sui.WindowEvent.SetVisible;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.Loader;
import jonl.jgl.Window;
import jonl.jgl.GraphicsLibrary.Mask;
import jonl.jgl.Window.Insets;
import jonl.jgl.lwjgl.GLFWWindow;
import jonl.vmath.Matrix4;

/**
 * Window running on a separate thread
 * 
 * @author Jonathan Lacombe
 *
 */
public class SWindow extends AbstractSingleSlot implements jonl.aui.Window {

    Widget widget;
    
    String title = "Window";
    boolean visible;
    boolean resizable;
    
    Window window;
    GraphicsLibrary gl;
    Input input;
    SGraphics g;
    
    Layout<SingleSlot> layout = new FillLayout();
    
    ArrayDeque<WindowEvent> windowEventQueue = new ArrayDeque<>();
    
    Object lock = new Object();
    
    Matrix4 ortho;
    
    Loader loader;
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setPosition(HAlign halign, VAlign valign) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double w = screenSize.getWidth();
        double h = screenSize.getHeight();
        
        switch (halign) {
        case LEFT:      setXWithNoRequest( 0 );                                 break;
        case CENTER:    setXWithNoRequest( (int) ((w/2) - (getWidth()/2)) );    break;
        case RIGHT:     setXWithNoRequest( (int) (w - (getWidth()/2)) );        break;
        }
        switch(valign) {
        case TOP:       setYWithNoRequest( 0 );                                 break;
        case MIDDLE:    setYWithNoRequest( (int) ((h/2) - (getHeight()/2)) );   break;
        case BOTTOM:    setYWithNoRequest( (int) (h - (getHeight()/2)) );       break;
        }
    }
    
    @Override
    public void create() {
        SequentialProcessor sp = new SequentialProcessor();
        sp.setLockCount(1);
        sp.add(()->{
            window = new GLFWWindow(title,getWidth(),getHeight(),false,resizable,true,4,false);
            gl = window.getGraphicsLibrary();
            input = window.getInput();
            
            Insets insets = window.getInsets();
            window.setPosition(insets.left+getX(),insets.top+getY());
            
            window.addPositionListener((x,y,prevX,prevY)->{
                setXWithNoRequest(x);
                setYWithNoRequest(y);
                this.firePositionChanged(x,y,prevX,prevY);
            });
            
            window.addSizeListener((width,height,prevWidth,prevHeight)->{
                setWidthWithNoRequest(width);
                setHeightWithNoRequest(height);
                this.fireSizeChanged(width,height,prevWidth,prevHeight);
                if (g!=null) {
                    ortho = Matrix4.orthographic(0,width,0,height,-1,1);
                    g.setOrtho(ortho);
                }
            });
            
            sp.countDown();
            window.setLoader(()->{
                g = new SGraphics(gl);
                ortho = Matrix4.orthographic(0,window.getWidth(),0,window.getHeight(),-1,1);
                g.setOrtho(ortho);
                if (loader!=null) {
                    loader.load();
                }
            });
            window.setRunner(()->{
                while (window.isRunning()) {
                    gl.glViewport(0,0,getWidth(),getHeight());
                    gl.glScissor(0,0,getWidth(),getHeight());
                    gl.glClearColor(1,1,1,1);
                    gl.glClear(Mask.COLOR_BUFFER_BIT);
                    
                    g.paint(this);
                    
                    handleInput(window);
                    synchronized (lock) {
                        while (!windowEventQueue.isEmpty()) {
                            WindowEvent we = windowEventQueue.pollFirst();
                            if (we instanceof WindowEvent.Move) {
                                WindowEvent.Move move = (Move) we;
                                window.setPosition(move.x,move.y);
                            } else if (we instanceof WindowEvent.Resize) {
                                WindowEvent.Resize resize = (Resize) we;
                                window.setSize(resize.width,resize.height);
                            } else if (we instanceof WindowEvent.SetVisible) {
                                WindowEvent.SetVisible visible = (SetVisible) we;
                                window.setVisible(visible.visible);
                            } else if (we instanceof WindowEvent.SetResizable) {
                                //GLFW does not support window resizable after creation
                            }
                        }
                    }
                }
            });
            window.setCloser(()->{
                
            });
            window.start();
        });
        sp.run();
    }
    
    
    @Override
    public void setX(int x) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new Move(x,getY()));
            }
        } else {
            setXWithNoRequest(x);
        }
    }
    
    @Override
    public void setY(int y) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new Move(getX(),window.getScreenHeight() - (y+window.getWidth())));
            }
        } else {
            setYWithNoRequest(y);
        }
    }
    
    @Override
    public void setWidth(int width) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new Resize(width,getHeight()));
            }
        } else {
            setWidthWithNoRequest(width);
        }
    }
    
    @Override
    public void setHeight(int height) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new Resize(getWidth(),height));
            }
        } else {
            setHeightWithNoRequest(height);
        }
    }
    
    @Override
    public void setVisible(boolean visible) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new SetResizable(visible));
            }
        } else {
            this.visible = visible;
        }
    }
    
    @Override
    public void setResizable(boolean resizable) {
        if (window!=null) {
            synchronized (lock) {
                windowEventQueue.addLast(new SetResizable(resizable));
            }
        } else {
            this.resizable = resizable;
        }
    }
    
    @Override
    public void layout() {
        layout.layout(this);
    }
    
    private void handleInput(Window window) {
        if (inputOpen) {
            Input input = window.getInput();
            int prevX = (int) (input.getX() - input.getDX());
            int prevY = (int) (input.getY() - input.getDY());
            int x = (int) input.getX();
            int y = (int) input.getY();
            for (int i=Input.MB_LEFT; i<Input.MB_LEFT+Input.MB_COUNT; i++) {
                if (input.isButtonDown(i)) {
                    this.fireMouseDown(new MouseButtonEvent(MouseButtonEvent.DOWN,i,x,y));
                    this.fireGlobalMouseDown(new MouseButtonEvent(MouseButtonEvent.DOWN,i,x,y));
                }
                if (input.isButtonPressed(i)) {
                    this.fireMousePressed(new MouseButtonEvent(MouseButtonEvent.PRESSED,i,x,y));
                    this.fireGlobalMousePressed(new MouseButtonEvent(MouseButtonEvent.PRESSED,i,x,y));
                }
                if (input.isButtonReleased(i)) {
                    this.fireMouseReleased(new MouseButtonEvent(MouseButtonEvent.RELEASED,i,x,y));
                    this.fireGlobalMouseReleased(new MouseButtonEvent(MouseButtonEvent.RELEASED,i,x,y));
                }
            }
            boolean inNow = isWithin(x,y);
            boolean inBefore = isWithin(prevX,prevY);
            if (inNow && !inBefore) {
                this.fireMouseEnter(new MouseMotionEvent(MouseMotionEvent.ENTER,x,y,prevX,prevY));
            }
            if (!inNow && inBefore) {
                this.fireMouseExit(new MouseMotionEvent(MouseMotionEvent.EXIT,x,y,prevX,prevY));
            }
            if (inNow) {
                this.fireMouseHover(new MouseMotionEvent(MouseMotionEvent.HOVER,x,y,prevX,prevY));
            }
            if (x!=prevX || y!=prevY) {
                if (inNow && inBefore) {
                    this.fireMouseMoved(new MouseMotionEvent(MouseMotionEvent.MOVED,x,y,prevX,prevY));
                }
                this.fireGlobalMousePositionChanged(x,y,prevX,prevY);
            }
        }
    }
    
    public Window getWindow() {
        return window;
    }
    
    public GraphicsLibrary getGraphicsLibrary() {
        return gl;
    }
    
    public Input getInput() {
        return input;
    }

    public void setLoader(Loader loader) {
        this.loader = loader;
    }
    
    boolean inputOpen = true;
    
    public void closeInput() {
        inputOpen = false;
    }
    
    public void openInput() {
        inputOpen = true;
    }


    
    
}
