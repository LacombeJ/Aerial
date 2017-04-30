package jonl.ge.ext;

import jonl.jgl.Input;
import jonl.jgl.InputEvent;

import jonl.ge.Camera;

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
        if (getInput().checkEvent(event)
                ) {
            if (camera.isOrthograpic()) {
                camera.setPerspective();
            } else {
                camera.setOrthographic();
            }
        }
    }
    
}
