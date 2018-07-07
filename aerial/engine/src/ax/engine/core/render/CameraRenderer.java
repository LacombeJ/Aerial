package ax.engine.core.render;

import ax.engine.core.Camera;
import ax.engine.core.Component;
import ax.engine.core.Scene;
import ax.engine.core.Service;

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
