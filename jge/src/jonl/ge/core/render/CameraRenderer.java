package jonl.ge.core.render;

import jonl.ge.core.Camera;
import jonl.ge.core.Component;
import jonl.ge.core.Scene;
import jonl.ge.core.Service;

/**
 * Adds rendering functionality outside of render loop.
 * 
 * @author Jonathan Lacombe
 *
 */
public class CameraRenderer extends Component {

    public void render() {
        
        Scene scene = sceneObject().scene();
        Service service = sceneObject().service();
        service.renderCameraSeparately(sceneObject().getComponentOfType(Camera.class),scene);
        
    }
    
}
