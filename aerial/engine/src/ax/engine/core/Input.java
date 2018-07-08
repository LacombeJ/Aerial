package ax.engine.core;

import ax.math.vector.Vector2;

/**
 * @author Jonathan Lacombe
 */
public interface Input {
    
    /**
     * Assumes that InputEvent is has valid type and code, returns false if not
     * @return true if event occurred
     */
    public boolean checkEvent(InputEvent e);
    
    /////////////////////////////////////////////////////////
    //////////////////      Keyboard      ///////////////////
    /////////////////////////////////////////////////////////
    
    public boolean isKeyDown(int key);
    public boolean isKeyPressed(int key);
    public boolean isKeyReleased(int key);
    public boolean isKeyRepeated(int key);
    
    /////////////////////////////////////////////////////////
    //////////////////       Mouse        ///////////////////
    /////////////////////////////////////////////////////////
    
    public float getX();
    public float getY();
    public Vector2 getXY();
    
    public float getDX();
    public float getDY();
    public Vector2 getDXY();
    
    public float getScrollX();
    public float getScrollY();
    public Vector2 getScrollXY();
    
    public boolean isButtonDown(int mbutton);
    public boolean isButtonPressed(int mbutton);
    public boolean isButtonReleased(int mbutton);
    
    
    
    
    
    
    public static final int MB_COUNT        = ax.graphics.Input.MB_COUNT;
    public static final int MB_NONE         = ax.graphics.Input.MB_NONE;
    public static final int MB_LEFT         = ax.graphics.Input.MB_LEFT;
    public static final int MB_RIGHT        = ax.graphics.Input.MB_RIGHT;
    public static final int MB_MIDDLE       = ax.graphics.Input.MB_MIDDLE;
    
