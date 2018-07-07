package jonl.ge.mod.misc;

import jonl.ge.core.Camera;
import jonl.ge.core.Property;

/**
 * Camera property that updates aspect ratio of perspective from updated window dimensions
 * 
 * @author Jonathan Lacombe
 *
 */
public class PerspectiveUpdate extends Property {

    Camera camera = null;
    
    public PerspectiveUpdate(Camera camera) {
        this.camera = camera;
    }
    
    public PerspectiveUpdate() {
        
    }
    
    @Override
    public void create() {
        if (camera==null) camera = getComponent(Camera.class);
        perspective();
    }
    
    @Override
    public void update() {
        perspective();
    }
    
    private void perspective() {
        float aspect = window().aspect();
        if (camera.isOrthograpic()) {
            camera.setOrthographic(camera.height(),aspect,camera.near(),camera.far());
        } else {
            camera.setPerspective(camera.fov(),aspect,camera.near(),camera.far());
        }
    }
    
}
