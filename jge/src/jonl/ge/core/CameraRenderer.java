package jonl.ge.core;

/**
 * Adds rendering functionality outside of render loop.
 * 
 * @author Jonathan Lacombe
 *
 */
public class CameraRenderer extends Component {

    public void render() {
        
        Scene scene = gameObject().scene();
        AppRenderer renderer = (AppRenderer) scene.application.getRenderer();
        renderer.renderCameraSeparately(gameObject().getComponentOfType(Camera.class),scene);
        
    }
    
}
