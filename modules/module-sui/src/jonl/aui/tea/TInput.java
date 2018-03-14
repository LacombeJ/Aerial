package jonl.aui.tea;

import jonl.jgl.Input;
import jonl.jgl.InputEvent;
import jonl.jutils.func.Function0D;

public class TInput implements Input {

    private Input input;
    
    private Function0D<Integer> windowHeight;
    
    public TInput(Input input, Function0D<Integer> windowWidth) {
        this.input = input;
        this.windowHeight = windowWidth;
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

}
