package jonl.ge.core.app;

import jonl.ge.core.Input;
import jonl.ge.core.InputEvent;
import jonl.vmath.Vector2;

/**
 * @author Jonathan Lacombe
 */
public class ApplicationInput implements Input {
    
    final jonl.jgl.Input input;
    
    public ApplicationInput(jonl.jgl.Input input) {
        this.input = input;
    }
    
    /**
     * Assumes that InputEvent is has valid type and code, returns false if not
     * @return true if event occurred
     */
    public boolean checkEvent(InputEvent e) {
        return input.checkEvent(new jonl.jgl.InputEvent(e.type, e.code));
    }
    
    /////////////////////////////////////////////////////////
    //////////////////      Keyboard      ///////////////////
    /////////////////////////////////////////////////////////
    
    public boolean isKeyDown(int key) {
        return input.isKeyDown(key);
    }
    public boolean isKeyPressed(int key) {
        return input.isKeyPressed(key);
    }
    public boolean isKeyReleased(int key) {
        return input.isKeyReleased(key);
    }
    public boolean isKeyRepeated(int key) {
        return input.isKeyRepeated(key);
    }
    
    /////////////////////////////////////////////////////////
    //////////////////       Mouse        ///////////////////
    /////////////////////////////////////////////////////////
    
    public float getX() {
        return input.getX();
    }
    public float getY() {
        return input.getY();
    }
    public Vector2 getXY() {
        return new Vector2(getX(),getY());
    }
    
    public float getDX() {
        return input.getDX();
    }
    public float getDY() {
        return input.getDY();
    }
    public Vector2 getDXY() {
        return new Vector2(getDX(),getDY());
    }
    
    public float getScrollX() {
        return input.getScrollX();
    }
    public float getScrollY() {
        return input.getScrollY();
    }
    public Vector2 getScrollXY() {
        return new Vector2(getScrollX(),getScrollY());
    }
    
    public boolean isButtonDown(int mbutton) {
        return input.isButtonDown(mbutton);
    }
    public boolean isButtonPressed(int mbutton) {
        return input.isButtonPressed(mbutton);
    }
    public boolean isButtonReleased(int mbutton) {
        return input.isButtonReleased(mbutton);
    }
    
    
}
