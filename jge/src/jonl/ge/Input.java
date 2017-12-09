package jonl.ge;

import jonl.vmath.Vector2;

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
    
    
    
    
    
    
    public static final int MB_COUNT        = jonl.jgl.Input.MB_COUNT;
    public static final int MB_NONE         = jonl.jgl.Input.MB_NONE;
    public static final int MB_LEFT         = jonl.jgl.Input.MB_LEFT;
    public static final int MB_RIGHT        = jonl.jgl.Input.MB_RIGHT;
    public static final int MB_MIDDLE       = jonl.jgl.Input.MB_MIDDLE;
    
    public static final int K_COUNT         = jonl.jgl.Input.K_COUNT;
    public static final int K_NONE          = jonl.jgl.Input.K_NONE;
    public static final int K_0             = jonl.jgl.Input.K_0;
    public static final int K_1             = jonl.jgl.Input.K_1;
    public static final int K_2             = jonl.jgl.Input.K_2;
    public static final int K_3             = jonl.jgl.Input.K_3;
    public static final int K_4             = jonl.jgl.Input.K_4;
    public static final int K_5             = jonl.jgl.Input.K_5;
    public static final int K_6             = jonl.jgl.Input.K_6;
    public static final int K_7             = jonl.jgl.Input.K_7;
    public static final int K_8             = jonl.jgl.Input.K_8;
    public static final int K_9             = jonl.jgl.Input.K_9;
    public static final int K_A             = jonl.jgl.Input.K_A;
    public static final int K_B             = jonl.jgl.Input.K_B;
    public static final int K_C             = jonl.jgl.Input.K_C;
    public static final int K_D             = jonl.jgl.Input.K_D;
    public static final int K_E             = jonl.jgl.Input.K_E;
    public static final int K_F             = jonl.jgl.Input.K_F;
    public static final int K_G             = jonl.jgl.Input.K_G;
    public static final int K_H             = jonl.jgl.Input.K_H;
    public static final int K_I             = jonl.jgl.Input.K_I;
    public static final int K_J             = jonl.jgl.Input.K_J;
    public static final int K_K             = jonl.jgl.Input.K_K;
    public static final int K_L             = jonl.jgl.Input.K_L;
    public static final int K_M             = jonl.jgl.Input.K_M;
    public static final int K_N             = jonl.jgl.Input.K_N;
    public static final int K_O             = jonl.jgl.Input.K_O;
    public static final int K_P             = jonl.jgl.Input.K_P;
    public static final int K_Q             = jonl.jgl.Input.K_Q;
    public static final int K_R             = jonl.jgl.Input.K_R;
    public static final int K_S             = jonl.jgl.Input.K_S;
    public static final int K_T             = jonl.jgl.Input.K_T;
    public static final int K_U             = jonl.jgl.Input.K_U;
    public static final int K_V             = jonl.jgl.Input.K_V;
    public static final int K_W             = jonl.jgl.Input.K_W;
    public static final int K_X             = jonl.jgl.Input.K_X;
    public static final int K_Y             = jonl.jgl.Input.K_Y;
    public static final int K_Z             = jonl.jgl.Input.K_Z;
    public static final int K_F1            = jonl.jgl.Input.K_F1;
    public static final int K_F2            = jonl.jgl.Input.K_F2;
    public static final int K_F3            = jonl.jgl.Input.K_F3;
    public static final int K_F4            = jonl.jgl.Input.K_F4;
    public static final int K_F5            = jonl.jgl.Input.K_F5;
    public static final int K_F6            = jonl.jgl.Input.K_F6;
    public static final int K_F7            = jonl.jgl.Input.K_F7;
    public static final int K_F8            = jonl.jgl.Input.K_F8;
    public static final int K_F9            = jonl.jgl.Input.K_F9;
    public static final int K_F10           = jonl.jgl.Input.K_F10;
    public static final int K_F11           = jonl.jgl.Input.K_F11;
    public static final int K_F12           = jonl.jgl.Input.K_F12;
    public static final int K_F13           = jonl.jgl.Input.K_F13;
    public static final int K_F14           = jonl.jgl.Input.K_F14;
    public static final int K_F15           = jonl.jgl.Input.K_F15;
    public static final int K_F16           = jonl.jgl.Input.K_F16;
    public static final int K_F17           = jonl.jgl.Input.K_F17;
    public static final int K_F18           = jonl.jgl.Input.K_F18;
    public static final int K_F19           = jonl.jgl.Input.K_F19;
    public static final int K_NP_0          = jonl.jgl.Input.K_NP_0;
    public static final int K_NP_1          = jonl.jgl.Input.K_NP_1;
    public static final int K_NP_2          = jonl.jgl.Input.K_NP_2;
    public static final int K_NP_3          = jonl.jgl.Input.K_NP_3;
    public static final int K_NP_4          = jonl.jgl.Input.K_NP_4;
    public static final int K_NP_5          = jonl.jgl.Input.K_NP_5;
    public static final int K_NP_6          = jonl.jgl.Input.K_NP_6;
    public static final int K_NP_7          = jonl.jgl.Input.K_NP_7;
    public static final int K_NP_8          = jonl.jgl.Input.K_NP_8;
    public static final int K_NP_9          = jonl.jgl.Input.K_NP_9;
    public static final int K_NP_ADD        = jonl.jgl.Input.K_NP_ADD;
    public static final int K_NP_DECIMAL    = jonl.jgl.Input.K_NP_DECIMAL;
    public static final int K_NP_DIVIDE     = jonl.jgl.Input.K_NP_DIVIDE;
    public static final int K_NP_ENTER      = jonl.jgl.Input.K_NP_ENTER;
    public static final int K_NP_EQUAL      = jonl.jgl.Input.K_NP_EQUAL;
    public static final int K_NP_MULTIPLY   = jonl.jgl.Input.K_NP_MULTIPLY;
    public static final int K_NP_SUBTRACT   = jonl.jgl.Input.K_NP_SUBTRACT;
    public static final int K_LEFT          = jonl.jgl.Input.K_LEFT;
    public static final int K_LALT          = jonl.jgl.Input.K_LALT;
    public static final int K_LBRACKET      = jonl.jgl.Input.K_LBRACKET;
    public static final int K_LCONTROL      = jonl.jgl.Input.K_LCONTROL;
    public static final int K_LSHIFT        = jonl.jgl.Input.K_LSHIFT;
    public static final int K_LSUPER        = jonl.jgl.Input.K_LSUPER;
    public static final int K_RIGHT         = jonl.jgl.Input.K_RIGHT;
    public static final int K_RALT          = jonl.jgl.Input.K_RALT;
    public static final int K_RBRACKET      = jonl.jgl.Input.K_RBRACKET;
    public static final int K_RCONTROL      = jonl.jgl.Input.K_RCONTROL;
    public static final int K_RSHIFT        = jonl.jgl.Input.K_RSHIFT;
    public static final int K_RSUPER        = jonl.jgl.Input.K_RSUPER;
    public static final int K_APOSTROPHE    = jonl.jgl.Input.K_APOSTROPHE;
    public static final int K_BACKSLASH     = jonl.jgl.Input.K_BACKSLASH;
    public static final int K_BACKSPACE     = jonl.jgl.Input.K_BACKSPACE;
    public static final int K_CAPS_LOCK     = jonl.jgl.Input.K_CAPS_LOCK;
    public static final int K_COMMA         = jonl.jgl.Input.K_COMMA;
    public static final int K_DELETE        = jonl.jgl.Input.K_DELETE;
    public static final int K_DOWN          = jonl.jgl.Input.K_DOWN;
    public static final int K_END           = jonl.jgl.Input.K_END;
    public static final int K_ENTER         = jonl.jgl.Input.K_ENTER;
    public static final int K_EQUAL         = jonl.jgl.Input.K_EQUAL;
    public static final int K_ESCAPE        = jonl.jgl.Input.K_ESCAPE;
    public static final int K_GRAVE         = jonl.jgl.Input.K_GRAVE;
    public static final int K_HOME          = jonl.jgl.Input.K_HOME;
    public static final int K_INSERT        = jonl.jgl.Input.K_INSERT;
    public static final int K_MENU          = jonl.jgl.Input.K_MENU;
    public static final int K_MINUS         = jonl.jgl.Input.K_MINUS;
    public static final int K_NUM_LOCK      = jonl.jgl.Input.K_NUM_LOCK;
    public static final int K_PAGE_DOWN     = jonl.jgl.Input.K_PAGE_DOWN;
    public static final int K_PAGE_UP       = jonl.jgl.Input.K_PAGE_UP;
    public static final int K_PAUSE         = jonl.jgl.Input.K_PAUSE;
    public static final int K_PERIOD        = jonl.jgl.Input.K_PERIOD;
    public static final int K_PRINT_SCREEN  = jonl.jgl.Input.K_PRINT_SCREEN;
    public static final int K_SCROLL_LOCK   = jonl.jgl.Input.K_SCROLL_LOCK;
    public static final int K_SEMICOLON     = jonl.jgl.Input.K_SEMICOLON;
    public static final int K_SLASH         = jonl.jgl.Input.K_SLASH;
    public static final int K_SPACE         = jonl.jgl.Input.K_SPACE;
    public static final int K_TAB           = jonl.jgl.Input.K_TAB;
    public static final int K_UP            = jonl.jgl.Input.K_UP;
    public static final int K_WORLD1        = jonl.jgl.Input.K_WORLD1;
    public static final int K_WORLD2        = jonl.jgl.Input.K_WORLD2;

    
    public static enum CursorState {
        NORMAL(jonl.jgl.Input.CursorState.NORMAL),
        GRABBED(jonl.jgl.Input.CursorState.GRABBED),
        HIDDEN(jonl.jgl.Input.CursorState.HIDDEN);
        jonl.jgl.Input.CursorState state;
        CursorState(jonl.jgl.Input.CursorState state) {
            this.state = state;
        }
        static CursorState state(jonl.jgl.Input.CursorState state) {
            switch (state) {
            case NORMAL: return NORMAL;
            case GRABBED: return GRABBED;
            case HIDDEN: return HIDDEN;
            }
            return null;
        }
    }
    
    
    
}
