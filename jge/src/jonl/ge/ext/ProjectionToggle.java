package jonl.ge.ext;

import jonl.ge.core.Camera;
import jonl.ge.core.Input;
import jonl.ge.core.InputEvent;

public class ProjectionToggle extends Toggle {
    
    public Camera camera;
    
    public ProjectionToggle(InputEvent event) {
        super(event);
    }
    
    public ProjectionToggle() {
        event = new InputEvent(InputEvent.KEY_PRESSED,Input.K_E);
    }
    
    @Override
    public void create() {
        camera = getComponent(Camera.class);
    }
    
    @Override
    public void update() {
        if (input().checkEvent(event)
                ) {
            if (camera.isOrthograpic()) {
                camera.setPerspective();
            } else {
                camera.setOrthographic();
            }
        }
    }
    
}
