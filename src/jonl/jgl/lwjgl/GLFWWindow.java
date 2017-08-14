package jonl.jgl.lwjgl;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import jonl.jgl.Closer;
import jonl.jgl.GraphicsLibrary;
import jonl.jgl.Input;
import jonl.jgl.Loader;
import jonl.jgl.Runner;
import jonl.jgl.Window;
import jonl.jgl.lwjgl.GLFWInstance.GetInputModeRequest;
import jonl.jgl.lwjgl.GLFWInstance.GetInputModeResponse;
import jonl.jgl.lwjgl.GLFWInstance.GetWindowFrameSizeRequest;
import jonl.jgl.lwjgl.GLFWInstance.GetWindowFrameSizeResponse;
import jonl.jgl.lwjgl.GLFWInstance.SetInputModeRequest;
import jonl.jgl.lwjgl.GLFWInstance.SetWindowPosRequest;
import jonl.jgl.lwjgl.GLFWInstance.SetWindowSizeRequest;
import jonl.jgl.lwjgl.GLFWInstance.SetWindowTitleRequest;
import jonl.jgl.lwjgl.GLFWInstance.SetWindowVisibleRequest;
import jonl.jgl.lwjgl.GLFWInstance.CreateWindowRequest;
import jonl.jgl.lwjgl.GLFWInstance.CreateWindowResponse;
import jonl.jgl.lwjgl.GLFWInstance.StartWindowRequest;
import jonl.jutils.func.Callback;
import jonl.jutils.func.Callback2D;
import jonl.jutils.structs.BijectiveMap;
import jonl.jgl.Input.CursorState;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
public final class GLFWWindow implements Window {

    private final long id;
    
    private final boolean fullscreen;
    private final boolean resizable;
    private final boolean decorated;
    private boolean visible;
    private boolean vsyncEnabled;
    
    private String title;
    
    private int x;
    private int y;
    private int width;
    private int height;
    
    private final int screenWidth;
    private final int screenHeight;
    
    private final GLFWInput input;
    private final LWJGL gl;
    
    private Loader loader;
    private Runner runner;
    private Closer closer;
    
    private final ArrayList<Int2ChangedListener> sizeListeners;
    private final ArrayList<Int2ChangedListener> positionListeners;
    private final ArrayList<Callback<Boolean>>   cursorListeners;
    
    public GLFWWindow(String title, int width, int height, boolean fullscreen,
            boolean resizable, boolean decorated, int multiSample, boolean vsyncEnabled) {
        
        GLFWInstance.init();
        
        this.visible = false;
        this.vsyncEnabled = vsyncEnabled;
        
        sizeListeners = new ArrayList<>();
        positionListeners = new ArrayList<>();
        cursorListeners = new ArrayList<>();
        
        Callback2D<Integer,Integer> windowPosCallback = (x,y) -> {
            int prevX = this.x;
            int prevY = getY();
            this.x = x;
            this.y = y;
            for (Int2ChangedListener pl : positionListeners) {
                pl.valueChanged(x,getY(),prevX,prevY);
            }
        };
        
        Callback2D<Integer,Integer> windowSizeCallback = (w,h) -> {
            int prevWidth = this.width;
            int prevHeight = this.height;
            int prevY = getY();
            this.width = w;
            this.height = h;
            int newY = getY();
            for (Int2ChangedListener sl : sizeListeners) {
                sl.valueChanged(w,h,prevWidth,prevHeight);
            }
            if (newY != prevY) {
                for (Int2ChangedListener pl : positionListeners) {
                    pl.valueChanged(x,newY,x,prevY);
                }
            }
        };
        
        CreateWindowResponse response = GLFWInstance.create(
                new CreateWindowRequest(this, title, width, height,
                        fullscreen, resizable, decorated, multiSample,
                        windowPosCallback, windowSizeCallback));
        
        this.id = response.id;
        this.title = response.title;
        this.x = response.x;
        this.y = response.y;
        this.width = response.width;
        this.height = response.height;
        this.resizable = response.resizable;
        this.decorated = response.decorated;
        this.fullscreen = response.fullscreen;
        this.screenWidth = response.screenWidth;
        this.screenHeight = response.screenHeight;
        this.input = response.input;
        this.gl = response.gl;
        
        setLoader(()->{
           //empty by default
        });
        setRunner(()->{
            while (isRunning()) { //keeps window open
                //empty by default
            }
        });
        setCloser(()->{
            //empty by default
        });
    }
    
    public GLFWWindow(String title, int width, int height, boolean resizable) {
        this(title,width,height,false,resizable,true,4,true);
    }
    
    public GLFWWindow(String title, int width, int height) {
        this(title,width,height,false,false,true,4,true);
    }
    
    public GLFWWindow(int width, int height) {
        this("GLFW Window",width,height,false,false,true,4,true);
    }
    
    public GLFWWindow(String title) {
        this(title,1024,576,false,false,true,4,true);
    }
    
    public GLFWWindow() {
        this("GLFW Window");
    }
    
    @Override
    public void setLoader(Loader loader) {
        if (loader!=null)
            this.loader = loader;
    }
    
    @Override
    public void setRunner(Runner runner) {
        if (runner!=null)
            this.runner = runner;
    }
    
