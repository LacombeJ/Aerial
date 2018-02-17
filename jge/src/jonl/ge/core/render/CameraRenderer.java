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
        
        Scene scene = gameObject().scene();
        Service service = gameObject().service();
        service.renderCameraSeparately(gameObject().getComponentOfType(Camera.class),scene);
        
    }
    
}
