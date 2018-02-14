package jonl.ge.ext;

import jonl.ge.core.Camera;
import jonl.ge.core.Property;

/**
 * Camera property that updates aspect ratio of perspective from updated window dimensions
 * 
 * @author Jonathan Lacombe
 *
 */
public class PerspectiveUpdate extends Property {

    Camera camera;
    
    @Override
    public void create() {
        camera = getComponent(Camera.class);
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
