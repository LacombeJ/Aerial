package jonl.jgl.lwjgl;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import java.util.ArrayList;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import jonl.jutils.func.Callback2D;
import jonl.jutils.parallel.Processor;
import jonl.jutils.parallel.Request;
import jonl.jutils.parallel.RequestQueue;
import jonl.jutils.parallel.Response;

/**
 * This class is a utility class used to hold static data and functions
 * used by every GLFW window
 * <p>
 * For example each GLFWWindow needs glfwInit() called before window
 * creation but glfwInit() only needs to be called one. This class will
 * have a function that calls glfwInit() if it hasn't been called by
 * another window.
 * @author Jonathan Lacombe
 *
 */
class GLFWInstance {

    private static final RequestQueue<WindowCreationRequest,WindowCreationResponse> CREATE_REQUEST = new RequestQueue<>(
            (request) -> respond(request)
    );
    private static final RequestQueue<WindowRequest,WindowResponse> START_REQUEST = new RequestQueue<>(
            (request) -> respondStart(request)
    );
    private static final RequestQueue<WindowIDRequest,WindowIDResponse> SHOW_REQUEST = new RequestQueue<>(
            (request) -> respondShow(request)
    );
    private static final ArrayList<GLFWWindow> WINDOWS = new ArrayList<>();
    
    private static boolean initialized = false;
    private static boolean windowStarted = false;
    private static LWJGL gl = new LWJGL();
    
    static void init() {
        if (requestInit()) {
            initializeInstance();
        }
    }
    
    static WindowCreationResponse create(WindowCreationRequest request) {
        return CREATE_REQUEST.request(request);
    }
    
    static WindowResponse start(WindowRequest request) {
        return START_REQUEST.request(request);
    }
    
    static WindowIDResponse show(WindowIDRequest request) {
        return SHOW_REQUEST.request(request);
    }
    
    private synchronized static boolean requestInit() {
        if (!initialized) {
            initialized = true;
            return true;
        }
        return false;
    }
    
    private static void initializeInstance() {
        Processor.thread(()->{
            
            GLFWErrorCallback.createPrint().set();
            
            if (!GLFW.glfwInit()) {
                throw new IllegalStateException("Failed to initialize GLFW.");
            }
            
            while (true) {
                if (WINDOWS.size()>0) {
                    for (GLFWWindow w : WINDOWS) {
                        ((GLFWInput)w.getInput()).reset();
                    }
                    GLFW.glfwPollEvents();
                    for (GLFWWindow w : WINDOWS) {
                        ((GLFWInput)w.getInput()).update();
                    }
                } else if (windowStarted) {
                    break;
                }
                ArrayList<GLFWWindow> remove = new ArrayList<>();
                for (GLFWWindow w : WINDOWS) {
                    if (w.isClosing()) {
                        glfwFreeCallbacks(w.getID());
                        glfwDestroyWindow(w.getID());
                        remove.add(w);
                    }
                }
                for (GLFWWindow w : remove) {
                    WINDOWS.remove(w);
                }
                
                CREATE_REQUEST.respond();
                START_REQUEST.respond();
                SHOW_REQUEST.respond();
            }
            
            GLFW.glfwTerminate();
            GLFW.glfwSetErrorCallback(null).free();
        });
    }
    
    private static WindowCreationResponse respond(WindowCreationRequest request) {
        
        final long id;
        final String title;
        final int x, y, width, height, screenWidth, screenHeight;
        final boolean resizable, decorated, fullscreen;
        final GLFWInput input;
        final LWJGL gl;
        
        //Window will show after loading
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, request.multiSample);
        
