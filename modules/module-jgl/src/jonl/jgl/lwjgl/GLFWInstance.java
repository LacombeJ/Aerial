package jonl.jgl.lwjgl;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import jonl.jutils.func.Callback0D;
import jonl.jutils.func.Callback2D;
import jonl.jutils.misc.BufferPool;
import jonl.jutils.parallel.Processor;
import jonl.jutils.parallel.Request;
import jonl.jutils.parallel.RequestQueue;
import jonl.jutils.parallel.Response;
import jonl.jutils.parallel.TimeOut;

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

    private static final ArrayList<Long> CREATED_WINDOWS = new ArrayList<>();
    private static final ArrayList<GLFWWindow> STARTED_WINDOWS = new ArrayList<>();
    
    private static boolean initialized = false;
    private static LWJGL gl = new LWJGL();
    
    private final static DoubleBuffer xBuffer = BufferPool.createDoubleBuffer(1);
    private final static DoubleBuffer yBuffer = BufferPool.createDoubleBuffer(1);
    
    static final HashMap<Integer,Long> STANDARD_CURSORS = new HashMap<>();
    
    
    
    private static final RequestQueue<CreateWindowRequest,CreateWindowResponse>
        CREATE_REQUEST                  = new RequestQueue<>( (request) -> _createWindow(request) );
    
    private static final RequestQueue<StartWindowRequest,StartWindowResponse>
        START_REQUEST                   = new RequestQueue<>( (request) -> _startWindow(request) );
    
    private static final RequestQueue<CallWindowRequest,CallWindowResponse>
        CALL_REQUEST                   = new RequestQueue<>( (request) -> _callWindow(request) );
    
    private static final RequestQueue<WindowRequest,WindowResponse>
        WINDOW_REQUEST  = new RequestQueue<>( (request) -> _window(request) );
    
    private static final RequestQueue<GetWindowFrameSizeRequest,GetWindowFrameSizeResponse>
        GET_WINDOW_FRAME_SIZE_REQUEST   = new RequestQueue<>( (request) -> _getWindowFrameSize(request) );
    
    private static final RequestQueue<SetWindowTitleRequest,SetWindowTitleResponse>
        SET_WINDOW_TITLE_REQUEST        = new RequestQueue<>( (request) -> _setWindowTitle(request) );
    
    private static final RequestQueue<SetWindowPosRequest,SetWindowPosResponse>
        SET_WINDOW_POS_REQUEST          = new RequestQueue<>( (request) -> _setWindowPos(request) );
    
    private static final RequestQueue<SetWindowSizeRequest,SetWindowSizeResponse>
        SET_WINDOW_SIZE_REQUEST         = new RequestQueue<>( (request) -> _setWindowSize(request) );
    
    private static final RequestQueue<SetWindowSizeLimitsRequest,SetWindowSizeLimitsResponse>
        SET_WINDOW_SIZE_LIMITS_REQUEST  = new RequestQueue<>( (request) -> _setWindowSizeLimits(request) );
    
    private static final RequestQueue<GetInputModeRequest,GetInputModeResponse>
        GET_INPUT_MODE_REQUEST          = new RequestQueue<>( (request) -> _getInputMode(request) );
    
    private static final RequestQueue<SetInputModeRequest,SetInputModeResponse>
        SET_INPUT_MODE_REQUEST          = new RequestQueue<>( (request) -> _setInputMode(request) );
    
    private static final RequestQueue<GetCursorPosRequest,GetCursorPosResponse>
        GET_CURSOR_POS_REQUEST          = new RequestQueue<>( (request) -> _getCursorPos(request) );
    
    private static final RequestQueue<SetCursorRequest,SetCursorResponse>
        SET_CURSOR_REQUEST          = new RequestQueue<>( (request) -> _setCursor(request) );
    
    private static final RequestQueue<GetMouseButtonRequest,GetMouseButtonResponse>
        GET_MOUSE_BUTTON_REQUEST        = new RequestQueue<>( (request) -> _getMouseButton(request) );
    
    private static final RequestQueue<GetKeyRequest,GetKeyResponse>
        GET_KEY_REQUEST                 = new RequestQueue<>( (request) -> _getKey(request) );
    
    private static final RequestQueue<SetWindowVisibleRequest,SetWindowVisibleResponse>
        SET_WINDOW_VISIBLE_REQUEST      = new RequestQueue<>( (request) -> _setWindowVisible(request) );
    
    private static final RequestQueue<GetWindowAttribRequest,GetWindowAttribResponse>
        GET_WINDOW_ATTRIB_REQUEST         = new RequestQueue<>( (request) -> _getWindowAttrib(request) );
    
    private static final RequestQueue<?,?>[] REQUEST_QUEUES = {
            CREATE_REQUEST,
            START_REQUEST,
            CALL_REQUEST,
            WINDOW_REQUEST,
            GET_WINDOW_FRAME_SIZE_REQUEST,
            SET_WINDOW_TITLE_REQUEST,
            SET_WINDOW_POS_REQUEST,
            SET_WINDOW_SIZE_REQUEST,
            SET_WINDOW_SIZE_LIMITS_REQUEST,
            GET_INPUT_MODE_REQUEST,
            SET_INPUT_MODE_REQUEST,
            GET_CURSOR_POS_REQUEST,
            SET_CURSOR_REQUEST,
            GET_MOUSE_BUTTON_REQUEST,
            GET_KEY_REQUEST,
            SET_WINDOW_VISIBLE_REQUEST,
            GET_WINDOW_ATTRIB_REQUEST
    };
    
    public enum WindowRequestType {
        MAXIMIZE,
        MINIMIZE,
        RESTORE
    }
    
    static CreateWindowResponse create(CreateWindowRequest request) {
        return CREATE_REQUEST.request(request);
    }
    static StartWindowResponse start(StartWindowRequest request) {
        return START_REQUEST.request(request);
    }
    static CallWindowResponse call(CallWindowRequest request) {
        return CALL_REQUEST.request(request);
    }
    static WindowResponse window(WindowRequest request) {
        return WINDOW_REQUEST.request(request);
    }
    static GetWindowFrameSizeResponse getWindowFrameSize(GetWindowFrameSizeRequest request) {
        return GET_WINDOW_FRAME_SIZE_REQUEST.request(request);
    }
    static SetWindowTitleResponse setWindowTitle(SetWindowTitleRequest request) {
        return SET_WINDOW_TITLE_REQUEST.request(request);
    }
    static SetWindowPosResponse setWindowPos(SetWindowPosRequest request) {
        return SET_WINDOW_POS_REQUEST.request(request);
    }
    static SetWindowSizeResponse setWindowSize(SetWindowSizeRequest request) {
        return SET_WINDOW_SIZE_REQUEST.request(request);
    }
    static SetWindowSizeLimitsResponse setWindowSizeLimits(SetWindowSizeLimitsRequest request) {
        return SET_WINDOW_SIZE_LIMITS_REQUEST.request(request);
    }
    static GetInputModeResponse getInputMode(GetInputModeRequest request) {
        return GET_INPUT_MODE_REQUEST.request(request);
    }
    static SetInputModeResponse setInputMode(SetInputModeRequest request) {
        return SET_INPUT_MODE_REQUEST.request(request);
    }
    static GetCursorPosResponse getCursorPos(GetCursorPosRequest request) {
        return GET_CURSOR_POS_REQUEST.request(request);
    }
    static SetCursorResponse setCursor(SetCursorRequest request) {
        return SET_CURSOR_REQUEST.request(request);
    }
    static GetMouseButtonResponse getMouseButton(GetMouseButtonRequest request) {
        return GET_MOUSE_BUTTON_REQUEST.request(request);
    }
    static GetKeyResponse getKey(GetKeyRequest request) {
        return GET_KEY_REQUEST.request(request);
    }
    static SetWindowVisibleResponse setWindowVisible(SetWindowVisibleRequest request) {
        return SET_WINDOW_VISIBLE_REQUEST.request(request);
    }
    static GetWindowAttribResponse getWindowAttrib(GetWindowAttribRequest request) {
        return GET_WINDOW_ATTRIB_REQUEST.request(request);
    }

    
    static void init() {
        boolean init = false;
        synchronized (GLFWInstance.class) {
            if (!initialized) {
                initialized = true;
                init = true;
            }
        }
        if (init) {
            initializeInstance();
        }
    }
    
    static void createCursors() {
        for (int cursor = GLFW.GLFW_ARROW_CURSOR; cursor < GLFW.GLFW_VRESIZE_CURSOR+1; cursor++) {
            long cursorId = GLFW.glfwCreateStandardCursor(cursor);
            STANDARD_CURSORS.put(cursor, cursorId);
        }
    }
    
    static TimeOut initTimeOut;
    private static void initializeInstance() {
    	
    	// Performing a time out check because program will hang if native libraries aren't found
        // Choosing arbitrary value of 3 seconds to timeout
    	TimeOut timeOut = new TimeOut(3);
    	
        Processor.thread(()->{
        	
            GLFWErrorCallback.createPrint().set();
            
            if (!GLFW.glfwInit()) {
                throw new IllegalStateException("Failed to initialize GLFW.");
            }
            
            timeOut.pass(); // Initialization succeeded and no errors have occurred so far
            
            createCursors();
            
            while (true) {
            	
                if (STARTED_WINDOWS.size()>0) {   
                	
                    GLFW.glfwPollEvents();
                    
                } else if (CREATED_WINDOWS.size()==0) {
                	
                	// If there are no windows and no requests in queue
                    if (!isAnyRequestLeft()) {
                    	
                    	// Program will time out if a request is not received in alloted time
                    	if (initTimeOut == null) {
                    		initTimeOut = new TimeOut(1);
                    	} else {
                    		if (initTimeOut.check()) {
                    			continue; // Keep waiting for a request
                    		} else {
                    			System.out.println("No more requests. Exiting program.");
                    			break; // Break loop and exit program
                    		}
                    	}
                    	
                	// A request was found (maybe a window creation request?)
                    } else {
                    	initTimeOut = null; //reset time out check
                    }
                    
                }
                
                ArrayList<GLFWWindow> remove = new ArrayList<>();
                for (GLFWWindow w : STARTED_WINDOWS) {
                    if (w.isClosing()) {
                        Callbacks.glfwFreeCallbacks(w.getID());
                        GLFW.glfwDestroyWindow(w.getID());
                        remove.add(w);
                    }
                }
                
                for (GLFWWindow w : remove) {
                    STARTED_WINDOWS.remove(w);
                    CREATED_WINDOWS.remove(w.getID());
                }
                
                requestRespond();
            }
            
            GLFW.glfwTerminate();
            GLFW.glfwSetErrorCallback(null).free();
            
            resetInstance();
        });
        
        if (!timeOut.hold()) {
        	System.err.println("Error: Time out reached! Exiting.");
        	System.exit(0); // Stop program if we reach a time out (there is a hanging thread)
        }
        
    }
    
    private static void requestRespond() {
        for (RequestQueue<?,?> q : REQUEST_QUEUES) {
            q.respond();
        }
    }
    
    private static boolean isAnyRequestLeft() {
        for (RequestQueue<?,?> q : REQUEST_QUEUES) {
            if (!q.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    private static void resetInstance() {
        initialized = false;
    }
    
    
    
    
    /* ********************************************************************************* */
    /* ********************************* Create Window ********************************* */
    /* ********************************************************************************* */
    static class CreateWindowRequest extends Request {
        final GLFWWindow window;
        final String title;
        final int width, height;
        final boolean fullscreen, resizable, decorated;
        final int multiSample;
        final Callback2D<Integer,Integer> windowPosCallback;
        final Callback2D<Integer,Integer> windowSizeCallback;
        CreateWindowRequest(GLFWWindow window, String title, int width, int height,
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
    static class CreateWindowResponse extends Response {
        final long id;
        final String title;
        final int x, y, width, height, screenWidth, screenHeight;
        final boolean resizable, decorated, fullscreen;
        final GLFWInput input;
        final LWJGL gl;
        CreateWindowResponse(long id, String title, int x, int y, int width, int height,
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
    private static CreateWindowResponse _createWindow(CreateWindowRequest request) {
        
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
        
        CreateWindowResponse response = new CreateWindowResponse(id, title, x, y, width, height,
                resizable, decorated, fullscreen,
                screenWidth, screenHeight, input, gl);
        
        CREATED_WINDOWS.add(id);
        
        return response;
    }
    
    /* ********************************************************************************* */
    /* ********************************* Start Window ********************************** */
    /* ********************************************************************************* */
    static class StartWindowRequest extends Request {
        final GLFWWindow window;
        StartWindowRequest(GLFWWindow window) {
            this.window = window;
        }
    }
    static class StartWindowResponse extends Response {
        StartWindowResponse() { }
    }
    private static StartWindowResponse _startWindow(StartWindowRequest request) {
        StartWindowResponse response = new StartWindowResponse();
        STARTED_WINDOWS.add(request.window);
        return response;
    }
    
    /* ********************************************************************************* */
    /* ********************************** Call Window ********************************** */
    /* ********************************************************************************* */
    static class CallWindowRequest extends Request {
        final long id;
        final Callback0D call;
        CallWindowRequest(long id, Callback0D call) {
            this.id = id;
            this.call = call;
        }
    }
    static class CallWindowResponse extends Response {
        CallWindowResponse() { }
    }
    private static CallWindowResponse _callWindow(CallWindowRequest request) {
        CallWindowResponse response = new CallWindowResponse();
        request.call.f();
        return response;
    }
    
    /* ********************************************************************************* */
    /* ******************************** Window Requests ******************************** */
    /* ********************************************************************************* */
    static class WindowRequest extends Request {
        final long id;
        final WindowRequestType type;
        WindowRequest(long id, WindowRequestType type) {
            this.id = id;
            this.type = type;
        }
    }
    static class WindowResponse extends Response {
        WindowResponse() {
            
        }
    }
    private static WindowResponse _window(WindowRequest request) {
        switch (request.type) {
        case MAXIMIZE:
            GLFW.glfwMaximizeWindow(request.id);
            break;
        case MINIMIZE:
            GLFW.glfwIconifyWindow(request.id);
            break;
        case RESTORE:
            GLFW.glfwRestoreWindow(request.id);
            break;
        }
        return new WindowResponse();
    }
    
    /* ********************************************************************************* */
    /* **************************** Get Window Frame Size ****************************** */
    /* ********************************************************************************* */
    static class GetWindowFrameSizeRequest extends Request {
        final long id;
        GetWindowFrameSizeRequest(long id) {
            this.id = id;
        }
    }
    static class GetWindowFrameSizeResponse extends Response {
        final int left;
        final int top;
        final int right;
        final int bottom;
        GetWindowFrameSizeResponse(int left, int top, int right, int bottom) {
            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }
    }
    private static GetWindowFrameSizeResponse _getWindowFrameSize(GetWindowFrameSizeRequest request) {
        int[] left = new int[1];
        int[] top = new int[1];
        int[] right = new int[1];
        int[] bottom = new int[1];
        GLFW.glfwGetWindowFrameSize(request.id,left,top,right,bottom);
        GetWindowFrameSizeResponse response = new GetWindowFrameSizeResponse(left[0],top[0],right[0],bottom[0]);
        return response;
    }
    
    /* ********************************************************************************* */
    /* ******************************* Set Window Title ******************************** */
    /* ********************************************************************************* */
    static class SetWindowTitleRequest extends Request {
        final long id;
        final String title;
        SetWindowTitleRequest(long id, String title) {
            this.id = id;
            this.title = title;
        }
    }
    static class SetWindowTitleResponse extends Response {
        SetWindowTitleResponse() {
            
        }
    }
    private static SetWindowTitleResponse _setWindowTitle(SetWindowTitleRequest request) {
        GLFW.glfwSetWindowTitle(request.id,request.title);
        return new SetWindowTitleResponse();
    }
    
    /* ********************************************************************************* */
    /* ********************************* Set Window Pos ******************************** */
    /* ********************************************************************************* */
    static class SetWindowPosRequest extends Request {
        final long id;
        final int x;
        final int y;
        SetWindowPosRequest(long id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }
    static class SetWindowPosResponse extends Response {
        SetWindowPosResponse() {
            
        }
    }
    private static SetWindowPosResponse _setWindowPos(SetWindowPosRequest request) {
        GLFW.glfwSetWindowPos(request.id,request.x,request.y);
        return new SetWindowPosResponse();
    }
    
    /* ********************************************************************************* */
    /* ******************************** Set Window Size ******************************** */
    /* ********************************************************************************* */
    static class SetWindowSizeRequest extends Request {
        final long id;
        final int width;
        final int height;
        SetWindowSizeRequest(long id, int width, int height) {
            this.id = id;
            this.width = width;
            this.height = height;
        }
    }
    static class SetWindowSizeResponse extends Response {
        SetWindowSizeResponse() {
            
        }
    }
    private static SetWindowSizeResponse _setWindowSize(SetWindowSizeRequest request) {
        GLFW.glfwSetWindowSize(request.id,request.width,request.height);
        return new SetWindowSizeResponse();
    }
    
    /* ********************************************************************************* */
    /* **************************** Set Window Size Limits ***************************** */
    /* ********************************************************************************* */
    static class SetWindowSizeLimitsRequest extends Request {
        final long id;
        final int minWidth;
        final int minHeight;
        final int maxWidth;
        final int maxHeight;
        /** Use values of GLFW_DONT_CARE (-1) to ignore limits */
        SetWindowSizeLimitsRequest(long id, int minWidth, int minHeight, int maxWidth, int maxHeight) {
            this.id = id;
            this.minWidth = minWidth;
            this.minHeight = minHeight;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }
    }
    static class SetWindowSizeLimitsResponse extends Response {
        SetWindowSizeLimitsResponse() {
            
        }
    }
    private static SetWindowSizeLimitsResponse _setWindowSizeLimits(SetWindowSizeLimitsRequest request) {
        GLFW.glfwSetWindowSizeLimits(request.id,request.minWidth,request.minHeight,request.maxWidth,request.maxHeight);
        return new SetWindowSizeLimitsResponse();
    }
    
    /* ********************************************************************************* */
    /* ****************************** Get Window Attrib ******************************** */
    /* ********************************************************************************* */
    static class GetWindowAttribRequest extends Request {
        final long id;
        final int attrib;
        GetWindowAttribRequest(long id, int attrib) {
            this.id = id;
            this.attrib = attrib;
        }
    }
    static class GetWindowAttribResponse extends Response {
        final boolean value;
        GetWindowAttribResponse(boolean value) {
            this.value = value;
        }
    }
    private static GetWindowAttribResponse _getWindowAttrib(GetWindowAttribRequest request) {
        int value = GLFW.glfwGetWindowAttrib(request.id, request.attrib);
        boolean ret = (value == 1) ? true : false;
        return new GetWindowAttribResponse(ret);
    }
    
    /* ********************************************************************************* */
    /* ******************************** Get Input Mode  ******************************** */
    /* ********************************************************************************* */
    static class GetInputModeRequest extends Request {
        final long id;
        final int mode;
        GetInputModeRequest(long id, int mode) {
            this.id = id;
            this.mode = mode;
        }
    }
    static class GetInputModeResponse extends Response {
        final int value;
        GetInputModeResponse(int value) {
            this.value = value;
        }
    }
    private static GetInputModeResponse _getInputMode(GetInputModeRequest request) {
        int value = GLFW.glfwGetInputMode(request.id,request.mode);
        return new GetInputModeResponse(value);
    }
    
    /* ********************************************************************************* */
    /* ******************************** Set Input Mode  ******************************** */
    /* ********************************************************************************* */
    static class SetInputModeRequest extends Request {
        final long id;
        final int mode;
        final int value;
        SetInputModeRequest(long id, int mode, int value) {
            this.id = id;
            this.mode = mode;
            this.value = value;
        }
    }
    static class SetInputModeResponse extends Response {
        SetInputModeResponse() {
            
        }
    }
    private static SetInputModeResponse _setInputMode(SetInputModeRequest request) {
        GLFW.glfwSetInputMode(request.id,request.mode,request.value);
        return new SetInputModeResponse();
    }
    
    /* ********************************************************************************* */
    /* ******************************** Get Cursor Pos ********************************* */
    /* ********************************************************************************* */
    static class GetCursorPosRequest extends Request {
        final long id;
        GetCursorPosRequest(long id) {
            this.id = id;
        }
    }
    static class GetCursorPosResponse extends Response {
        final double x;
        final double y;
        GetCursorPosResponse(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    private static GetCursorPosResponse _getCursorPos(GetCursorPosRequest request) {
        GLFW.glfwGetCursorPos(request.id,xBuffer,yBuffer);
        double x = (double) xBuffer.get(0);
        double y = (double) yBuffer.get(0);
        return new GetCursorPosResponse(x,y);
    }
    
    /* ********************************************************************************* */
    /* ******************************** Get Cursor Pos ********************************* */
    /* ********************************************************************************* */
    static class SetCursorRequest extends Request {
        final long id;
        final long cursorId;
        SetCursorRequest(long id, long cursorId) {
            this.id = id;
            this.cursorId = cursorId;
        }
    }
    static class SetCursorResponse extends Response {
        SetCursorResponse() {
            
        }
    }
    private static SetCursorResponse _setCursor(SetCursorRequest request) {
        GLFW.glfwSetCursor(request.id,request.cursorId);
        return new SetCursorResponse();
    }
    
    /* ********************************************************************************* */
    /* ******************************* Get Mouse Button ******************************** */
    /* ********************************************************************************* */
    static class GetMouseButtonRequest extends Request {
        final long id;
        final int[] buttons;
        GetMouseButtonRequest(long id, int[] buttons) {
            this.id = id;
            this.buttons = buttons;
        }
    }
    static class GetMouseButtonResponse extends Response {
        final int[] actions;
        GetMouseButtonResponse(int[] actions) {
            this.actions = actions;
        }
    }
    private static GetMouseButtonResponse _getMouseButton(GetMouseButtonRequest request) {
        int[] actions = new int[request.buttons.length];
        for (int i=0; i<actions.length; i++) {
            int action = GLFW.glfwGetMouseButton(request.id,request.buttons[i]);
            actions[i] = action;
        }
        return new GetMouseButtonResponse(actions);
    }
    
    /* ********************************************************************************* */
    /* ************************************ Get Key ************************************ */
    /* ********************************************************************************* */
    static class GetKeyRequest extends Request {
        final long id;
        final int[] keys;
        GetKeyRequest(long id, int[] buttons) {
            this.id = id;
            this.keys = buttons;
        }
    }
    static class GetKeyResponse extends Response {
        final int[] actions;
        GetKeyResponse(int[] actions) {
            this.actions = actions;
        }
    }
    private static GetKeyResponse _getKey(GetKeyRequest request) {
        int[] actions = new int[request.keys.length];
        for (int i=0; i<actions.length; i++) {
            int action = GLFW.glfwGetKey(request.id,request.keys[i]);
            actions[i] = action;
        }
        return new GetKeyResponse(actions);
    }
    
    /* ********************************************************************************* */
    /* ******************************** Set Window Visible ***************************** */
    /* ********************************************************************************* */
    static class SetWindowVisibleRequest extends Request {
        final long id;
        final boolean visible;
        SetWindowVisibleRequest(long id, boolean visible) {
            this.id = id;
            this.visible = visible;
        }
    }
    static class SetWindowVisibleResponse extends Response {
        SetWindowVisibleResponse() {
            
        }
    }
    private static SetWindowVisibleResponse _setWindowVisible(SetWindowVisibleRequest request) {
        if (request.visible) {
            GLFW.glfwShowWindow(request.id);
        } else {
            GLFW.glfwHideWindow(request.id);
        }
        return new SetWindowVisibleResponse();
    }
    

    
}
