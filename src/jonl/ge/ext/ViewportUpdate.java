package jonl.ge.ext;

import jonl.ge.Camera;
import jonl.ge.Property;

/**
 * Camera property that updates viewport from updated window dimensions
 * 
 * @author Jonathan Lacombe
 *
 */
public class ViewportUpdate extends Property {

    Camera camera;
    
    @Override
    public void create() {
        camera = getComponent(Camera.class);
        viewport();
    }
    
    @Override
    public void update() {
        viewport();
    }
    
    private void viewport() {
        int[] b = getWindowSize();
        camera.setViewport(0,0,b[0],b[1]);
    }
    
}
