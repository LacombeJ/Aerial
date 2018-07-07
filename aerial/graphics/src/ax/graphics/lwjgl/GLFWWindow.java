package ax.graphics.lwjgl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import ax.commons.func.Callback;
import ax.commons.func.Callback0D;
import ax.commons.func.Callback2D;
import ax.commons.structs.BijectiveMap;
import ax.graphics.Closer;
import ax.graphics.GL;
import ax.graphics.Input;
import ax.graphics.Loader;
import ax.graphics.Runner;
import ax.graphics.Window;
import ax.graphics.Input.CursorState;
import ax.graphics.lwjgl.GLFWInstance.CallWindowRequest;
import ax.graphics.lwjgl.GLFWInstance.CreateWindowRequest;
import ax.graphics.lwjgl.GLFWInstance.CreateWindowResponse;
import ax.graphics.lwjgl.GLFWInstance.GetInputModeRequest;
import ax.graphics.lwjgl.GLFWInstance.GetInputModeResponse;
import ax.graphics.lwjgl.GLFWInstance.GetWindowAttribRequest;
import ax.graphics.lwjgl.GLFWInstance.GetWindowAttribResponse;
import ax.graphics.lwjgl.GLFWInstance.GetWindowFrameSizeRequest;
import ax.graphics.lwjgl.GLFWInstance.GetWindowFrameSizeResponse;
import ax.graphics.lwjgl.GLFWInstance.SetCursorRequest;
import ax.graphics.lwjgl.GLFWInstance.SetIconRequest;
import ax.graphics.lwjgl.GLFWInstance.SetInputModeRequest;
import ax.graphics.lwjgl.GLFWInstance.SetWindowPosRequest;
import ax.graphics.lwjgl.GLFWInstance.SetWindowSizeLimitsRequest;
import ax.graphics.lwjgl.GLFWInstance.SetWindowSizeRequest;
import ax.graphics.lwjgl.GLFWInstance.SetWindowTitleRequest;
import ax.graphics.lwjgl.GLFWInstance.SetWindowVisibleRequest;
import ax.graphics.lwjgl.GLFWInstance.StartWindowRequest;
import ax.graphics.lwjgl.GLFWInstance.WindowRequest;
import ax.graphics.lwjgl.GLFWInstance.WindowRequestType;

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
    
    /**
     * Constructs a GLFWWindow
     * 
     * @param visible window becomes visible when {@link #start()} is called
     */
    public GLFWWindow(String title, int width, int height, boolean visible, boolean fullscreen,
            boolean resizable, boolean decorated, boolean floating, int multiSample, int resolutionType, boolean vsyncEnabled) {
        
        GLFWInstance.init();
        
        this.visible = visible;
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
                        fullscreen, resizable, decorated, floating, multiSample,
                        resolutionType, windowPosCallback, windowSizeCallback));
        
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
    
    public GLFWWindow(String title, int width, int height, boolean visible, boolean resizable, boolean floating) {
        this(title,width,height,visible,false,resizable,true,floating,4,Window.WINDOW,true);
    }
    
    public GLFWWindow(String title, int width, int height, boolean visible, boolean resizable) {
        this(title,width,height,visible,false,resizable,true,false,4,Window.WINDOW,true);
    }
    
    public GLFWWindow(String title, int width, int height) {
        this(title,width,height,true,false);
    }
    
    public GLFWWindow(int width, int height) {
        this("GLFW Window",width,height);
    }
    
    public GLFWWindow(String title) {
        this(title,1024,576);
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
        org.lwjgl.opengl.GL.createCapabilities();
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
    
    @Override
    public void call(Callback0D call) {
        CallWindowRequest request = new CallWindowRequest(id,call);
        GLFWInstance.call(request);
    }
    
    private void swapBuffers() {
        GLFW.glfwSwapBuffers(id);
    }
    
    private void pollInput() {
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
        return y;
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
    public void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        SetWindowSizeLimitsRequest request = new SetWindowSizeLimitsRequest(id,minWidth,minHeight,maxWidth,maxHeight);
        GLFWInstance.setWindowSizeLimits(request);
    }
    
    @Override
    public void maximize() {
        WindowRequest request = new WindowRequest(id, WindowRequestType.MAXIMIZE);
        GLFWInstance.window(request);
    }
    
    @Override
    public void minimize() {
        WindowRequest request = new WindowRequest(id, WindowRequestType.MINIMIZE);
        GLFWInstance.window(request);
    }
    
    @Override
    public void restore() {
        WindowRequest request = new WindowRequest(id, WindowRequestType.RESTORE);
        GLFWInstance.window(request);
    }
    
    @Override
    public void setIcon(BufferedImage image) {
        byte[] pixels = new byte[image.getHeight()*image.getWidth()*4];
        int c = 0;
        for (int j=0; j<image.getHeight(); j++) {
            for (int i=0; i<image.getWidth(); i++) {
                int rgba = image.getRGB(i,j);
                pixels[c*4+0] = (byte) ((rgba >> 16) & 255);
                pixels[c*4+1] = (byte) ((rgba >> 8) & 255);
                pixels[c*4+2] = (byte) ((rgba) & 255);
                pixels[c*4+3] = (byte) ((rgba >> 24)&255);
                c++;
            }
        }
        SetIconRequest request = new SetIconRequest(id, image.getWidth(), image.getHeight(), pixels);
        GLFWInstance.setIcon(request);
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
    
    private final static BijectiveMap<Integer,Integer> CURSOR_MAP = new BijectiveMap<>();
    
    static {
        CURSOR_MAP.put(ARROW_CURSOR, GLFW.GLFW_ARROW_CURSOR);
        CURSOR_MAP.put(IBEAM_CURSOR, GLFW.GLFW_IBEAM_CURSOR);
        CURSOR_MAP.put(CROSSHAIR_CURSOR, GLFW.GLFW_CROSSHAIR_CURSOR);
        CURSOR_MAP.put(HAND_CURSOR, GLFW.GLFW_HAND_CURSOR);
        CURSOR_MAP.put(HRESIZE_CURSOR, GLFW.GLFW_HRESIZE_CURSOR);
        CURSOR_MAP.put(VRESIZE_CURSOR, GLFW.GLFW_VRESIZE_CURSOR);
    }
    
    @Override
    public void setCursor(int cursorId) {
        int cursor = CURSOR_MAP.get(cursorId);
        long standardCursor = GLFWInstance.STANDARD_CURSORS.get(cursor);
        SetCursorRequest request = new SetCursorRequest(id, standardCursor);
        GLFWInstance.setCursor(request);
    }
    
    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public GL getGL() {
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
    
    private final static BijectiveMap<Integer,Integer> ATTRIBUTE_MAP = new BijectiveMap<>();
    
    static {
        ATTRIBUTE_MAP.put(Window.FOCUSED, GLFW.GLFW_FOCUSED);
        ATTRIBUTE_MAP.put(Window.ICONIFIED, GLFW.GLFW_ICONIFIED);
        ATTRIBUTE_MAP.put(Window.MAXIMIZED, GLFW.GLFW_MAXIMIZED);
        ATTRIBUTE_MAP.put(Window.VISIBLE, GLFW.GLFW_VISIBLE);
        ATTRIBUTE_MAP.put(Window.RESIZABLE, GLFW.GLFW_RESIZABLE);
        ATTRIBUTE_MAP.put(Window.DECORATED, GLFW.GLFW_DECORATED);
        ATTRIBUTE_MAP.put(Window.FLOATING, GLFW.GLFW_FLOATING);
        ATTRIBUTE_MAP.put(Window.HOVERED, GLFW.GLFW_HOVERED);
    }
    
    @Override
    public boolean getAttribute(int attribute) {
        GetWindowAttribRequest request = new GetWindowAttribRequest(id, ATTRIBUTE_MAP.get(attribute));
        GetWindowAttribResponse response = GLFWInstance.getWindowAttrib(request);
        return response.value;
    }

    boolean isClosing = false;
    boolean isClosing() {
        return isClosing;
    }
    void setClosing() {
        isClosing = true;
    }
    
}
