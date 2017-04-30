package jonl.ge.ext;

import jonl.jgl.Input;
import jonl.jgl.Input.CursorState;
import jonl.jgl.InputEvent;

public class MouseGrabToggle extends Toggle {
    
    public MouseGrabToggle(InputEvent event) {
        super(event);
    }
    
    public MouseGrabToggle() {
        event = new InputEvent(InputEvent.BUTTON_PRESSED,Input.MB_LEFT);
    }
    
    @Override
    public void create() {
        
    }
    
    @Override
    public void update() {
        if (getInput().checkEvent(event)) {
            if (getCursorState()==CursorState.GRABBED) {
                setCursorState(CursorState.NORMAL);
            } else {
                setCursorState(CursorState.GRABBED);
            }
        }
    }
    
}
