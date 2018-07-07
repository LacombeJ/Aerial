package ax.graphics;

/**
 * @author Jonathan Lacombe
 */
public interface Input {
    
    public static final int MB_COUNT         = 3;
    public static final int MB_NONE          = -1;
    public static final int MB_LEFT          = 0;
    public static final int MB_RIGHT         = 1;
    public static final int MB_MIDDLE        = 2;
    
    public static final int K_COUNT         = 114;
    public static final int K_NONE          = -1;
    public static final int K_0             = 0;
    public static final int K_1             = 1;
    public static final int K_2             = 2;
    public static final int K_3             = 3;
    public static final int K_4             = 4;
    public static final int K_5             = 5;
    public static final int K_6             = 6;
    public static final int K_7             = 7;
    public static final int K_8             = 8;
    public static final int K_9             = 9;
    public static final int K_A             = 10;
    public static final int K_B             = 11;
    public static final int K_C             = 12;
    public static final int K_D             = 13;
    public static final int K_E             = 14;
    public static final int K_F             = 15;
    public static final int K_G             = 16;
    public static final int K_H             = 17;
    public static final int K_I             = 18;
    public static final int K_J             = 19;
    public static final int K_K             = 20;
    public static final int K_L             = 21;
    public static final int K_M             = 22;
    public static final int K_N             = 23;
    public static final int K_O             = 24;
    public static final int K_P             = 25;
    public static final int K_Q             = 26;
    public static final int K_R             = 27;
    public static final int K_S             = 28;
    public static final int K_T             = 29;
    public static final int K_U             = 30;
    public static final int K_V             = 31;
    public static final int K_W             = 32;
    public static final int K_X             = 33;
    public static final int K_Y             = 34;
    public static final int K_Z             = 35;
    public static final int K_F1            = 36;
    public static final int K_F2            = 37;
    public static final int K_F3            = 38;
    public static final int K_F4            = 39;
    public static final int K_F5            = 40;
    public static final int K_F6            = 41;
    public static final int K_F7            = 42;
    public static final int K_F8            = 43;
    public static final int K_F9            = 44;
    public static final int K_F10           = 45;
    public static final int K_F11           = 46;
    public static final int K_F12           = 47;
    public static final int K_F13           = 48;
    public static final int K_F14           = 49;
    public static final int K_F15           = 50;
    public static final int K_F16           = 51;
    public static final int K_F17           = 52;
    public static final int K_F18           = 53;
    public static final int K_F19           = 54;
    public static final int K_NP_0          = 55;
    public static final int K_NP_1          = 56;
    public static final int K_NP_2          = 57;
    public static final int K_NP_3          = 58;
    public static final int K_NP_4          = 59;
    public static final int K_NP_5          = 60;
    public static final int K_NP_6          = 61;
    public static final int K_NP_7          = 62;
    public static final int K_NP_8          = 63;
    public static final int K_NP_9          = 64;
    public static final int K_NP_ADD        = 65;
    public static final int K_NP_DECIMAL    = 66;
    public static final int K_NP_DIVIDE     = 67;
    public static final int K_NP_ENTER      = 68;
    public static final int K_NP_EQUAL      = 69;
    public static final int K_NP_MULTIPLY   = 70;
    public static final int K_NP_SUBTRACT   = 71;
    public static final int K_LEFT          = 72;
    public static final int K_LALT          = 73;
    public static final int K_LBRACKET      = 74;
    public static final int K_LCONTROL      = 75;
    public static final int K_LSHIFT        = 76;
    public static final int K_LSUPER        = 77;
    public static final int K_RIGHT         = 78;
    public static final int K_RALT          = 79;
    public static final int K_RBRACKET      = 80;
    public static final int K_RCONTROL      = 81;
    public static final int K_RSHIFT        = 82;
    public static final int K_RSUPER        = 83;
    public static final int K_APOSTROPHE    = 84;
    public static final int K_BACKSLASH     = 85;
    public static final int K_BACKSPACE     = 86;
    public static final int K_CAPS_LOCK     = 87;
    public static final int K_COMMA         = 88;
    public static final int K_DELETE        = 89;
    public static final int K_DOWN          = 90;
    public static final int K_END           = 91;
    public static final int K_ENTER         = 92;
    public static final int K_EQUAL         = 93;
    public static final int K_ESCAPE        = 94;
    public static final int K_GRAVE         = 95;
    public static final int K_HOME          = 96;
    public static final int K_INSERT        = 97;
    public static final int K_MENU          = 98;
    public static final int K_MINUS         = 99;
    public static final int K_NUM_LOCK      = 100;
    public static final int K_PAGE_DOWN     = 101;
    public static final int K_PAGE_UP       = 102;
    public static final int K_PAUSE         = 103;
    public static final int K_PERIOD        = 104;
    public static final int K_PRINT_SCREEN  = 105;
    public static final int K_SCROLL_LOCK   = 106;
    public static final int K_SEMICOLON     = 107;
    public static final int K_SLASH         = 108;
    public static final int K_SPACE         = 109;
    public static final int K_TAB           = 110;
    public static final int K_UP            = 111;
    public static final int K_WORLD1        = 112;
    public static final int K_WORLD2        = 113;
    
    public static enum CursorState {
        NORMAL,
        GRABBED,
        HIDDEN;
    }
    
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
    
    public float getDX();
    public float getDY();
    
    public float getScrollX();
    public float getScrollY();
    
    public boolean isButtonDown(int mbutton);
    public boolean isButtonPressed(int mbutton);
    public boolean isButtonReleased(int mbutton);
    
    
    
}
