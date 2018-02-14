package jonl.ge.ext;

import jonl.ge.core.Camera;
import jonl.ge.core.Property;
import jonl.ge.core.Window;

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
        Window window = window();
        camera.setViewport(0,0,window.width(),window.height());
    }
    
}
