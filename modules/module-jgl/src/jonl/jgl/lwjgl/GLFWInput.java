package jonl.jgl.lwjgl;

import java.util.ArrayDeque;

import org.lwjgl.glfw.GLFW;

import jonl.jgl.AbstractInput;
import jonl.jgl.lwjgl.GLFWInstance.GetCursorPosRequest;
import jonl.jgl.lwjgl.GLFWInstance.GetCursorPosResponse;
import jonl.jgl.lwjgl.GLFWInstance.GetKeyRequest;
import jonl.jgl.lwjgl.GLFWInstance.GetKeyResponse;
import jonl.jgl.lwjgl.GLFWInstance.GetMouseButtonRequest;
import jonl.jgl.lwjgl.GLFWInstance.GetMouseButtonResponse;
import jonl.jutils.func.Function0D;
import jonl.jutils.structs.BijectiveMap;

/**
 * 
 * @author Jonathan Lacombe
 *
 */
class GLFWInput extends AbstractInput {

    final long windowID;
    final Function0D<Integer> windowHeight;
    
    private static final BijectiveMap<Integer,Integer> BUTTON_MAP = new BijectiveMap<>(MB_COUNT);
    private static final BijectiveMap<Integer,Integer> KEY_MAP = new BijectiveMap<>(K_COUNT);
    private static final int[] BUTTON_ARRAY = new int[MB_COUNT];
    private static final int[] KEY_ARRAY = new int[K_COUNT];
    
    private final boolean[] buttonDown          = new boolean[MB_COUNT];
    private final boolean[] buttonPressed       = new boolean[MB_COUNT];
    private final boolean[] buttonReleased      = new boolean[MB_COUNT];
    
    private final boolean[] keyDown             = new boolean[K_COUNT];
    private final boolean[] keyPressed          = new boolean[K_COUNT];
    private final boolean[] keyReleased         = new boolean[K_COUNT];
    private final boolean[] keyRepeated         = new boolean[K_COUNT];
    
    // Using a queue because set key callback happens on a different thread
    private Object keySync = new Object();
    private final ArrayDeque<Integer> keyRepeatQueue = new ArrayDeque<>();
    
    private float x;
    private float y;
    private float dx;
    private float dy;
    private float scrollX;
    private float scrollY;
    
    //Accumulating because scroll callback access happens on a different thread
    private Object scrollSync = new Object();
    private float saccumx;
    private float saccumy;
    /**
     * Used to get correct dx and dy values following start of window or
     * following change in cursor state (grabbed to normal and vice-versa)
     */
    private boolean override = true;
    
    /**
     * This function must only be called from the main thread -> [GLFW]
     */
    GLFWInput(long windowID, Function0D<Integer> windowHeight) {
        this.windowID = windowID;
        this.windowHeight = windowHeight;
        
        GLFW.glfwSetScrollCallback(windowID,(windowid,xoffset,yoffset)->{
            synchronized(scrollSync) {
                saccumx += (float) xoffset;
                saccumy += (float) yoffset;
            }
            
        });
        
        // GLFW_REPEAT only works with key callback
        GLFW.glfwSetKeyCallback(windowID, (windowid,key,scancode,action,mods)->{
            synchronized(keySync) {
                if (action==GLFW.GLFW_REPEAT) {
                    keyRepeatQueue.addLast(KEY_MAP.getKey(key));
                }
            }
        });
        
    }

    void setOverride(boolean o) {
        override = o;
    }
    
    void update() {
        updatePosition();
        updateButtons();
        updateKeys();
    }
    
    private void updatePosition() {
        GetCursorPosRequest request = new GetCursorPosRequest(windowID);
        GetCursorPosResponse response = GLFWInstance.getCursorPos(request);
        
        int nx = (int) Math.floor(response.x);
        int ny = windowHeight.f() - (int) Math.floor(response.y);
        
        if (override) {
            override = false;
        } else {
            dx = nx - x;
            dy = ny - y;
        }
        
        synchronized(scrollSync) {
            scrollX = saccumx;
            scrollY = saccumy;
            saccumx = 0;
            saccumy = 0;
        }
        
        x = nx;
        y = ny;
    }
    
    private void updateGLFWAction(int i, int action, boolean[] pressed, boolean[] released, boolean[] down) {
        pressed[i] = false;
        released[i] = false;
        
        switch (action) {
        case GLFW.GLFW_PRESS:
            if (!down[i]) {
                pressed[i] = true;
            }
            down[i] = true;
            break;
        case GLFW.GLFW_RELEASE:
            if (down[i]) {
                released[i] = true;
            }
            down[i] = false;
            break;
        }
    }
    