        if (!request.fullscreen) {
            GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,(resizable=request.resizable) ?
                    GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
            GLFW.glfwWindowHint(GLFW.GLFW_DECORATED,(decorated=request.decorated) ?
                    GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        } else {
            resizable = false;
            decorated = false;
        }
        
        id = GLFW.glfwCreateWindow(width=request.width,height=request.height,title=request.title,
                (fullscreen=request.fullscreen) ? GLFW.glfwGetPrimaryMonitor() : MemoryUtil.NULL,
                MemoryUtil.NULL);
        
        if (id == MemoryUtil.NULL) {
            throw new IllegalStateException("GLFW window creation failed.");
        }
        
        //Centers window
        GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        screenWidth = mode.width();
        screenHeight = mode.height();
        x = (screenWidth - request.width) / 2;
        y = (screenHeight - request.height) / 2;
        GLFW.glfwSetWindowPos(id,x,y);
        
        GLFW.glfwSetWindowPosCallback(id, (windowID,wx,wy)->{
            request.windowPosCallback.f(wx, wy);
        });
        
        GLFW.glfwSetFramebufferSizeCallback(id, (windowID,w,h)->{
            request.windowSizeCallback.f(w, h);
        });
        
        input = new GLFWInput(id,()->request.window.getHeight());
        gl = GLFWInstance.gl;
        
        WindowCreationResponse response = new WindowCreationResponse(id, title, x, y, width, height,
                resizable, decorated, fullscreen,
                screenWidth, screenHeight, input, gl);
        
        return response;
    }
    
    static class WindowCreationRequest extends Request {
        final GLFWWindow window;
        final String title;
        final int width, height;
        final boolean fullscreen, resizable, decorated;
        final int multiSample;
        final Callback2D<Integer,Integer> windowPosCallback;
        final Callback2D<Integer,Integer> windowSizeCallback;
        WindowCreationRequest(GLFWWindow window, String title, int width, int height,
                boolean fullscreen, boolean resizable, boolean decorated, int multiSample,
                Callback2D<Integer,Integer> windowPosCallback, Callback2D<Integer,Integer> windowSizeCallback) {
            this.window = window;
            this.title = title;
            this.width = width;
            this.height = height;
            this.fullscreen = fullscreen;
            this.resizable = resizable;
            this.decorated = decorated;
            this.multiSample = multiSample;
            this.windowPosCallback = windowPosCallback;
            this.windowSizeCallback = windowSizeCallback;
        }
    }
    
    static class WindowCreationResponse extends Response {
        final long id;
        final String title;
        final int x, y, width, height, screenWidth, screenHeight;
        final boolean resizable, decorated, fullscreen;
        final GLFWInput input;
        final LWJGL gl;
        WindowCreationResponse(long id, String title, int x, int y, int width, int height,
                boolean resizable, boolean decorated, boolean fullscreen,
                int screenWidth, int screenHeight, GLFWInput input, LWJGL gl) {
            this.id = id;
            this.title = title;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.resizable = resizable;
            this.decorated = decorated;
            this.fullscreen = fullscreen;
            this.screenWidth = screenWidth;
            this.screenHeight = screenHeight;
            this.input = input;
            this.gl = gl;
        }
    }
    
    
    private static WindowResponse respondStart(WindowRequest request) {
        WindowResponse response = new WindowResponse();
        windowStarted = true;
        WINDOWS.add(request.window);
        return response;
    }
    static class WindowRequest extends Request {
        final GLFWWindow window;
        WindowRequest(GLFWWindow window) {
            this.window = window;
        }
    }
    static class WindowResponse extends Response {
        WindowResponse() { }
    }
    
    private static WindowIDResponse respondShow(WindowIDRequest request) {
        WindowIDResponse response = new WindowIDResponse();
        GLFW.glfwShowWindow(request.windowID);
        return response;
    }
    static class WindowIDRequest extends Request {
        final long windowID;
        WindowIDRequest(long windowID) {
            this.windowID = windowID;
        }
    }
    static class WindowIDResponse extends Response {
        WindowIDResponse() { }
    }
    

    static class WindowFrameSizeRequest extends Request {
        final long id;
        final int[] left;
        final int[] top;
        final int[] right;
        final int[] bottom;
        WindowFrameSizeRequest(long id, int[] left, int[] top, int[] right, int[] bottom) {
            this.id =id;
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
    static class WindowFrameSizeResponse extends Response {
        WindowFrameSizeResponse() {
            
        }
    }
    
    
}
