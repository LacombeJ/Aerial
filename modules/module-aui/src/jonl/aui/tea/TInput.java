package jonl.aui.tea;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import jonl.jgl.Input;
import jonl.jgl.InputEvent;
import jonl.jutils.func.Function0D;

public class TInput implements Input {

    private Input input;
    
    private Function0D<Integer> windowHeight;
    
    public TInput(Input input, Function0D<Integer> windowHeight) {
        this.input = input;
        this.windowHeight = windowHeight;
    }
    
    public int windowHeight() {
        return windowHeight.f();
    }
    
    @Override
    public boolean checkEvent(InputEvent e) {
        return input.checkEvent(e);
    }

    @Override
    public boolean isKeyDown(int key) {
        return input.isKeyDown(key);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return input.isKeyPressed(key);
    }

    @Override
    public boolean isKeyReleased(int key) {
        return input.isKeyReleased(key);
    }
    
    @Override
    public boolean isKeyRepeated(int key) {
        return input.isKeyRepeated(key);
    }

    @Override
    public float getX() {
        return input.getX();
    }

    @Override
    public float getY() {
        return windowHeight.f() - input.getY();
    }

    @Override
    public float getDX() {
        return input.getDX();
    }

    @Override
    public float getDY() {
        return - input.getDY();
    }

    @Override
    public float getScrollX() {
        return input.getScrollX();
    }

    @Override
    public float getScrollY() {
        return input.getScrollY();
    }

    @Override
    public boolean isButtonDown(int mbutton) {
        return input.isButtonDown(mbutton);
    }

    @Override
    public boolean isButtonPressed(int mbutton) {
        return input.isButtonPressed(mbutton);
    }

    @Override
    public boolean isButtonReleased(int mbutton) {
        return input.isButtonReleased(mbutton);
    }

    public char getChar(int key, boolean shift) {
        
        if (key==Input.K_SPACE) return ' ';
        
        boolean caps = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        caps = caps ^ shift;
        
        if (key >= Input.K_A && key <= Input.K_Z) {
            char c = (char) ('a' + (key-Input.K_A));
            if (caps) {
                return Character.toUpperCase(c);
            }
            return c;
        }
        
        if (key==Input.K_1) return shift ? '!' : '1';
        if (key==Input.K_2) return shift ? '@' : '2';
        if (key==Input.K_3) return shift ? '#' : '3';
        if (key==Input.K_4) return shift ? '$' : '4';
        if (key==Input.K_5) return shift ? '%' : '5';
        if (key==Input.K_6) return shift ? '^' : '6';
        if (key==Input.K_7) return shift ? '&' : '7';
        if (key==Input.K_8) return shift ? '*' : '8';
        if (key==Input.K_9) return shift ? '(' : '9';
        if (key==Input.K_0) return shift ? ')' : '0';
        
        if (key==Input.K_COMMA)         return shift ? '<' : ',';
        if (key==Input.K_PERIOD)        return shift ? '>' : '.';
        if (key==Input.K_SLASH)         return shift ? '?' : '/';
        if (key==Input.K_SEMICOLON)     return shift ? ':' : ';';
        if (key==Input.K_APOSTROPHE)    return shift ? '"' : '\'';
        if (key==Input.K_LBRACKET)      return shift ? '{' : '[';
        if (key==Input.K_RBRACKET)      return shift ? '}' : ']';
        if (key==Input.K_BACKSLASH)     return shift ? '|' : '\\';
        if (key==Input.K_MINUS)         return shift ? '_' : '-';
        if (key==Input.K_EQUAL)         return shift ? '+' : '=';
        if (key==Input.K_GRAVE)         return shift ? '~' : '`';
        
        boolean numLock = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);
        
        if (key==Input.K_NP_1) return numLock ? '1' : 0;
        if (key==Input.K_NP_2) return numLock ? '2' : 0;
        if (key==Input.K_NP_3) return numLock ? '3' : 0;
        if (key==Input.K_NP_4) return numLock ? '4' : 0;
        if (key==Input.K_NP_5) return numLock ? '5' : 0;
        if (key==Input.K_NP_6) return numLock ? '6' : 0;
        if (key==Input.K_NP_7) return numLock ? '7' : 0;
        if (key==Input.K_NP_8) return numLock ? '8' : 0;
        if (key==Input.K_NP_9) return numLock ? '9' : 0;
        if (key==Input.K_NP_0) return numLock ? '0' : 0;
        
        return 0;
    }

}
