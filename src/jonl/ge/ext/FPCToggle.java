package jonl.ge.ext;

import jonl.ge.Input;
import jonl.ge.Input.CursorState;
import jonl.ge.Property;

public class FPCToggle extends Property {
    
    FirstPersonControl fpc;
    Input input;
    boolean leftToggle = false; // Whether fpc is toggled by left click pressed
    boolean allowLeftToggle = false;
    
    public FPCToggle() {
        
    }
    
    public FPCToggle(boolean allowLeftToggle) {
        this.allowLeftToggle = allowLeftToggle;
    }
    
    @Override
    public void create() {
        fpc = getComponent(FirstPersonControl.class);
        input = getInput();
        fpc.update = false;
    }
    
    @Override
    public void update() {
        
        if (allowLeftToggle) {
            if (input.isButtonPressed(Input.MB_LEFT)) {
                leftToggle = !leftToggle;
                if (leftToggle) {
                    fpc.update = true;
                    setCursorState(CursorState.GRABBED);
                } else {
                    fpc.update = false;
                    setCursorState(CursorState.NORMAL);
                }
            }
        }
        
        if (!leftToggle) {
            if (input.isButtonPressed(Input.MB_RIGHT)) {
                fpc.update = true;
                setCursorState(CursorState.GRABBED);
            }
            if (input.isButtonReleased(Input.MB_RIGHT)) {
                fpc.update = false;
                setCursorState(CursorState.NORMAL);
            }
        }

    }
    
}
