package ax.aui.tea;

import ax.commons.func.Function0D;
import ax.graphics.Input;
import ax.graphics.InputEvent;

public class THandledInput extends TInput {
    
    float x;
    float y;
    float dx;
    float dy;
    float scrollX;
    float scrollY;
    final boolean[] buttonDown          = new boolean[MB_COUNT];
    final boolean[] buttonPressed       = new boolean[MB_COUNT];
    final boolean[] buttonReleased      = new boolean[MB_COUNT];
    final boolean[] keyDown             = new boolean[K_COUNT];
    final boolean[] keyPressed          = new boolean[K_COUNT];
    final boolean[] keyReleased         = new boolean[K_COUNT];
    final boolean[] keyRepeated         = new boolean[K_COUNT];
    
    public THandledInput(Input input, Function0D<Integer> windowHeight) {
        super(input,windowHeight);
    }
    
    void update() {
        for (int i=0; i<K_COUNT; i++) {
            if (keyPressed[i]) {
                keyDown[i] = true;
            }
            if (keyReleased[i]) {
                keyDown[i] = false;
            }
        }
        for (int i=0; i<MB_COUNT; i++) {
            if (buttonPressed[i]) {
                buttonDown[i] = true;
            }
            if (keyReleased[i]) {
                buttonDown[i] = false;
            }
        }
    }
    
    @Override
    public boolean checkEvent(InputEvent e) {
        switch(e.type) {
        case InputEvent.KEY_PRESSED:        return isKeyPressed(e.code);
        case InputEvent.KEY_RELEASED:       return isKeyReleased(e.code);
        case InputEvent.KEY_DOWN:           return isKeyDown(e.code);
        case InputEvent.BUTTON_PRESSED:     return isButtonPressed(e.code);
        case InputEvent.BUTTON_RELEASED:    return isButtonPressed(e.code);
        case InputEvent.BUTTON_DOWN:        return isButtonPressed(e.code);
        }
        return false;
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
    public boolean isButtonDown(int mbutton) {
        return buttonDown[mbutton];
    }

    @Override
    public boolean isButtonPressed(int mbutton) {
        return buttonPressed[mbutton];
    }

    @Override
    public boolean isButtonReleased(int mbutton) {
        return buttonReleased[mbutton];
    }

}