    private void updateButtons() {
        GetMouseButtonRequest request = new GetMouseButtonRequest(windowID, BUTTON_ARRAY);
        GetMouseButtonResponse response = GLFWInstance.getMouseButton(request);
        for (int i=0; i<MB_COUNT; i++) {
            int action = response.actions[i];
            updateGLFWAction(i,action,buttonPressed,buttonReleased,buttonDown);
        }
    }
    
    private void updateKeys() {
        GetKeyRequest request = new GetKeyRequest(windowID, KEY_ARRAY);
        GetKeyResponse response = GLFWInstance.getKey(request);
        for (int i=0; i<K_COUNT; i++) {
            
            int action = response.actions[i];
            updateGLFWAction(i,action,keyPressed,keyReleased,keyDown);
        }
        synchronized(keySync) {
            Integer poll = keyRepeatQueue.pollFirst();
            int key = (poll==null) ? K_NONE : poll;
            for (int i=0; i<K_COUNT; i++) {
                keyRepeated[i] = i==key;
            }
        }
    }
    
    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getDX() {
        return dx;
    }

    @Override
    public float getDY() {
        return dy;
    }

    @Override
    public float getScrollX() {
        return scrollX;
    }

    @Override
    public float getScrollY() {
        return scrollY;
    }

    @Override
    public boolean isButtonDown(int key) {
        return buttonDown[key];
    }

    @Override
    public boolean isButtonPressed(int key) {
        return buttonPressed[key];
    }

    @Override
    public boolean isButtonReleased(int key) {
        return buttonReleased[key];
    }
    
    @Override
    public boolean isKeyDown(int key) {
        return keyDown[key];
    }

    @Override
    public boolean isKeyPressed(int key) {
        return keyPressed[key];
    }

    @Override
    public boolean isKeyReleased(int key) {
        return keyReleased[key];
    }
    
    @Override
    public boolean isKeyRepeated(int key) {
        return keyRepeated[key];
    }
    