    @Override
    public void setCloser(Closer closer) {
        if (closer!=null)
            this.closer = closer;
    }
    
    @Override
    public void start() {
        GLFWInstance.start(new StartWindowRequest(this));
        makeContext();
        loader.load();
        showWindow();
        runner.run();
        closer.close();
        setClosing();
    }
    
    /**
     * Makes this window the current context for OpenGL
     */
    private void makeContext() {
        GLFW.glfwMakeContextCurrent(id);
        GL.createCapabilities();
        if (vsyncEnabled) {
            GLFW.glfwSwapInterval(1);
        }
    }
    
    private void showWindow() {
        if (visible) {
            SetWindowVisibleRequest request = new SetWindowVisibleRequest(id,true);
            GLFWInstance.setWindowVisible(request);
        }
    }
    
    @Override
    public void close() {
        GLFW.glfwSetWindowShouldClose(id,true);
    }
    
    private void swapBuffers() {
        GLFW.glfwSwapBuffers(id);
    }
    
    private void pollInput() {
        input.reset();
        input.update();
    }
    
    @Override
    public boolean isRunning() {
        if (!GLFW.glfwWindowShouldClose(id)) {
            swapBuffers();
            pollInput();
            return true;
        }
        return false;
    }
    
    long getID() {
        return id;
    }
    
    @Override
    public boolean isFullscreen() {
        return fullscreen;
    }
    
    @Override
    public boolean isResizable() {
        return resizable;
    }
    
    @Override
    public boolean isDecorated() {
        return decorated;
    }
    
    @Override
    /** @return true if window should be visible after start */
    public boolean isVisible() {
        return visible;
    }
    
    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public void setTitle(String title) {
        SetWindowTitleRequest request = new SetWindowTitleRequest(id,this.title=title);
        GLFWInstance.setWindowTitle(request);
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return screenHeight - (y + height);
    }
    
    @Override
    public void setPosition(int x, int y) {
        SetWindowPosRequest request = new SetWindowPosRequest(id,x,y);
        GLFWInstance.setWindowPos(request);
    }
    
    @Override
    public int getWidth() {
        return width;
    }
    
    @Override
    public int getHeight() {
        return height;
    }
    
    @Override
    public void setSize(int width, int height) {
        SetWindowSizeRequest request = new SetWindowSizeRequest(id,this.width=width,this.height=height);
        GLFWInstance.setWindowSize(request);
    }

    @Override
    public void setVisible(boolean visible) {
        SetWindowVisibleRequest request = new SetWindowVisibleRequest(id,this.visible=visible);
        GLFWInstance.setWindowVisible(request);
    }
    
    private final static BijectiveMap<CursorState,Integer> STATE_MAP = new BijectiveMap<>();
    
    static {
        STATE_MAP.put(CursorState.NORMAL, GLFW.GLFW_CURSOR_NORMAL);
        STATE_MAP.put(CursorState.HIDDEN, GLFW.GLFW_CURSOR_HIDDEN);
        STATE_MAP.put(CursorState.GRABBED, GLFW.GLFW_CURSOR_DISABLED);
    }
    
    @Override
    public CursorState getCursorState() {
        GetInputModeRequest request = new GetInputModeRequest(id,GLFW.GLFW_CURSOR);
        GetInputModeResponse response = GLFWInstance.getInputMode(request);
        return STATE_MAP.getKey(response.value);
    }
    
    @Override
    public void setCursorState(CursorState state) {
        if (getCursorState()==CursorState.GRABBED && state!=CursorState.GRABBED) {
            ((GLFWInput) getInput()).setOverride(true);
        }
        SetInputModeRequest request = new SetInputModeRequest(id,GLFW.GLFW_CURSOR,STATE_MAP.getValue(state));
        GLFWInstance.setInputMode(request);
    }
    
    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public GraphicsLibrary getGraphicsLibrary() {
        return gl;
    }

    @Override
    public void addSizeListener(Int2ChangedListener sl) {
        sizeListeners.add(sl);
    }

    @Override
    public void removeSizeListener(Int2ChangedListener sl) {
        sizeListeners.remove(sl);
    }

    @Override
    public Insets getInsets() {
        GetWindowFrameSizeRequest request = new GetWindowFrameSizeRequest(id);
        GetWindowFrameSizeResponse response = GLFWInstance.getWindowFrameSize(request);
        return new Insets(response.top,response.bottom,response.left,response.right);
    }

    @Override
    public void addPositionListener(Int2ChangedListener pl) {
        positionListeners.add(pl);
    }

    @Override
    public void removePositionListener(Int2ChangedListener pl) {
        positionListeners.remove(pl);
    }
    
    @Override
    public void addCursorListener(Callback<Boolean> cl) {
        cursorListeners.add(cl);
    }
    
    @Override
    public void removeCursorListener(Callback<Boolean> cl) {
        cursorListeners.remove(cl);
    }

    @Override
    public int getScreenWidth() {
        return screenWidth;
    }

    @Override
    public int getScreenHeight() {
        return screenHeight;
    }

    boolean isClosing = false;
    boolean isClosing() {
        return isClosing;
    }
    void setClosing() {
        isClosing = true;
    }
    
}