    public static final int K_COUNT         = ax.graphics.Input.K_COUNT;
    public static final int K_NONE          = ax.graphics.Input.K_NONE;
    public static final int K_0             = ax.graphics.Input.K_0;
    public static final int K_1             = ax.graphics.Input.K_1;
    public static final int K_2             = ax.graphics.Input.K_2;
    public static final int K_3             = ax.graphics.Input.K_3;
    public static final int K_4             = ax.graphics.Input.K_4;
    public static final int K_5             = ax.graphics.Input.K_5;
    public static final int K_6             = ax.graphics.Input.K_6;
    public static final int K_7             = ax.graphics.Input.K_7;
    public static final int K_8             = ax.graphics.Input.K_8;
    public static final int K_9             = ax.graphics.Input.K_9;
    public static final int K_A             = ax.graphics.Input.K_A;
    public static final int K_B             = ax.graphics.Input.K_B;
    public static final int K_C             = ax.graphics.Input.K_C;
    public static final int K_D             = ax.graphics.Input.K_D;
    public static final int K_E             = ax.graphics.Input.K_E;
    public static final int K_F             = ax.graphics.Input.K_F;
    public static final int K_G             = ax.graphics.Input.K_G;
    public static final int K_H             = ax.graphics.Input.K_H;
    public static final int K_I             = ax.graphics.Input.K_I;
    public static final int K_J             = ax.graphics.Input.K_J;
    public static final int K_K             = ax.graphics.Input.K_K;
    public static final int K_L             = ax.graphics.Input.K_L;
    public static final int K_M             = ax.graphics.Input.K_M;
    public static final int K_N             = ax.graphics.Input.K_N;
    public static final int K_O             = ax.graphics.Input.K_O;
    public static final int K_P             = ax.graphics.Input.K_P;
    public static final int K_Q             = ax.graphics.Input.K_Q;
    public static final int K_R             = ax.graphics.Input.K_R;
    public static final int K_S             = ax.graphics.Input.K_S;
    public static final int K_T             = ax.graphics.Input.K_T;
    public static final int K_U             = ax.graphics.Input.K_U;
    public static final int K_V             = ax.graphics.Input.K_V;
    public static final int K_W             = ax.graphics.Input.K_W;
    public static final int K_X             = ax.graphics.Input.K_X;
    public static final int K_Y             = ax.graphics.Input.K_Y;
    public static final int K_Z             = ax.graphics.Input.K_Z;
    public static final int K_F1            = ax.graphics.Input.K_F1;
    public static final int K_F2            = ax.graphics.Input.K_F2;
    public static final int K_F3            = ax.graphics.Input.K_F3;
    public static final int K_F4            = ax.graphics.Input.K_F4;
    public static final int K_F5            = ax.graphics.Input.K_F5;
    public static final int K_F6            = ax.graphics.Input.K_F6;
    public static final int K_F7            = ax.graphics.Input.K_F7;
    public static final int K_F8            = ax.graphics.Input.K_F8;
    public static final int K_F9            = ax.graphics.Input.K_F9;
    public static final int K_F10           = ax.graphics.Input.K_F10;
    public static final int K_F11           = ax.graphics.Input.K_F11;
    public static final int K_F12           = ax.graphics.Input.K_F12;
    public static final int K_F13           = ax.graphics.Input.K_F13;
    public static final int K_F14           = ax.graphics.Input.K_F14;
    public static final int K_F15           = ax.graphics.Input.K_F15;
    public static final int K_F16           = ax.graphics.Input.K_F16;
    public static final int K_F17           = ax.graphics.Input.K_F17;
    public static final int K_F18           = ax.graphics.Input.K_F18;
    public static final int K_F19           = ax.graphics.Input.K_F19;
    public static final int K_NP_0          = ax.graphics.Input.K_NP_0;
    public static final int K_NP_1          = ax.graphics.Input.K_NP_1;
    public static final int K_NP_2          = ax.graphics.Input.K_NP_2;
    public static final int K_NP_3          = ax.graphics.Input.K_NP_3;
    public static final int K_NP_4          = ax.graphics.Input.K_NP_4;
    public static final int K_NP_5          = ax.graphics.Input.K_NP_5;
    public static final int K_NP_6          = ax.graphics.Input.K_NP_6;
    public static final int K_NP_7          = ax.graphics.Input.K_NP_7;
    public static final int K_NP_8          = ax.graphics.Input.K_NP_8;
    public static final int K_NP_9          = ax.graphics.Input.K_NP_9;
    public static final int K_NP_ADD        = ax.graphics.Input.K_NP_ADD;
    public static final int K_NP_DECIMAL    = ax.graphics.Input.K_NP_DECIMAL;
    public static final int K_NP_DIVIDE     = ax.graphics.Input.K_NP_DIVIDE;
    public static final int K_NP_ENTER      = ax.graphics.Input.K_NP_ENTER;
    public static final int K_NP_EQUAL      = ax.graphics.Input.K_NP_EQUAL;
    public static final int K_NP_MULTIPLY   = ax.graphics.Input.K_NP_MULTIPLY;
    public static final int K_NP_SUBTRACT   = ax.graphics.Input.K_NP_SUBTRACT;
    public static final int K_LEFT          = ax.graphics.Input.K_LEFT;
    public static final int K_LALT          = ax.graphics.Input.K_LALT;
    public static final int K_LBRACKET      = ax.graphics.Input.K_LBRACKET;
    public static final int K_LCONTROL      = ax.graphics.Input.K_LCONTROL;
    public static final int K_LSHIFT        = ax.graphics.Input.K_LSHIFT;
    public static final int K_LSUPER        = ax.graphics.Input.K_LSUPER;
    public static final int K_RIGHT         = ax.graphics.Input.K_RIGHT;
    public static final int K_RALT          = ax.graphics.Input.K_RALT;
    public static final int K_RBRACKET      = ax.graphics.Input.K_RBRACKET;
    public static final int K_RCONTROL      = ax.graphics.Input.K_RCONTROL;
    public static final int K_RSHIFT        = ax.graphics.Input.K_RSHIFT;
    public static final int K_RSUPER        = ax.graphics.Input.K_RSUPER;
    public static final int K_APOSTROPHE    = ax.graphics.Input.K_APOSTROPHE;
    public static final int K_BACKSLASH     = ax.graphics.Input.K_BACKSLASH;
    public static final int K_BACKSPACE     = ax.graphics.Input.K_BACKSPACE;
    public static final int K_CAPS_LOCK     = ax.graphics.Input.K_CAPS_LOCK;
    public static final int K_COMMA         = ax.graphics.Input.K_COMMA;
    public static final int K_DELETE        = ax.graphics.Input.K_DELETE;
    public static final int K_DOWN          = ax.graphics.Input.K_DOWN;
    public static final int K_END           = ax.graphics.Input.K_END;
    public static final int K_ENTER         = ax.graphics.Input.K_ENTER;
    public static final int K_EQUAL         = ax.graphics.Input.K_EQUAL;
    public static final int K_ESCAPE        = ax.graphics.Input.K_ESCAPE;
    public static final int K_GRAVE         = ax.graphics.Input.K_GRAVE;
    public static final int K_HOME          = ax.graphics.Input.K_HOME;
    public static final int K_INSERT        = ax.graphics.Input.K_INSERT;
    public static final int K_MENU          = ax.graphics.Input.K_MENU;
    public static final int K_MINUS         = ax.graphics.Input.K_MINUS;
    public static final int K_NUM_LOCK      = ax.graphics.Input.K_NUM_LOCK;
    public static final int K_PAGE_DOWN     = ax.graphics.Input.K_PAGE_DOWN;
    public static final int K_PAGE_UP       = ax.graphics.Input.K_PAGE_UP;
    public static final int K_PAUSE         = ax.graphics.Input.K_PAUSE;
    public static final int K_PERIOD        = ax.graphics.Input.K_PERIOD;
    public static final int K_PRINT_SCREEN  = ax.graphics.Input.K_PRINT_SCREEN;
    public static final int K_SCROLL_LOCK   = ax.graphics.Input.K_SCROLL_LOCK;
    public static final int K_SEMICOLON     = ax.graphics.Input.K_SEMICOLON;
    public static final int K_SLASH         = ax.graphics.Input.K_SLASH;
    public static final int K_SPACE         = ax.graphics.Input.K_SPACE;
    public static final int K_TAB           = ax.graphics.Input.K_TAB;
    public static final int K_UP            = ax.graphics.Input.K_UP;
    public static final int K_WORLD1        = ax.graphics.Input.K_WORLD1;
    public static final int K_WORLD2        = ax.graphics.Input.K_WORLD2;

    
    public static enum CursorState {
        NORMAL(ax.graphics.Input.CursorState.NORMAL),
        GRABBED(ax.graphics.Input.CursorState.GRABBED),
        HIDDEN(ax.graphics.Input.CursorState.HIDDEN);
        ax.graphics.Input.CursorState state;
        CursorState(ax.graphics.Input.CursorState state) {
            this.state = state;
        }
        static CursorState state(ax.graphics.Input.CursorState state) {
            switch (state) {
            case NORMAL: return NORMAL;
            case GRABBED: return GRABBED;
            case HIDDEN: return HIDDEN;
            }
            return null;
        }
    }
    
    
    
}