    static {
        BUTTON_MAP.put(MB_LEFT, GLFW.GLFW_MOUSE_BUTTON_LEFT);
        BUTTON_MAP.put(MB_RIGHT, GLFW.GLFW_MOUSE_BUTTON_RIGHT);
        BUTTON_MAP.put(MB_MIDDLE, GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
        
        for (int i=0; i<MB_COUNT; i++) {
            BUTTON_ARRAY[i] = BUTTON_MAP.getValue(i);
        }
    }

    static {
        KEY_MAP.put(K_0, GLFW.GLFW_KEY_0);
        KEY_MAP.put(K_1, GLFW.GLFW_KEY_1);
        KEY_MAP.put(K_2, GLFW.GLFW_KEY_2);
        KEY_MAP.put(K_3, GLFW.GLFW_KEY_3);
        KEY_MAP.put(K_4, GLFW.GLFW_KEY_4);
        KEY_MAP.put(K_5, GLFW.GLFW_KEY_5);
        KEY_MAP.put(K_6, GLFW.GLFW_KEY_6);
        KEY_MAP.put(K_7, GLFW.GLFW_KEY_7);
        KEY_MAP.put(K_8, GLFW.GLFW_KEY_8);
        KEY_MAP.put(K_9, GLFW.GLFW_KEY_9);
        
        KEY_MAP.put(K_A, GLFW.GLFW_KEY_A);
        KEY_MAP.put(K_B, GLFW.GLFW_KEY_B);
        KEY_MAP.put(K_C, GLFW.GLFW_KEY_C);
        KEY_MAP.put(K_D, GLFW.GLFW_KEY_D);
        KEY_MAP.put(K_E, GLFW.GLFW_KEY_E);
        KEY_MAP.put(K_F, GLFW.GLFW_KEY_F);
        KEY_MAP.put(K_G, GLFW.GLFW_KEY_G);
        KEY_MAP.put(K_H, GLFW.GLFW_KEY_H);
        KEY_MAP.put(K_I, GLFW.GLFW_KEY_I);
        KEY_MAP.put(K_J, GLFW.GLFW_KEY_J);
        KEY_MAP.put(K_K, GLFW.GLFW_KEY_K);
        KEY_MAP.put(K_L, GLFW.GLFW_KEY_L);
        KEY_MAP.put(K_M, GLFW.GLFW_KEY_M);
        KEY_MAP.put(K_N, GLFW.GLFW_KEY_N);
        KEY_MAP.put(K_O, GLFW.GLFW_KEY_O);
        KEY_MAP.put(K_P, GLFW.GLFW_KEY_P);
        KEY_MAP.put(K_Q, GLFW.GLFW_KEY_Q);
        KEY_MAP.put(K_R, GLFW.GLFW_KEY_R);
        KEY_MAP.put(K_S, GLFW.GLFW_KEY_S);
        KEY_MAP.put(K_T, GLFW.GLFW_KEY_T);
        KEY_MAP.put(K_U, GLFW.GLFW_KEY_U);
        KEY_MAP.put(K_V, GLFW.GLFW_KEY_V);
        KEY_MAP.put(K_W, GLFW.GLFW_KEY_W);
        KEY_MAP.put(K_X, GLFW.GLFW_KEY_X);
        KEY_MAP.put(K_Y, GLFW.GLFW_KEY_Y);
        KEY_MAP.put(K_Z, GLFW.GLFW_KEY_Z);
        
        KEY_MAP.put(K_F1, GLFW.GLFW_KEY_F1);
        KEY_MAP.put(K_F2, GLFW.GLFW_KEY_F2);
        KEY_MAP.put(K_F3, GLFW.GLFW_KEY_F3);
        KEY_MAP.put(K_F4, GLFW.GLFW_KEY_F4);
        KEY_MAP.put(K_F5, GLFW.GLFW_KEY_F5);
        KEY_MAP.put(K_F6, GLFW.GLFW_KEY_F6);
        KEY_MAP.put(K_F7, GLFW.GLFW_KEY_F7);
        KEY_MAP.put(K_F8, GLFW.GLFW_KEY_F8);
        KEY_MAP.put(K_F9, GLFW.GLFW_KEY_F9);
        KEY_MAP.put(K_F10, GLFW.GLFW_KEY_F10);
        KEY_MAP.put(K_F11, GLFW.GLFW_KEY_F11);
        KEY_MAP.put(K_F12, GLFW.GLFW_KEY_F12);
        KEY_MAP.put(K_F13, GLFW.GLFW_KEY_F13);
        KEY_MAP.put(K_F14, GLFW.GLFW_KEY_F14);
        KEY_MAP.put(K_F15, GLFW.GLFW_KEY_F15);
        KEY_MAP.put(K_F16, GLFW.GLFW_KEY_F16);
        KEY_MAP.put(K_F17, GLFW.GLFW_KEY_F17);
        KEY_MAP.put(K_F18, GLFW.GLFW_KEY_F18);
        KEY_MAP.put(K_F19, GLFW.GLFW_KEY_F19);
        
        KEY_MAP.put(K_NP_0, GLFW.GLFW_KEY_KP_0);
        KEY_MAP.put(K_NP_1, GLFW.GLFW_KEY_KP_1);
        KEY_MAP.put(K_NP_2, GLFW.GLFW_KEY_KP_2);
        KEY_MAP.put(K_NP_3, GLFW.GLFW_KEY_KP_3);
        KEY_MAP.put(K_NP_4, GLFW.GLFW_KEY_KP_4);
        KEY_MAP.put(K_NP_5, GLFW.GLFW_KEY_KP_5);
        KEY_MAP.put(K_NP_6, GLFW.GLFW_KEY_KP_6);
        KEY_MAP.put(K_NP_7, GLFW.GLFW_KEY_KP_7);
        KEY_MAP.put(K_NP_8, GLFW.GLFW_KEY_KP_8);
        KEY_MAP.put(K_NP_9, GLFW.GLFW_KEY_KP_9);
        KEY_MAP.put(K_NP_ADD, GLFW.GLFW_KEY_KP_ADD);
        KEY_MAP.put(K_NP_DECIMAL, GLFW.GLFW_KEY_KP_DECIMAL);
        KEY_MAP.put(K_NP_DIVIDE, GLFW.GLFW_KEY_KP_DIVIDE);
        KEY_MAP.put(K_NP_ENTER, GLFW.GLFW_KEY_KP_ENTER);
        KEY_MAP.put(K_NP_EQUAL, GLFW.GLFW_KEY_KP_EQUAL);
        KEY_MAP.put(K_NP_MULTIPLY, GLFW.GLFW_KEY_KP_MULTIPLY);
        KEY_MAP.put(K_NP_SUBTRACT, GLFW.GLFW_KEY_KP_SUBTRACT);
        
        KEY_MAP.put(K_LEFT, GLFW.GLFW_KEY_LEFT);
        KEY_MAP.put(K_LALT, GLFW.GLFW_KEY_LEFT_ALT);
        KEY_MAP.put(K_LBRACKET, GLFW.GLFW_KEY_LEFT_BRACKET);
        KEY_MAP.put(K_LCONTROL, GLFW.GLFW_KEY_LEFT_CONTROL);
        KEY_MAP.put(K_LSHIFT, GLFW.GLFW_KEY_LEFT_SHIFT);
        KEY_MAP.put(K_LSUPER, GLFW.GLFW_KEY_LEFT_SUPER);
        
        KEY_MAP.put(K_RIGHT, GLFW.GLFW_KEY_RIGHT);
        KEY_MAP.put(K_RALT, GLFW.GLFW_KEY_RIGHT_ALT);
        KEY_MAP.put(K_RBRACKET, GLFW.GLFW_KEY_RIGHT_BRACKET);
        KEY_MAP.put(K_RCONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL);
        KEY_MAP.put(K_RSHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT);
        KEY_MAP.put(K_RSUPER, GLFW.GLFW_KEY_RIGHT_SUPER);
        
        KEY_MAP.put(K_APOSTROPHE, GLFW.GLFW_KEY_APOSTROPHE);
        KEY_MAP.put(K_BACKSLASH, GLFW.GLFW_KEY_BACKSLASH);
        KEY_MAP.put(K_BACKSPACE, GLFW.GLFW_KEY_BACKSPACE);
        KEY_MAP.put(K_CAPS_LOCK, GLFW.GLFW_KEY_CAPS_LOCK);
        KEY_MAP.put(K_COMMA, GLFW.GLFW_KEY_COMMA);
        KEY_MAP.put(K_DELETE, GLFW.GLFW_KEY_DELETE);
        KEY_MAP.put(K_DOWN, GLFW.GLFW_KEY_DOWN);
        KEY_MAP.put(K_END, GLFW.GLFW_KEY_END);
        KEY_MAP.put(K_ENTER, GLFW.GLFW_KEY_ENTER);
        KEY_MAP.put(K_EQUAL, GLFW.GLFW_KEY_EQUAL);
        KEY_MAP.put(K_ESCAPE, GLFW.GLFW_KEY_ESCAPE);
        KEY_MAP.put(K_GRAVE, GLFW.GLFW_KEY_GRAVE_ACCENT);
        KEY_MAP.put(K_HOME, GLFW.GLFW_KEY_HOME);
        KEY_MAP.put(K_INSERT, GLFW.GLFW_KEY_INSERT);
        KEY_MAP.put(K_MENU, GLFW.GLFW_KEY_MENU);
        KEY_MAP.put(K_MINUS, GLFW.GLFW_KEY_MINUS);
        KEY_MAP.put(K_NUM_LOCK, GLFW.GLFW_KEY_NUM_LOCK);
        KEY_MAP.put(K_PAGE_DOWN, GLFW.GLFW_KEY_PAGE_DOWN);
        KEY_MAP.put(K_PAGE_UP, GLFW.GLFW_KEY_PAGE_UP);
        KEY_MAP.put(K_PAUSE, GLFW.GLFW_KEY_PAUSE);
        KEY_MAP.put(K_PERIOD, GLFW.GLFW_KEY_PERIOD);
        KEY_MAP.put(K_PRINT_SCREEN, GLFW.GLFW_KEY_PRINT_SCREEN);
        KEY_MAP.put(K_SCROLL_LOCK, GLFW.GLFW_KEY_SCROLL_LOCK);
        KEY_MAP.put(K_SEMICOLON, GLFW.GLFW_KEY_SEMICOLON);
        KEY_MAP.put(K_SLASH, GLFW.GLFW_KEY_SLASH);
        KEY_MAP.put(K_SPACE, GLFW.GLFW_KEY_SPACE);
        KEY_MAP.put(K_TAB, GLFW.GLFW_KEY_TAB);
        KEY_MAP.put(K_UP, GLFW.GLFW_KEY_UP);
        KEY_MAP.put(K_WORLD1, GLFW.GLFW_KEY_WORLD_1);
        KEY_MAP.put(K_WORLD2, GLFW.GLFW_KEY_WORLD_2);
        
        for (int i=0; i<K_COUNT; i++) {
            KEY_ARRAY[i] = KEY_MAP.getValue(i);
        }
    }

}
