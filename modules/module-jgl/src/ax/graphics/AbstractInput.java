package ax.graphics;

public abstract class AbstractInput implements Input {

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
    
}
