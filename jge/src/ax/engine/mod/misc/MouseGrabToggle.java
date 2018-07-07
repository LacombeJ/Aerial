package ax.engine.mod.misc;

import ax.engine.core.Input;
import ax.engine.core.InputEvent;
import ax.engine.core.Input.CursorState;

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
        if (input().checkEvent(event)) {
            if (getCursorState()==CursorState.GRABBED) {
                setCursorState(CursorState.NORMAL);
            } else {
                setCursorState(CursorState.GRABBED);
            }
        }
    }
    
}
