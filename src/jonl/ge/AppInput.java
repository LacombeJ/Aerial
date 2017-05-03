package jonl.ge;

/**
 * @author Jonathan Lacombe
 */
class AppInput implements Input {
    
    final jonl.jgl.Input input;
    
    AppInput(jonl.jgl.Input input) {
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
    
    /////////////////////////////////////////////////////////
    //////////////////       Mouse        ///////////////////
    /////////////////////////////////////////////////////////
    
    public float getX() {
        return input.getX();
    }
    public float getY() {
        return input.getY();
    }
    
    public float getDX() {
        return input.getDX();
    }
    public float getDY() {
        return input.getDY();
    }
    
    public float getScrollX() {
        return input.getScrollX();
    }
    public float getScrollY() {
        return input.getScrollX();
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
